package Core;

import java.io.Serializable;

public class Example implements Serializable {
    private int example_ID, word_ID;
    private String englishEx, vietEx;

    public Example(int word_ID, String englishEx, String vietEx) {
        this.word_ID = word_ID;
        this.englishEx = englishEx;
        this.vietEx = vietEx;
    }

    public Example(int example_ID, int word_ID, String englishEx, String vietEx) {
        this.example_ID = example_ID;
        this.word_ID = word_ID;
        this.vietEx = vietEx;
        this.englishEx = englishEx;
    }

    public String getVietEx() {
        return vietEx;
    }

    public void setVietEx(String vietEx) {
        this.vietEx = vietEx;
    }

    public String getEnglishEx() {
        return englishEx;
    }

    public void setEnglishEx(String englishEx) {
        this.englishEx = englishEx;
    }

    @Override
    public String toString() {
        return " - " + englishEx + "\n - " + vietEx;
    }

    public int getExample_ID() {
        return example_ID;
    }

    public int getWord_ID() {
        return word_ID;
    }

    public void setWord_ID(int word_ID) {
        this.word_ID = word_ID;
    }
}