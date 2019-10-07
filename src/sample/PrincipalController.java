package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {

    //
    // Menu items
    //
    @FXML
    private MenuItem menuItemNewFile;

    @FXML
    private MenuItem menuItemOpenFile;

    @FXML
    private MenuItem menuItemSave;

    @FXML
    private MenuItem menuItemSaveAs;

    @FXML
    private MenuItem menuItemOpenLocationFile;

    @FXML
    private MenuItem menuItemRenameFile;

    @FXML
    private MenuItem menuItemCloseFile;

    @FXML
    private MenuItem menuItemDeleteFile;

    @FXML
    private MenuItem menuItemToggleExplorer;

    //
    // Labels
    //
    @FXML
    private Label lblInitialMessage;

    //
    // Text Area
    //
    @FXML
    private TextArea textAreaEditor;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
        // Short Keys registration
        //
        // New File
        this.menuItemNewFile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        // Open file
        this.menuItemOpenFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        // Save file
        this.menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        // Close file
        this.menuItemCloseFile.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        // Explorer files
        this.menuItemToggleExplorer.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
    }


    public void start(Stage stage) throws IOException {
        stage.setTitle("Editor de archivos");
        Main.loadView(stage, "PrincipalView.fxml");
    }


    public void openNewFileWindow() throws IOException {
        FileController fileController = new FileController();
        fileController.openNewFileWindow();
    }
}
