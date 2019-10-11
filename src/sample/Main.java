package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private String filenameview = "PrincipalView.fxml";

    @Override
    public void start(Stage pStage) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource(this.filenameview));
        pStage.setTitle("Awesome editor");
        pStage.setScene(new Scene(root));
        pStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
