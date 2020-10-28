package org.xorko.todolist.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.xorko.todolist.MainApp;
import org.xorko.todolist.db.Database;
import org.xorko.todolist.db.Queries;
import org.xorko.todolist.model.Task;
import org.xorko.todolist.util.DateUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class TodoController {

    @FXML
    private TextField nameField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private CheckBox hasNoDateCheck;
    @FXML
    private Button newTask;
    @FXML
    private Button editTask;
    @FXML
    private Button deleteTask;
    @FXML
    private Button saveTask;
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> taskNameColumn;
    @FXML
    private TableColumn<Task, String> taskDateColumn;
    @FXML
    private TableColumn<Task, CheckBox> taskStatusColumn;

    private MainApp mainApp;

    private Task currentlyEdited;

    @FXML
    private void initialize() {
        taskNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        taskDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getDate())));
        taskStatusColumn.setCellValueFactory(cellData -> {
            CheckBox checkBox = new CheckBox();
            // Set the state of the object to the checkbox
            checkBox.setSelected(cellData.getValue().isDone());
            // Change the state in DB
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Connection connection = Database.getConnection();
                    String query = Queries.getQuery("task.update.state");
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setBoolean(1, newValue);
                    stmt.setInt(2, cellData.getValue().getId());
                    stmt.execute();
                    mainApp.loadTasks();
                } catch (IOException e) {
                    System.out.println("Could not access to queries.properties");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        // Set the date format for the DatePicker
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return DateUtil.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });
    }

    @FXML
    private void handleNewTask() {
        if (fieldsAreValid()) {
            try {
                Connection connection = Database.getConnection();
                String query;
                PreparedStatement stmt;
                if (hasNoDateCheck.isSelected()) {
                    query = Queries.getQuery("task.insert.name");
                    stmt = connection.prepareStatement(query);
                } else {
                    query = Queries.getQuery("task.insert");
                    stmt = connection.prepareStatement(query);
                    stmt.setString(2, datePicker.getEditor().getText());
                }
                stmt.setString(1, nameField.getText());
                stmt.execute();
                mainApp.loadTasks();
            } catch (IOException e) {
                System.out.println("Could not access to queries.properties");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEditTask() {
        Task selectedTask = taskTable.getSelectionModel().selectedItemProperty().getValue();
        currentlyEdited = selectedTask;
        if (selectedTask != null) {
            // Button disabling is only for safety reason, it's not needed to work correctly
            saveTask.setVisible(true);
            saveTask.setDisable(false);
            newTask.setVisible(false);
            newTask.setDisable(true);
            nameField.setText(selectedTask.getName());
            if (selectedTask.getDate() != null) {
                datePicker.getEditor().setText(DateUtil.format(selectedTask.getDate()));
                hasNoDateCheck.setSelected(false);
            } else {
                // We need to reset these in case we had loaded a task with date before
                datePicker.getEditor().setText("");
                hasNoDateCheck.setSelected(true);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No task selected");
            alert.setContentText("Please select a task in the table.");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleSaveTask() {
        if (fieldsAreValid()) {
            System.out.println("Saved");
            // Button disabling is only for safety reason, it's not needed to work correctly
            newTask.setVisible(true);
            newTask.setDisable(false);
            saveTask.setVisible(false);
            saveTask.setDisable(true);
            try {
                Connection connection = Database.getConnection();
                String query;
                PreparedStatement stmt;
                if (hasNoDateCheck.isSelected()) {
                    query = Queries.getQuery("task.update.name");
                    stmt = connection.prepareStatement(query);
                    stmt.setInt(2, currentlyEdited.getId());
                } else {
                    query = Queries.getQuery("task.update.name.date");
                    stmt = connection.prepareStatement(query);
                    stmt.setString(2, datePicker.getEditor().getText());
                    stmt.setInt(3, currentlyEdited.getId());
                }
                stmt.setString(1, nameField.getText());
                stmt.execute();
                currentlyEdited = null;
                mainApp.loadTasks();
            } catch (IOException e) {
                System.out.println("Could not access to queries.properties");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().selectedItemProperty().getValue();
        if (selectedTask != null) {
            try {
                Connection connection = Database.getConnection();
                String query = Queries.getQuery("task.delete");
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, selectedTask.getId());
                stmt.execute();
                mainApp.loadTasks();
            } catch (IOException e) {
                System.out.println("Could not access to queries.properties");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No task selected");
            alert.setContentText("Please select a task in the table.");

            alert.showAndWait();
        }
    }

    private boolean fieldsAreValid() {
        String errMessage = "";
        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errMessage += "No valid task name\n";
        }
        if (!hasNoDateCheck.isSelected() && (datePicker.getEditor().getText() == null || !DateUtil.validDate(datePicker.getEditor().getText()))) {
            errMessage += "No valid date selected\n";
        }

        if (errMessage.length() == 0) {
            return true;
        }
        // Else there is at least one error
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Invalid Fields");
        alert.setHeaderText("Please correct invalid fields");
        alert.setContentText(errMessage);

        alert.showAndWait();

        return false;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        taskTable.setItems(mainApp.getTaskData());
    }
}
