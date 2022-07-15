package Pane;

import java.net.URL;
import java.util.ResourceBundle;
import Core.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Bookmark {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea definition;

    @FXML
    private Label pronounceWord;

    @FXML
    private ListView<Word> results;

    @FXML
    private TextField searchField;

    @FXML
    private Button speakBtn;

    @FXML
    private Button unmarkBtn;

    private ObservableList<Word> list;

    private void chooseWord(Word word) {
        if (word == null) {
            definition.clear();
            pronounceWord.setText("");
        } else {
            Main.controller.loadExample(word);
            definition.setText(word.toString() + "\n" + word.getExamplesToString());
            pronounceWord.setText(word.getWord_phonetic());
        }
    }

    @FXML
    void onKeyReleasedSearchField(KeyEvent event) {
        try {
            list = FXCollections.observableArrayList(Main.controller.searchMark(searchField.getText().toLowerCase().trim()));
            results.setItems(list);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void onMouseClickListView(MouseEvent event) {
        try {
            Word selectedWord = results.getSelectionModel().getSelectedItem();

            if (selectedWord != null) chooseWord(selectedWord);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void onMouseClickedSpeakBtn(MouseEvent event) {
        try {
            Word selectedWord = results.getSelectionModel().getSelectedItem();
        
            Main.controller.speak(selectedWord.getWord_target());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void onMouseClickedUnmarkBtn(MouseEvent event) {
        try {
            Word selectedWord = results.getSelectionModel().getSelectedItem();
        
            if (selectedWord != null) {
                Main.controller.mark(selectedWord);

                list = FXCollections.observableArrayList(Main.controller.searchMark(searchField.getText().toLowerCase().trim()));
                results.setItems(list);

                chooseWord(null);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void initialize() {
        try {
            assert definition != null : "fx:id=\"definition\" was not injected: check your FXML file 'Bookmark.fxml'.";
            assert pronounceWord != null : "fx:id=\"pronounceWord\" was not injected: check your FXML file 'Bookmark.fxml'.";
            assert results != null : "fx:id=\"results\" was not injected: check your FXML file 'Bookmark.fxml'.";
            assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'Bookmark.fxml'.";
            assert speakBtn != null : "fx:id=\"speakBtn\" was not injected: check your FXML file 'Bookmark.fxml'.";
            assert unmarkBtn != null : "fx:id=\"unmarkBtn\" was not injected: check your FXML file 'Bookmark.fxml'.";
            list = FXCollections.observableArrayList(Main.controller.searchMark(""));
            results.setItems(list);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}