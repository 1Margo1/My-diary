package com.example.demo1;
import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;

public class About implements Serializable{
    private static final long serialVersionUID = 1L;

    private SimpleStringProperty information;

    public About( String information) {
        this.information = new SimpleStringProperty(information);
    }

    public String getInformation() {
        return information.get();
    }

    @Override
    public String toString() {
        return "Task: " + information;
    }
}