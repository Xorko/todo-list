package org.xorko.todolist.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Task {
    /** The number of tasks created. It will be used to set the task id */
    public static int s_id = 1;

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleBooleanProperty done;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private final SimpleObjectProperty<LocalDate> date;

    public Task() {
        this(null, null);
    }

    public Task(String name, LocalDate date) {
        ++s_id;
        this.id = new SimpleIntegerProperty(s_id);
        this.name = new SimpleStringProperty(name);
        this.done = new SimpleBooleanProperty();
        this.date = new SimpleObjectProperty<>(date);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
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