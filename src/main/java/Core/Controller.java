package Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// import java.util.LinkedList;
// import java.util.List;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Controller {
    private final String URL = "jdbc:mysql://localhost:3306/";
    private final String DB_NAME = "english";
    private final String USER_NAME = "root";
    private final String PASSWORD = "admin";
    public Connection connection;
    public Statement statement;
    public int number_of_word;
    public int number_of_example;
    private final int minSizeOfListToShow = 0, maxSizeOfListToShow = 50;

    public Controller() {
        openConnection();
    }

    private void update_number() {
        try {
            String sql = "SELECT count(*) from english.words";
            ResultSet a = statement.executeQuery(sql);
            a.next();
            number_of_word = a.getInt("count(*)");

            sql = "SELECT count(*) from english.examples";
            a = statement.executeQuery(sql);
            a.next();
            number_of_example = a.getInt("count(*)");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void openConnection() {
        try {
            connection = DriverManager.getConnection(URL+DB_NAME, USER_NAME, PASSWORD);
            statement = connection.createStatement();
            update_number();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean includeDB(String wordToCheck) {
        openConnection();
        boolean ans = false;
        try {
            String sql = "SELECT count(*) FROM english.words WHERE word_target = " + "'" + wordToCheck + "'";
            ResultSet a = statement.executeQuery(sql);
            a.next();
            ans = a.getInt(1) > 0;
            a.close();
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ans;
    }

    public void loadExample(Word word) {
        if (!includeDB(word.getWord_target()) || !word.getExamples().isEmpty())
            return;
        openConnection();

        try {
            String sql = "SELECT * FROM english.examples WHERE word_ID = " + word.getWord_ID();
            ResultSet a = statement.executeQuery(sql);
            while (a.next()) {
                word.addExamples(new Example(a.getInt("example_ID"), a.getInt("word_ID"), a.getString("englishEx"), a.getString("vietEx")));
            }

            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void add(Word word) {
        if (includeDB(word.getWord_target()))
            return;
        openConnection();

        try {
            String sql = "INSERT INTO english.words (word_target, word_explain, word_phonetic) values ('" +
                    word.getWord_target() + "', '" + word.getWord_explain() + "', '" + word.getWord_phonetic() + "')";
            statement.execute(sql);

            if (word.getExamples() != null && !word.getExamples().isEmpty()) {
                for (Example example : word.getExamples()) {
                    sql = "INSERT INTO english.examples (word_ID, englishEx, vietEx) value (" +
                        word.getWord_ID() + ", '" + example.getEnglishEx() + "', '" + example.getVietEx() + "')";
                    statement.execute(sql);
                }
            }

            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(Word word) {
        if (!includeDB(word.getWord_target()))
            return;
        openConnection();

        try {
            String sql = "DELETE FROM english.words WHERE word_ID = " + word.getWord_ID();
            statement.execute(sql);
            sql = "DELETE FROM english.examples WHERE word_ID = " + word.getWord_ID();
            statement.execute(sql);

            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveEdited(Word old, Word newWord) {
        if (!includeDB(old.getWord_target()))
            return;

        if (old.getWord_explain() != newWord.getWord_explain())
            old.setWord_explain(newWord.getWord_explain());
        if (old.getWord_target() != newWord.getWord_target())
            old.setWord_target(newWord.getWord_target());
        if (old.getWord_phonetic() != newWord.getWord_phonetic())
            old.setWord_phonetic(newWord.getWord_phonetic());
        old.setExamples(newWord.getExamples());

        openConnection();

        try {
            String sql = "UPDATE english.words SET word_target = '" + newWord.getWord_target() +
                "', word_explain = '" + newWord.getWord_explain() +
                "', word_phonetic = '" + newWord.getWord_phonetic() + "' where word_ID = " + old.getWord_ID();
            statement.execute(sql);
            sql = "DELETE FROM english.examples WHERE word_ID = " + old.getWord_ID();
            statement.execute(sql);

            if (newWord.getExamples() != null && !newWord.getExamples().isEmpty()) {
                for (Example example : newWord.getExamples()) {
                    sql = "INSERT INTO english.examples (word_ID, englishEx, vietEx) value (" +
                        old.getWord_ID() + ", '" + example.getEnglishEx() + "', '" + example.getVietEx() + "')";
                    statement.execute(sql);
                }
            }

            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ObservableList<Word> search(String wordToFind) {
        ObservableList<Word> ans = FXCollections.observableArrayList();
        
        openConnection();
        try {
            String query_words = "SELECT * FROM english.words WHERE word_target like " + "'" + wordToFind + "%'" + " ORDER BY LENGTH(word_target) LIMIT " + maxSizeOfListToShow;
            ResultSet a = statement.executeQuery(query_words);
            
            while(a.next()) {
                ans.add(new Word(a.getInt("word_ID"), a.getString("word_target"), a.getString("word_explain"), a.getString("word_phonetic"), a.getInt("mark") == 1));
            } a.close();
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ans.size() < minSizeOfListToShow && wordToFind.length() <= 1 ?
            search(wordToFind.substring(0, wordToFind.length()-2)) : ans;
    }

    public void mark(Word word) {
        openConnection();

        try {
            word.mark();
            String sql = "UPDATE english.words set mark = " + word.isMark() + " WHERE word_ID = " + word.getWord_ID();
            statement.executeUpdate(sql);
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ObservableList<Word> searchMark(String wordToFind) {
        ObservableList<Word> ans = FXCollections.observableArrayList();
        wordToFind = wordToFind.replace("'", " ");
        
        openConnection();
        try {
            String sql = "SELECT * FROM english.words WHERE word_target like " + "'" + wordToFind + "%'" + " and mark = 1 ORDER BY word_target";
            ResultSet a = statement.executeQuery(sql);
            
            while(a.next()) {
                ans.add(new Word(a.getInt("word_ID"), a.getString("word_target"), a.getString("word_explain"), a.getString("word_phonetic"), true));
            } a.close();
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ans;
    }

    public boolean isWord(Word word) {
        return !word.getWord_explain().equals("")
            && !word.getWord_phonetic().equals("")
            && !word.getWord_target().equals("");
    }
    
    public void speak(String word) {
        Voice voice = VoiceManager.getInstance().getVoice("kevin");
        voice.allocate();
        voice.speak(word);
    }

}