package com.example.demo1;

import javafx.scene.control.TableCell;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BoldTextDecorator<S, T> implements TableCellDecorator<S, T> {
    @Override
    public void decorate(TableCell<S, T> cell) {
        cell.setFont(Font.font("System", FontWeight.BOLD, 12));
    }
}
//decorator
