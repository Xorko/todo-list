package org.xorko.todolist;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.xorko.todolist.controller.RootLayoutController;
import org.xorko.todolist.controller.TodoController;
import org.xorko.todolist.db.Database;
import org.xorko.todolist.db.Queries;
import org.xorko.todolist.model.Task;
import org.xorko.todolist.util.DateUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private GridPane todo;

    private final ObservableList<Task> taskData = FXCollections.observableArrayList();

    public MainApp() {
        loadTasks();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Todo list");
        initRootLayout();
        showTodo();
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
            todo = loader.load();

            // Set TodoView into the center of the root layout
            rootLayout.setCenter(todo);

            // Give the controller access to the main app
            TodoController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load tasks from the database and refresh taskData
     */
    public void loadTasks() {
        ResultSet rs;
        try {
            Connection connection = Database.getConnection();
            String query = Queries.getQuery("task.select.all");
            PreparedStatement stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            Task tempTask;
            taskData.clear();
            if (rs != null) {
                while (rs.next()) {
                    tempTask = new Task();
                    tempTask.setTaskID(rs.getInt("id"));
                    tempTask.setName(rs.getString("name"));
                    if (rs.getString("date") != null) {
                        if (!rs.getString("date").isEmpty()) {
                            LocalDate date = DateUtil.parse(rs.getString("date"));
                            tempTask.setDate(date);
                        }
                    }
                    tempTask.setDone(rs.getBoolean("done"));
                    taskData.add(tempTask);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not access to queries.properties");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Task> getTaskData() {
        return taskData;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
