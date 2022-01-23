package Pane;

import Core.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Search {
    @FXML
    private AnchorPane bigPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button cancelEditBtn;

    @FXML
    private Button confirmEditBtn;

    @FXML
    private TextArea definition;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    @FXML
    private AnchorPane editPane;

    @FXML
    private TextArea editedEnglishEx;

    @FXML
    private TextField editedExplain;

    @FXML
    private TextField editedPhonetic;

    @FXML
    private TextField editedTarget;

    @FXML
    private TextArea editedVietnameseEx;

    @FXML
    private Button markbtn;

    @FXML
    private Label pronounce;

    @FXML
    private ListView<Word> results;

    @FXML
    private TextField searchField;

    @FXML
    private Button speakBtn;

    @FXML
    private ImageView starSymbol;

    private ObservableList<Word> list;

    private void chooseWord(Word word) {
        if (word == null) {
            definition.clear();
            starSymbol.setVisible(false);
            pronounce.setText("");
        } else {
            definition.setText(word.toString() + "\n" + word.getExamplesToString());
            starSymbol.setVisible(word.isMark() ? true : false);
            pronounce.setText(word.getWord_phonetic());
        }
    }

    @FXML
    void onKeyReleasedSearchField(KeyEvent event) {
        try {
            list = FXCollections.observableArrayList(Main.controller.search(searchField.getText().toLowerCase().trim()));
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
    void onMouseClickedCancelEditBtn(MouseEvent event) {
        editPane.setVisible(false);
        bigPane.setDisable(false);
    }

    @FXML
    void onMouseClickedConfirmEditBtn(MouseEvent event) {
        try {
            Word selectedWord = results.getSelectionModel().getSelectedItem();
            String[] eExample = editedEnglishEx.getText().toLowerCase().trim().split("\n");
            String[] vExample = editedVietnameseEx.getText().toLowerCase().trim().split("\n");
            List<Example> listEx = new LinkedList<>();
            if (eExample.length > 0 && eExample.length == vExample.length)
                for (int i = 0; i < eExample.length; i++)
                    if (!vExample[i].equals("") && !eExample[i].equals(""))
                        listEx.add(new Example(vExample[i], eExample[i]));
            Word editedWord = new Word(editedTarget.getText(), editedExplain.getText(), editedPhonetic.getText(), listEx);

            if (selectedWord != null && Main.controller.isWord(editedWord)) {
                Main.controller.saveEdited(selectedWord, editedWord);

                // update bigPane
                list = FXCollections.observableArrayList(Main.controller.search(editedWord.getWord_target()));
                results.setItems(list);
                results.getSelectionModel().select(editedWord);
                chooseWord(editedWord);
            }

            editPane.setVisible(false);
            bigPane.setDisable(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void onMouseClickedDeleteBtn(MouseEvent event) {
        try {
            Word selectedWord = results.getSelectionModel().getSelectedItem();

            if (selectedWord != null) {
                Main.controller.delete(selectedWord);

                list = FXCollections.observableArrayList(Main.controller.search(searchField.getText().toLowerCase().trim()));
                results.setItems(list);
                if (list.size() > 0) {
                    chooseWord(list.get(0));
                } else {
                    chooseWord(null);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void onMouseClickedEditBtn(MouseEvent event) {
        try {
            Word selectedWord = results.getSelectionModel().getSelectedItem();
        
            if (selectedWord != null) {
                editPane.setVisible(true);
                bigPane.setDisable(true);

                editedTarget.setText(selectedWord.getWord_target());
                editedExplain.setText(selectedWord.getWord_explain());
                editedPhonetic.setText(selectedWord.getWord_phonetic());

                String englishEx = "", vietnameseEx = "";
                for (Example i: selectedWord.getExamples()) {
                    englishEx += i.getEnglishEx() + "\n";
                    vietnameseEx += i.getVietEx() + "\n";
                }

                editedEnglishEx.setText(englishEx);
                editedVietnameseEx.setText(vietnameseEx);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void onMouseClickedMarkBtn(MouseEvent event) {
        try {
            Word selectedWord = results.getSelectionModel().getSelectedItem();
        
            // change mark
            if (selectedWord != null) {
                Main.controller.mark(selectedWord);
                starSymbol.setVisible(selectedWord.isMark() ? true : false);
            }
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
    void initialize() {
        assert cancelEditBtn != null : "fx:id=\"cancelEditBtn\" was not injected: check your FXML file 'Search.fxml'.";
        assert confirmEditBtn != null : "fx:id=\"confirmEditBtn\" was not injected: check your FXML file 'Search.fxml'.";
        assert definition != null : "fx:id=\"definition\" was not injected: check your FXML file 'Search.fxml'.";
        assert deleteBtn != null : "fx:id=\"deleteBtn\" was not injected: check your FXML file 'Search.fxml'.";
        assert editBtn != null : "fx:id=\"editBtn\" was not injected: check your FXML file 'Search.fxml'.";
        assert editPane != null : "fx:id=\"editPane\" was not injected: check your FXML file 'Search.fxml'.";
        assert editedEnglishEx != null : "fx:id=\"editedEnglishEx\" was not injected: check your FXML file 'Search.fxml'.";
        assert editedExplain != null : "fx:id=\"editedExplain\" was not injected: check your FXML file 'Search.fxml'.";
        assert editedPhonetic != null : "fx:id=\"editedPhonetic\" was not injected: check your FXML file 'Search.fxml'.";
        assert editedTarget != null : "fx:id=\"editedTarget\" was not injected: check your FXML file 'Search.fxml'.";
        assert editedVietnameseEx != null : "fx:id=\"editedVietnameseEx\" was not injected: check your FXML file 'Search.fxml'.";
        assert markbtn != null : "fx:id=\"markbtn\" was not injected: check your FXML file 'Search.fxml'.";
        assert pronounce != null : "fx:id=\"pronounce\" was not injected: check your FXML file 'Search.fxml'.";
        assert results != null : "fx:id=\"results\" was not injected: check your FXML file 'Search.fxml'.";
        assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'Search.fxml'.";
        assert speakBtn != null : "fx:id=\"speakBtn\" was not injected: check your FXML file 'Search.fxml'.";
        assert starSymbol != null : "fx:id=\"starSymbol\" was not injected: check your FXML file 'Search.fxml'.";
    }
}