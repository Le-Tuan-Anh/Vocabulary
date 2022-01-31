package Pane;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import Core.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class Add {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea addEnglishExample;

    @FXML
    private TextField addExplain;

    @FXML
    private TextField addPhonetic;

    @FXML
    private TextField addTarget;

    @FXML
    private TextArea addVietnameseExample;

    @FXML
    private ImageView canAdd;

    @FXML
    private ImageView cantAdd;

    @FXML
    private Button saveBtn;

    @FXML
    void save(ActionEvent event) {
        try {
            String target = addTarget.getText().toLowerCase().trim();
            String explain = addExplain.getText().toLowerCase().trim();
            String pronounce = addPhonetic.getText().toLowerCase().trim();
            String[] eExample = addEnglishExample.getText().toLowerCase().trim().split("\n");
            String[] vExample = addVietnameseExample.getText().toLowerCase().trim().split("\n");
            List<Example> listEx = new LinkedList<>();
            if (eExample.length > 0 && eExample.length == vExample.length)
                for (int i = 0; i < eExample.length; i++)
                    if (!vExample[i].equals("") && !eExample[i].equals(""))
                        listEx.add(new Example(vExample[i], eExample[i]));
            Word word = new Word(target, explain, pronounce, listEx);

            if (Main.controller.isWord(word)) {
                Main.controller.add(word);
                canAdd.setVisible(true);
                cantAdd.setVisible(false);
            } else {
                cantAdd.setVisible(true);
                canAdd.setVisible(false);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            addTarget.clear();
            addExplain.clear();
            addPhonetic.clear();
            addEnglishExample.clear();
            addVietnameseExample.clear();

            Thread turnOffSymbol = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        canAdd.setVisible(false);
                        cantAdd.setVisible(false);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            };
            turnOffSymbol.start();
        }
    }

    @FXML
    void initialize() {
        assert addEnglishExample != null : "fx:id=\"addEnglishExample\" was not injected: check your FXML file 'Add.fxml'.";
        assert addExplain != null : "fx:id=\"addExplain\" was not injected: check your FXML file 'Add.fxml'.";
        assert addPhonetic != null : "fx:id=\"addPhonetic\" was not injected: check your FXML file 'Add.fxml'.";
        assert addTarget != null : "fx:id=\"addTarget\" was not injected: check your FXML file 'Add.fxml'.";
        assert addVietnameseExample != null : "fx:id=\"addVietnameseExample\" was not injected: check your FXML file 'Add.fxml'.";
        assert canAdd != null : "fx:id=\"canAdd\" was not injected: check your FXML file 'Add.fxml'.";
        assert cantAdd != null : "fx:id=\"cantAdd\" was not injected: check your FXML file 'Add.fxml'.";
        assert saveBtn != null : "fx:id=\"saveBtn\" was not injected: check your FXML file 'Add.fxml'.";
        canAdd.setVisible(false);
        cantAdd.setVisible(false);
    }
}