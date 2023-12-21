package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Date;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class MyDiary {
    @FXML
    private Button back;
    @FXML
    private Button add;
    @FXML
    private TextField text;
    @FXML
    private DatePicker date;
    @FXML
    private TableView<LocalEvent> tableView;
    @FXML
    private TableColumn<LocalEvent, LocalDate> dateColumn;
    @FXML
    private TableColumn<LocalEvent, String> descriptionColumn;

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Akaru@2003";


    private ObservableList<LocalEvent> eventsList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        BoldTextDecorator<LocalEvent, String> boldDecorator = new BoldTextDecorator<>();
        descriptionColumn.setCellFactory(column -> {
            BlueTextCell<LocalEvent, String> blueCell = new BlueTextCell<>(boldDecorator);
            return blueCell;
        });

        loadEventsForCurrentUser();
    }

    private void loadEventsForCurrentUser() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT event_date, event_description FROM my_diary WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Login.getCurrentUser());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    eventsList.clear();
                    while (resultSet.next()) {
                        LocalDate eventDate = resultSet.getDate("event_date").toLocalDate();
                        String eventDescription = resultSet.getString("event_description");
                        eventsList.add(new LocalEvent(eventDate, eventDescription));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(eventsList);
    }

    @FXML
    void handleMain(ActionEvent event) throws IOException {
        if (event.getSource() == back) {
            moveToMain(back);
        }
    }

    private void moveToMain(Button main) throws IOException {
        Stage stage = (Stage) main.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleAdd(ActionEvent event) {
        String eventDescription = text.getText();
        LocalDate eventDate = date.getValue();

        if (eventDescription != null && eventDate != null) {
            addEventToDatabase(eventDate, eventDescription);
            loadEventsForCurrentUser();
        }
    }

    private void addEventToDatabase(LocalDate eventDate, String eventDescription) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO my_diary (user_id, event_date, event_description) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Login.getCurrentUser());
                preparedStatement.setDate(2, Date.valueOf(eventDate));
                preparedStatement.setString(3, eventDescription);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}