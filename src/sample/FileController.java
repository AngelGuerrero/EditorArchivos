package sample;

import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FileController extends Application implements Initializable {

    public String filename;


    @Override
    public void start(Stage primaryStage) throws Exception {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void openNewFileWindow() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Nuevo archivo");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setMaximized(false);
        Main.loadView(stage, "NewFileView.fxml");
    }
}
