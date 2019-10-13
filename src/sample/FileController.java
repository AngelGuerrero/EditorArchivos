package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class FileController {

    Response response;


    @FXML
    TextField textFieldFileName;


    @FXML
    Button btnAccept;


    @FXML
    private Button btnCancel;


    static Stage stage;


    private FXMLLoader fxmlLoader;


    /// --------------------------------------------------
    /// Public methods
    /// --------------------------------------------------


    FileController() {
        //
        // Create and initialize instance variables
        this.response = new Response();

        //
        // Config the window options
        this.fxmlLoader = new FXMLLoader(getClass().getResource("FileView.fxml"));
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setMaximized(false);
    }


    void loadController(FileController pController) throws IOException {
        this.fxmlLoader.setController(pController);
        this.fxmlLoader.load();
        stage.setScene(new Scene(this.fxmlLoader.getRoot()));
    }


    void createNewFile() {

        if (this.textFieldFileName.getText().equals("")) {
            this.response = new Response(false, null, "No entered file name");
            return;
        }

        String newFileName = this.normalizeFileName(this.textFieldFileName.getText());

        //
        // Save file, fileChooser config
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(newFileName);
        fileChooser.setTitle("Save file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("SQL files (*.sql)", ".sql")
        );

        File file = fileChooser.showSaveDialog(this.btnAccept.getScene().getWindow());

        //
        // Cancel save dialog ?
        if (file == null) {
            this.response = new Response(false, null, "Cancel dialog for creating file");
            return;
        }

        try {
            FileWriter writer = new FileWriter(file);
            writer.close();
        } catch (IOException e) {
            this.response = new Response(false, file, "Error creating file: " + e.getMessage());
            return;
        }

        this.response = new Response(true, file, "File, created successfully!");

        this.closeWindow();
    }


    void renameFile(File pFile, String pNewNameFile) {

        if (pNewNameFile.equals("")) return;

        if (!this.haveExtensionFile(pNewNameFile)) {
            this.response = new Response(false, null, "You must entered a file extension");
            Message.showError("Error renaming file", null, this.response.message);
            return;
        }

        String path = pFile.getParent() + File.separator + pNewNameFile;
        File newFile = new File(path);

        if (!pFile.renameTo(newFile)) {
            this.response = new Response(false, null, "Something went wrong renaming file");
            return;
        }

        this.response = new Response(true, newFile, "renamed successfully!");

        this.closeWindow();
    }


    private void closeWindow() {
        stage.close();
    }


    /// --------------------------------------------------
    /// Private methods
    /// --------------------------------------------------


    void commonInitializeControls() {
        //
        // Key events for Text Field
        // this.textFieldFileName.setOnKeyReleased(e -> this.newFileName = this.textFieldFileName.getText());
        //
        // Action for cancel button
        this.btnCancel.setOnAction(e -> {
            this.response = new Response(false, null, "cancel");
            this.closeWindow();
        });
    }


    private String normalizeFileName(String pStr) {
        return pStr.replaceAll("\\s+", "");
    }


    private boolean haveExtensionFile(String pFilename) {
        boolean flag = false;

        // String extension = "";

        int i = pFilename.lastIndexOf('.');
        int p = Math.max(pFilename.lastIndexOf('/'), pFilename.lastIndexOf('\\'));

        if (i > p) {
            // extension = pFilename.substring(i+1);
            flag = true;
        }

        return flag;
    }
}
