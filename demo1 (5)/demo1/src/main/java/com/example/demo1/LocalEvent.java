package com.example.demo1;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;
import java.time.LocalDate;

public class LocalEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private SimpleObjectProperty<LocalDate> date;
    private SimpleStringProperty description;

    public LocalEvent(LocalDate date, String description) {
        this.date = new SimpleObjectProperty<>(date);
        this.description = new SimpleStringProperty(description);
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    @Override
    public String toString() {
        return "Date: " + date + " --- " + description;
    }
}