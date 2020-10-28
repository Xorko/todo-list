package org.xorko.todolist.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Task {

    private final SimpleIntegerProperty taskID;
    private final SimpleStringProperty name;
    private final SimpleBooleanProperty done;
    private final SimpleObjectProperty<LocalDate> date;

    public Task() {
        this(null, null);
    }

    public Task(String name, LocalDate date) {
        this.taskID = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty(name);
        this.done = new SimpleBooleanProperty();
        this.date = new SimpleObjectProperty<>(date);
    }

    public int getId() {
        return taskID.get();
    }

    public void setTaskID(int id) {
        this.taskID.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return taskID;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public boolean isDone() {
        return done.get();
    }

    public void setDone(boolean done) {
        this.done.set(done);
    }

    public SimpleBooleanProperty doneProperty() {
        return done;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }
}