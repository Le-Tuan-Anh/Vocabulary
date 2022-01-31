package Pane;

import java.io.FileInputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Home {
    private FXMLLoader loader;

    public Home() {
    }

    @FXML
    private Button addBtn;

    @FXML
    private Button bookmarkBtn;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button searchBtn;

    @FXML
    private Button testBtn;

    private void showComponent(String path) {
        try {
            loader = new FXMLLoader();
            AnchorPane component = loader.load(new FileInputStream(path));
            mainPane.getChildren().clear();
            mainPane.getChildren().add(component);
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
    }

    @FXML
    void onAddBtnClicked(MouseEvent event) {
        showComponent("src/resources/FXML/Add.fxml");
    }

    @FXML
    void onBookmarkBtnClicked(MouseEvent event) {
        showComponent("src/resources/FXML/Bookmark.fxml");
    }

    @FXML
    void onSearchBtnClicked(MouseEvent event) {
        showComponent("src/resources/FXML/Search.fxml");
    }

    @FXML
    void onTestBtnClicked(MouseEvent event) {

    }

    @FXML
    public void initialize() {
        showComponent("src/resources/FXML/Search.fxml");
    }
}
