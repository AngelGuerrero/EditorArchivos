package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        PrincipalController principalController = new PrincipalController();
        principalController.start(stage);
    }


    public static void main(String[] args) {
        launch(args);
    }


    public static void loadView(Stage stage, String filename) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource(filename));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
