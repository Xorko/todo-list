package org.xorko.todolist.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.xorko.todolist.MainApp;
import org.xorko.todolist.db.Database;

public class RootLayoutController {

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem quitMenuItem;
    @FXML
    private MenuItem aboutMenuItem;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        if (System.getProperties().getProperty("os.name").toLowerCase().contains("mac")) {
            menuBar.setUseSystemMenuBar(true);
        }
    }

    @FXML
    private void handleQuit() {
        Database.close();
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
