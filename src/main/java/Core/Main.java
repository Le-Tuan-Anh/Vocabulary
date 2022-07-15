package Core;

import java.io.FileInputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Controller controller = new Controller();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Class.forName("com.mysql.jdbc.Driver");
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(new FileInputStream("src/resources/FXML/Home.fxml"));
            Scene scene = new Scene(root);
            String css = this.getClass().getResource("GUI.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
    }
}

// Note: fxml use absolute path