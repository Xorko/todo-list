package org.xorko.todolist.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import org.xorko.todolist.MainApp;
import org.xorko.todolist.model.Task;

import java.io.File;
import java.util.Optional;

public class RootLayoutController {

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem newMenuItem;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;
    @FXML
    private MenuItem quitMenuItem;
    @FXML
    private MenuItem aboutMenuItem;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        if (System.getProperties().getProperty("os.name").toLowerCase().contains("mac")) {
            // Set the menu bar as macOS menu bar
            // TODO Fix menu bar frozen at launch on macOS
            // https://stackoverflow.com/a/39627830 may fix that but it doesn't work on macOS 10.15
            Platform.runLater(() -> menuBar.setUseSystemMenuBar(true));
            // Set shortcuts for macOS
            newMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.N, KeyCombination.META_DOWN));
            openMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.O, KeyCombination.META_DOWN));
            saveMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN));
            saveAsMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.META_DOWN));
            quitMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.Q, KeyCombination.META_DOWN));
        }
    }

    @FXML
    private void handleNew() {
        mainApp.getTaskData().clear();
        mainApp.setFilePath(null);
    }
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Extension filter
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            Task.s_id = 0;
            mainApp.loadTaskDataFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
        File file = mainApp.getFilePath();
        if (file != null) {
            mainApp.saveTaskDataToFile(file);
        } else {
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Extension filter
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure the file has the correct extension
            if (!file.getPath().endsWith(".json")) {
                file = new File(file.getPath() + ".json");
            }
            mainApp.saveTaskDataToFile(file);
        }
    }

    @FXML
    private void handleQuit() {
        // Check if current list has been saved. If not, ask the user if he wants to save
        if (mainApp.isDifferentFromSaved()) {
            File file = mainApp.getFilePath();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            if (file != null) {
                alert.setTitle("Current list has not been saved");
                alert.setHeaderText("Current list has not been saved");
                alert.setContentText("Save ?");
            }
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(noButton, yesButton);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                mainApp.saveTaskDataToFile(file);
            }
        }
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setHeaderText("Todo list");
        about.setContentText("Author: Xorko\nGitHub: https://www.github.com/Xorko");
        about.showAndWait();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
