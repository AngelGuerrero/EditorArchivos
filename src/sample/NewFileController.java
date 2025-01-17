package sample;

import javafx.fxml.FXML;

import java.io.IOException;

final public class NewFileController extends FileController  {

    public NewFileController() throws IOException {
        super();
        this.loadController(this);
    }


    @FXML
    public void initialize() {
        //
        // Initialize the common controls,
        // for common actions between classes
        this.commonInitializeControls();
        //
        // Set the button action for create new file
        this.btnAccept.setOnAction(e -> this.createNewFile());
    }


    public void showAndWaitForCreatingFile() {
        //
        // Configure its own window
        stage.setTitle("New file");
        //
        stage.showAndWait();
    }
}
