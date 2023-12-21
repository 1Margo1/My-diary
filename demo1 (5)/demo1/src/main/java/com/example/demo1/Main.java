package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class Main {
    @FXML
    private Button temporary_task;
    @FXML
    private Button short_task;
    @FXML
    private Button long_task;
    @FXML
    private Button my_diary;
    @FXML
    private Button back;
    @FXML
    private Button save;
    @FXML
    private Button delete;
    @FXML
    private TableView<About> table;
    @FXML
    private TextField text;
    @FXML
    private TableColumn<About, String> tableColumn;

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "Akaru@2003";

    private static ObservableList<About> aboutMe = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        tableColumn.setCellValueFactory(new PropertyValueFactory<>("information"));
        table.setItems(aboutMe);

        loadEventsForCurrentUser();
    }

    private void loadEventsForCurrentUser() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT information FROM about_me WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Login.getCurrentUser());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    aboutMe.clear();
                    while (resultSet.next()) {
                        String information = resultSet.getString("information");
                        aboutMe.add(new About(information));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setItems(aboutMe);
    }
    @FXML
    void handleTemporary(ActionEvent event) throws IOException {
        if (event.getSource() == temporary_task) {
            moveToTemporaryTask(temporary_task);
        }
    }

    @FXML
    void handleSave(ActionEvent event) {
        String termText = text.getText().trim();
        if (!termText.isEmpty()) {
            saveTermToDatabase(termText);
            aboutMe.add(new About(termText));
            text.clear();
        } else {
            showAlert("Error", "Information cannot be empty");
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        About selectedTerm = table.getSelectionModel().getSelectedItem();
        if (selectedTerm != null) {
            deleteTermFromDatabase(selectedTerm.getInformation());
            aboutMe.remove(selectedTerm);
        } else {
            showAlert("Error", "Select an information to delete");
        }
    }

    private void saveTermToDatabase(String information) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO about_me (user_id, information) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Login.getCurrentUser());
                statement.setString(2, information);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save information to the database");
        }
    }

    private void deleteTermFromDatabase(String information) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM about_me WHERE user_id = ? AND information = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Login.getCurrentUser());
                statement.setString(2, information);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete information from the database");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }








    private void moveToTemporaryTask(Button temporary_task) throws IOException {
        Stage stage = (Stage) temporary_task.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("TemporaryTask.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void handleShort(ActionEvent event) throws IOException {
        if (event.getSource() == short_task) {
            moveToShortTask(short_task);
        }
    }

    private void moveToShortTask(Button temporary_task) throws IOException {
        Stage stage = (Stage) temporary_task.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("ShortTermPlans.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void handleLong(ActionEvent event) throws IOException {
        if (event.getSource() == long_task) {
            moveToLongTask(long_task);
        }
    }

    private void moveToLongTask(Button long_task) throws IOException {
        Stage stage = (Stage) long_task.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("LongTermPlans.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
}
    @FXML
    void handleDiary(ActionEvent event) throws IOException {
        if (event.getSource() == my_diary) {
            moveToDiary(my_diary);
        }
    }

    private void moveToDiary(Button my_diary) throws IOException {
        Stage stage = (Stage) my_diary.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("MyDiary.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void handleLoginAction(ActionEvent event) throws IOException {
        if(event.getSource()==back){
            movePageToLogin(back);
        }
    }
    void movePageToLogin(Button click_to_login ) throws IOException {
        Stage stage = (Stage) click_to_login.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}