package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class PrincipalController implements Initializable {

    public static boolean sIsFileOpen;

    public static File staticCurrentOpenedFile;

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


    public PrincipalController() {
        sIsFileOpen = false;
        staticCurrentOpenedFile = null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
        // Short Keys registration
        //
        // New File CTR+N
        this.menuItemNewFile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        // Open file CTR+O
        this.menuItemOpenFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        // Save file
        this.menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
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
        newFileController.show();

        if (staticCurrentOpenedFile != null) {
            System.out.println(staticCurrentOpenedFile.getAbsolutePath());
        }
    }


    @FXML
    public void showOpenFileDialog() {
        String initialDir = System.getProperty("user.home");
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
        // Show the messages for user
        Message.setLabelInfo(this.lblDocumentName, "No file selected");
        Message.setLabelSuccess(this.lblDocumentStatus, "Ready!");
        //
        // Remove the item in three view
        this.removeTreeViewItem(staticCurrentOpenedFile.getName(), this.rootItemOpenedFiles);

        staticCurrentOpenedFile = null;
    }


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
        // Show the messages to principal window
        Message.setLabelInfo(this.lblDocumentName, "File: " + pFile.getName());
        Message.setLabelSuccess(this.lblDocumentStatus, "Status: " + response.message);
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