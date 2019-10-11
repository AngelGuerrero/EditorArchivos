package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewFileController implements Initializable {

    private String fileName;

    private String pathFile;

    private static Stage stage;

    private String fileNameView = "NewFileView.fxml";

    @FXML
    private TextField textFieldFileName;

    @FXML
    private Button btnAccept;


    public NewFileController() {
        this.fileName = "";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.textFieldFileName.requestFocus();
        this.textFieldFileName.setFocusTraversable(true);
        this.textFieldFileName.focusedProperty();
    }

    public void show() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource(this.fileNameView));

        stage = new Stage();
        stage.setTitle("Nuevo archivo");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.setScene(new Scene(root));

        stage.showAndWait();
    }


    @FXML
    public void closeWindow() {
        stage.close();
    }


    @FXML
    public void onTypedTextField() {
        this.fileName = this.textFieldFileName.getText();
    }


    @FXML
    public void createNewFile() {

        if (this.fileName.equals("")) return;

        this.fileName = this.normalizeFileName(this.fileName);

        //
        // Save file, fileChooser config
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(this.fileName);
        fileChooser.setTitle("Save file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("SQL files (*.sql)", ".sql")
        );

        File file = fileChooser.showSaveDialog(this.btnAccept.getScene().getWindow());

        //
        // Cancel save dialog ?
        if (file == null) return;

        Response response = this.createFile(file);

        if (!response.ok) {
            Message.showError("Error creating the file", response.message,"");
            return;
        }

        //
        // Save the file into static variable
        PrincipalController.staticCurrentOpenedFile = file;

        this.closeWindow();
    }


    private Response createFile(File pFile) {
        try {
            FileWriter file = new FileWriter(pFile);
            file.close();
        } catch (IOException e) {
            return new Response(false, null, e.getMessage());
        }

        return new Response(true, pFile.getName(), "File, created successfully");
    }


    private String normalizeFileName(String pStr) {
        return pStr.replaceAll("\\s+", "");
    }
}
