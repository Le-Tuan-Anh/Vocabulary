import Core.Main;
import Core.Word;

public class Test {
    public static void main(String[] args) {
        Word word = new Word("word_target", "word_explain", "word_phonetics");
        System.out.println(Main.controller.include(word));
        Main.controller.add(word);
        System.out.println(Main.controller.include(word));
        Main.controller.delete(word);
        System.out.println(Main.controller.include(word));
    }
}
