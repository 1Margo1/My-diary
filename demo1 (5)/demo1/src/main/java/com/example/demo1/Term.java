package com.example.demo1;

import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Term implements Serializable {
    private static final long serialVersionUID = 1L;
    private SimpleStringProperty term;
    private List<TermObserver> observers = new ArrayList<>();

    public Term(String term) {
        this.term = new SimpleStringProperty(term);
    }

    public String getTerm() {
        return term.get();
    }

    public SimpleStringProperty termProperty() {
        return term;
    }

    public void addObserver(TermObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TermObserver observer) {
        observers.remove(observer);
    }

    public void notifyTermAdded() {
        for (TermObserver observer : observers) {
            observer.onTermAdded(this);
        }
    }

    public void notifyTermDeleted() {
        for (TermObserver observer : observers) {
            observer.onTermDeleted(this);
        }
    }

    @Override
    public String toString() {
        return "Task: " + term;
    }
}
