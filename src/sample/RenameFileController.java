package sample;

import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;

final public class RenameFileController extends FileController {

    private File file;


    RenameFileController(File file) throws IOException {
        super();
        this.loadController(this);
        this.file = file;
    }


    @FXML
    public void initialize() {
        //
        // Initialize the common controls,
        // for common actions between classes
        this.commonInitializeControls();
        //
        // Set the button action for create new file
        this.btnAccept.setOnAction(e -> this.renameFile(this.file, this.textFieldFileName.getText()));
    }


    void showAndWaitForRenameFile() {
        //
        // Configure its own window
        stage.setTitle("Rename file");
        //
        stage.showAndWait();
    }
}
