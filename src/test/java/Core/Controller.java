package Core;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.WriteAbortedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    private final int minSizeOfListToShow = 0, minSizeOfMarkListToShow = 0;
    private final File wordListFile = new File(System.getProperty("user.dir") + "/src/resources/wordList.txt");
    private List<Word> list = new ArrayList<>();

    public void loadFromFile() {
        try {
            Thread loadData = new Thread() {
                @Override
                public void run() {
                    ObjectInputStream in = null;
        
                    try {
                        in = new ObjectInputStream(new FileInputStream(wordListFile));
                        
                        // need to update
                        Object word;
                        while ((word = in.readObject()) != null) {
                            list.add((Word) word);
                        }
        
                        in.close();
                    } catch (EOFException|WriteAbortedException e) {
                        // do nothing when in.readObject() == null
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            };

            loadData.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveToFile() {
        try {
            Thread saveData = new Thread() {
                @Override
                public void run() {
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(wordListFile));
            
                        for (Word i: list)
                            out.writeObject(i);
            
                        out.close();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            };

            saveData.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    public void add(Word word) {
        int left = 0, right = list.size()-1, mid;

        if (list.isEmpty() || list.get(right).compareTo(word) < 0) {
            list.add(word);
            saveToFile();
            return;
        } else if (list.get(0).compareTo(word) > 0) {
            list.add(0, word);
            saveToFile();
            return;
        }
        
        while (right > left + 1) {
            mid = left + (right - left)/2;
            int cmp = list.get(mid).compareTo(word);
            
            if (cmp > 0) right = mid;
            else if (cmp < 0) left = mid;
            else return;
        }

        list.add(left + 1, word);
        saveToFile();
    }

    public void delete(Word word) {
        if (include(word)) {
            list.remove(word);
            saveToFile();
        }
    }

    public void saveEdited(Word old, Word newWord) {
        if (old.getWord_explain() != newWord.getWord_explain())
            old.setWord_explain(newWord.getWord_explain());
        if (old.getWord_target() != newWord.getWord_target())
            old.setWord_target(newWord.getWord_target());
        if (old.getWord_phonetic() != newWord.getWord_phonetic())
            old.setWord_phonetic(newWord.getWord_phonetic());
        old.setExamples(newWord.getExamples());
        saveToFile();
    }

    public List<Word> search(String find) {
        if (find == "")
            return list;

        List<Word> ans = new ArrayList<>();

        // ans.elements get index of find == 0
        if (list.size() < 7) {
            list.forEach(i -> {
                if (i.getWord_target().indexOf(find) == 0)
                    ans.add(i);
            });
        } else {
            int left = 0, right = list.size()-1, mid = 0;

            while (left < right - 1) {
                mid = left + (right - left)/2;

                if (list.get(mid).getWord_target().indexOf(find) == 0)
                    break;
                
                int cmp = list.get(mid).getWord_target().compareTo(find);
                if (cmp < 0) left = mid;
                else right = mid;
            }

            if (left < right - 1) {
                for (int i = mid; i >= left; i--)
                    if (list.get(i).getWord_target().indexOf(find) == 0)
                        ans.add(list.get(i));
                    else break;
                for (int i = mid + 1; i <= right; i++)
                    if (list.get(i).getWord_target().indexOf(find) == 0)
                        ans.add(list.get(i));
                    else break;
            }
        }

        // make sure ans not too short
        if (!ans.isEmpty())
            return ans;
        else
            return ans.size() >= minSizeOfListToShow ? ans : search(find.substring(0, find.length()-2));
    }

    public void mark(Word word) {
        if (word.isMark())
            word.setMark(false);
        else word.setMark(true);
        saveToFile();
    }

    public List<Word> searchMark(String find) {
        List<Word> ans = new ArrayList<>();

        if (find == "") {
            list.forEach(i -> {
                if (i.isMark())
                    ans.add(i);
            });

            return ans;
        } else {
            list.forEach(i -> {
                if (i.getWord_target().contains(find) && i.isMark())
                    ans.add(i);
            });

            if (!ans.isEmpty() && ans.get(0).getWord_target().indexOf(find) == 0)
                return ans;
            else
                return ans.size() >= minSizeOfMarkListToShow ? ans : search(find.substring(0, find.length()-2));
        }
    }

    public boolean include(Word word) {
        return Collections.binarySearch(list, word) >= 0;
    }

    public boolean isWord(Word word) {
        return !word.getWord_explain().equals("")
            && !word.getWord_phonetic().equals("")
            && !word.getWord_target().equals("");
    }
    
}