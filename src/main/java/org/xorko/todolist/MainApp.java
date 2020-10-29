package org.xorko.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.xorko.todolist.controller.RootLayoutController;
import org.xorko.todolist.controller.TodoController;
import org.xorko.todolist.model.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private boolean differentFromSaved;

    private final ObservableList<Task> taskData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Todo list");
        initRootLayout();
        showTodo();
        File file = getFilePath();
        if (file != null) {
            loadTaskDataFromFile(file);

        }
        this.primaryStage.setOnCloseRequest(event -> {
            verifyIfListIsSaved();
            System.exit(0);
        });
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Set the scene
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTodo() {
        try {
            // Load TodoView layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/TodoView.fxml"));
            GridPane todo = loader.load();

            // Set TodoView into the center of the root layout
            rootLayout.setCenter(todo);

            // Give the controller access to the main app
            TodoController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
            primaryStage.setTitle("Todo list - " + file.getName());
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle("Todo list");
        }
    }

    public void saveTaskDataToFile(File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
            writer.writeValue(file, taskData);
            differentFromSaved = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTaskDataFromFile(File file) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        if (file != null) {
            try {
                CollectionType taskListType = mapper.getTypeFactory().constructCollectionType(List.class, Task.class);
                List<Task> tasks = mapper.readValue(file, taskListType);
                taskData.clear();
                taskData.addAll(tasks);
                setFilePath(file);
                primaryStage.setTitle(primaryStage.getTitle() + " - " + file.getName());
                differentFromSaved = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void verifyIfListIsSaved() {
        if (differentFromSaved) {
            File file = getFilePath();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(getPrimaryStage());
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
                saveTaskDataToFile(file);
            }
        }
    }

    public ObservableList<Task> getTaskData() {
        return taskData;
    }

    public void setDifferentFromSaved(boolean differentFromSaved) {
        this.differentFromSaved = differentFromSaved;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
