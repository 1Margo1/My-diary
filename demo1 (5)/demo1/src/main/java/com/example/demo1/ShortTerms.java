package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class ShortTerms {
    @FXML
    private Button add;
    @FXML
    private Button back;
    @FXML
    private Button delete;
    @FXML
    private TextField text;
    @FXML
    private TableView<Term> table;
    @FXML
    private TableColumn<Term, String> termColumn;

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "Akaru@2003";
    private ObservableList<Term> termList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        termColumn.setCellValueFactory(new PropertyValueFactory<>("term"));
        table.setItems(termList);
        loadEventsForCurrentUser();
    }

    private void loadEventsForCurrentUser() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT term FROM short_terms WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Login.getCurrentUser());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    termList.clear();
                    while (resultSet.next()) {
                        String termColumn = resultSet.getString("term");
                        termList.add(new Term(termColumn));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setItems(termList);
    }

    @FXML
    void handleAdd(ActionEvent event) {
        String termText = text.getText().trim();
        if (!termText.isEmpty()) {
            saveTermToDatabase(termText);
            termList.add(new Term(termText));
            text.clear();
        } else {
            showAlert("Error", "The field cannot be empty");
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        Term selectedTerm = table.getSelectionModel().getSelectedItem();
        if (selectedTerm != null) {
            deleteTermFromDatabase(selectedTerm.getTerm());
            termList.remove(selectedTerm);
        } else {
            showAlert("Error", "Select a data to delete");
        }
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

    private void saveTermToDatabase(String term) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO short_terms (user_id, term) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Login.getCurrentUser());
                statement.setString(2, term);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save to the database");
        }
    }

    private void deleteTermFromDatabase(String term) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM short_terms WHERE user_id = ? AND term = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Login.getCurrentUser());
                statement.setString(2, term);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete from the database");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}