package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;

public class PrincipalController {


    private static File staticCurrentOpenedFile;

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

    @FXML
    private Label lblDocumentName;

    @FXML
    private Label lblDocumentStatus;

    @FXML
    private VBox vboxCenter;

    //
    // Text Area
    private TextArea textAreaEditor;

    //
    // TreeView Editor
    @FXML
    private TreeView treeViewEditor;

    // Root item 'opened files'
    private TreeItem rootItemOpenedFiles;


    /// --------------------------------------------------
    /// Public methods
    /// --------------------------------------------------


    public PrincipalController() {
        staticCurrentOpenedFile = null;
    }


    @FXML
    public void initialize() {
        //
        // Short Keys registration
        //
        // New File CTR+N
        this.menuItemNewFile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        // Open file CTR+O
        this.menuItemOpenFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        // Save file
        this.menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        // Rename file menu
        this.menuItemRenameFile.setDisable(true);
        // Close file
        this.menuItemCloseFile.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        // Explorer files
        this.menuItemToggleExplorer.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        //
        // Tree view editor
        this.rootItemOpenedFiles = new TreeItem("OPEN EDITORS");
        //
        // Tree view editor, add root items to tree view
        this.treeViewEditor.setRoot(this.rootItemOpenedFiles);
        this.textAreaEditor = new TextArea();
        this.textAreaEditor.prefWidthProperty().bind(this.vboxCenter.widthProperty());
        this.textAreaEditor.setPrefRowCount(10);
    }


    @FXML
    public void closeApp() {
        System.exit(0);
    }


    @FXML
    public void showNewFileWindow() throws Exception {
        NewFileController newFileController = new NewFileController();
        newFileController.showAndWaitForCreatingFile();

        if (newFileController.response.ok) {
            //
            // Opens just created file
            this.openFile((File)newFileController.response.data);
        }
    }


    @FXML
    public void showRenameFileWindow() throws Exception {

        if (staticCurrentOpenedFile == null) return;

        RenameFileController renameFileController = new RenameFileController(staticCurrentOpenedFile);
        renameFileController.showAndWaitForRenameFile();

        if (renameFileController.response.ok) {
            // - remove tree list
            // - add tree list
            // - change name
            // - show status
            // - change static file
            this.removeTreeViewItem(staticCurrentOpenedFile.getName(), this.rootItemOpenedFiles);
            this.addTreeViewItem(((File)renameFileController.response.data).getName(), this.rootItemOpenedFiles);

            Message.setLabelSuccess(this.lblDocumentName, ((File)renameFileController.response.data).getName());
            Message.setLabelSuccess(this.lblDocumentStatus, renameFileController.response.message);
            staticCurrentOpenedFile = (File)renameFileController.response.data;
        } else if (!renameFileController.response.ok && !renameFileController.response.message.equals("cancel")) {
            Message.showError("Error renaming file", renameFileController.response.message, "");
            Message.setLabelError(this.lblDocumentStatus, renameFileController.response.message);
        }
    }


    @FXML
    public void showOpenFileDialog() {
        //
        // Open file chooser dialog
        FileChooser fileChooser = new FileChooser();
        // fileChooser.setInitialDirectory(new File(initialDir));
        File selectedFile = fileChooser.showOpenDialog(this.vboxCenter.getScene().getWindow());

        if (selectedFile == null) return;

        //
        // Close the current file if is 'opened'
        this.closeCurrentFile();
        //
        // Open and show the selected file in the view
        this.openFile(selectedFile);
    }


    @FXML
    public void saveFile() {
        //
        // No file selected
        if (staticCurrentOpenedFile == null) return;

        Response response = this.saveFile(this.textAreaEditor, staticCurrentOpenedFile);

        if (response.ok) {
            Message.setLabelSuccess(this.lblDocumentStatus, response.message);
        } else {
            Message.setLabelError(this.lblDocumentStatus, response.message);
        }
    }


    @FXML
    public void closeCurrentFile() {
        if (!this.vboxCenter.getChildren().contains(this.textAreaEditor)) return;

        //
        // Clear the text area
        this.textAreaEditor.clear();
        //
        // Removes text area and puts initial message
        this.vboxCenter.getChildren().remove(this.textAreaEditor);
        this.vboxCenter.getChildren().add(this.lblInitialMessage);
        VBox.setVgrow(this.lblInitialMessage, Priority.ALWAYS);
        //
        // Disable option for rename file
        this.menuItemRenameFile.setDisable(true);
        //
        // Show the messages for user
        Message.setLabelInfo(this.lblDocumentName, "No file selected");
        Message.setLabelSuccess(this.lblDocumentStatus, "Ready!");
        //
        // Remove the item in three view
        this.removeTreeViewItem(staticCurrentOpenedFile.getName(), this.rootItemOpenedFiles);

        staticCurrentOpenedFile = null;
    }


    /// --------------------------------------------------
    /// Private methods
    /// --------------------------------------------------


    private void openFile(File pFile) {
        //
        // Load the file content into text area component
        Response response = this.loadAndShowFileContent(this.textAreaEditor, pFile);

        if (!response.ok) {
            Message.showError("Error reading the text file", response.message, "");
            Message.setLabelError(this.lblDocumentStatus, response.message);
            return;
        }

        //
        // Save the file in static variable
        staticCurrentOpenedFile = pFile;
        //
        // Shows the file in tree view editor
        this.addTreeViewItem(pFile.getName(), this.rootItemOpenedFiles);
        //
        // Enable option for rename file
        this.menuItemRenameFile.setDisable(false);
        //
        // Show the messages to principal window
        Message.setLabelInfo(this.lblDocumentName, pFile.getName());
        Message.setLabelSuccess(this.lblDocumentStatus, response.message);
    }


    private Response saveFile(TextArea pTextArea, File pFile) {
        try {
            FileWriter fileWriter = new FileWriter(pFile);
            fileWriter.write(pTextArea.getText());
            fileWriter.close();
        } catch (IOException e) {
            return new Response(false, null, e.getMessage());
        }

        return new Response(true, pFile, "saved successfully!");
    }


    private Response loadAndShowFileContent(TextArea pTextArea, File pFile) {
        pTextArea.clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pFile));
            String str;

            while ((str = reader.readLine()) != null) {
                pTextArea.appendText(str);
                pTextArea.appendText(System.getProperty("line.separator"));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            return new Response(false, null, "not found " + e.getMessage());
        } catch (IOException e) {
            return new Response(false, null, "Error reading file: " + e.getMessage());
        }

        this.vboxCenter.getChildren().remove(this.lblInitialMessage);
        this.vboxCenter.getChildren().add(this.textAreaEditor);
        VBox.setVgrow(this.textAreaEditor, Priority.ALWAYS);

        return new Response(true, (File) pFile, "loaded successfully!");
    }


    private void addTreeViewItem(String pItem, TreeItem pTreeViewRootItem) {
        if (pTreeViewRootItem.getChildren().contains(pItem)) return;

        pTreeViewRootItem.getChildren().add(new TreeItem(pItem));

        pTreeViewRootItem.setExpanded(true);
    }


    private void removeTreeViewItem(String pItem, TreeItem pTreeViewRootItem) {
        pTreeViewRootItem.getChildren().removeIf(item -> item.toString().contains(pItem));
    }
}
