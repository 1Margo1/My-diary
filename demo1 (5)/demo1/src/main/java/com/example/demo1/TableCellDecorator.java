package com.example.demo1;

import javafx.scene.control.TableCell;

public interface TableCellDecorator<S, T> {
    void decorate(TableCell<S, T> cell);
}
//decorator