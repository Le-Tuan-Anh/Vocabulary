package Core;

import java.io.Serializable;

public class Example implements Serializable {
    private String vietEx, englishEx;

    public Example(String vietEx, String englishEx) {
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
}