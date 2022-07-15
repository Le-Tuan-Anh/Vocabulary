package Core;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Word implements Comparable<Word>, Serializable {
    private final int word_ID;
    private String word_target, word_explain, word_phonetic;
    private List<Example> examples = new LinkedList<>();
    private boolean mark = false;
    private final LocalDateTime time = LocalDateTime.now();
    public static final Comparator<Word> TIME_COMPARATOR = new CompareByTime();
    public static final Comparator<Word> WORD_COMPARATOR = new CompareByTarget();
    
    public String getWord_target() {
        return word_target;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public String getWord_phonetic() {
        return word_phonetic;
    }

    public void setWord_phonetic(String word_phonetic) {
        this.word_phonetic = word_phonetic;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    public void addExamples(Example example) {
        examples.add(example);
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public void mark() {
        this.mark = !mark;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public int compareTo(Word arg0) {
        return word_target.compareTo(arg0.getWord_target());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((word_target == null) ? 0 : word_target.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Word other = (Word) obj;
        if (word_target == null) {
            if (other.word_target != null)
                return false;
        } else if (!word_target.equals(other.word_target))
            return false;
        return true;
    }

    private static class CompareByTime implements Comparator<Word> {
        @Override
        public int compare(Word a, Word b) {
            return a.getTime().compareTo(b.getTime());
        }
    }

    private static class CompareByTarget implements Comparator<Word> {
        @Override
        public int compare(Word a, Word b) {
            return a.getWord_target().compareTo(b.getWord_target());
        }
    }

    public Word(int id, String word_target, String word_explain, String word_phonetic, boolean mark) {
        this.word_ID = id;
        this.word_target = word_target;
        this.word_explain = word_explain;
        this.word_phonetic = word_phonetic;
        this.mark = mark;
    }

    public Word(int id, String word_target, String word_explain, String word_phonetic, List<Example> examples, boolean mark) {
        this.word_ID = id;
        this.word_target = word_target;
        this.word_explain = word_explain;
        this.word_phonetic = word_phonetic;
        this.examples = examples;
        this.mark = mark;
    }

    @Override
    public String toString() {
        return word_target + ": " + word_explain;
    }

    public String getExamplesToString() {
        if (examples.isEmpty()) return "";

        String ans = "Examples:";

        for(Example i: examples)
            ans += "\n" + i.toString() + "\n";

        return ans;
    }

    public int getWord_ID() {
        return word_ID;
    }
}