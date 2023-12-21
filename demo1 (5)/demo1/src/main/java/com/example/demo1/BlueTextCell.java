package com.example.demo1;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class BlueTextCell<S, T> extends TableCell<S, T> {
    private TableCellDecorator<S, T> decorator;

    public BlueTextCell(TableCellDecorator<S, T> decorator) {
        this.decorator = decorator;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setTextFill(Color.BLACK);
        } else {
            setText(item.toString());
            setTextFill(Color.BLUE);
        }

        if (decorator != null) {
            decorator.decorate(this);
        }
    }
}
//decorator
