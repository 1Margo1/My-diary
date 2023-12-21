package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Sign_up {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button sign_up;
    @FXML
    private Button click_to_login;
    @FXML
    void handleLoginAction(ActionEvent event) throws IOException {
        if(event.getSource()==click_to_login){
            movePageToLogin(click_to_login);
        }
    }

    void movePageToLogin(Button click_to_login ) throws IOException {
        Stage stage = (Stage) click_to_login.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void handleSignUpAction(ActionEvent event) {
        if(event.getSource()==sign_up){
            try {
                checkData();
            } catch (Exception e) {
                notificationError(e.getMessage());
            }
        }

    }

    private void checkData() throws Exception {
        String user = username.getText();
        String passwordtext = password.getText();
        if (user.equals(passwordtext)) {
            throw new Exception("Password cannot be the same as the username");
        }
        BufferedReader reader = new BufferedReader(new FileReader("Information/user.txt"));
        String line;
        boolean usernameTaken = false;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Username: ")) {
                String storedUsername = line.substring("Username: ".length()).trim();
                if (storedUsername.equals(username)) {
                    usernameTaken = true;
                    break;
                }
            }
        }
        reader.close();

        if (usernameTaken) {
            throw new Exception("Username is already taken");
        } else {
            addData(user,passwordtext);
        }
    }


    void addData(String username, String password) throws IOException {
        try (Connection connection = DatabaseConnection.connect()) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration successful");
            alert.setContentText("Your account has been registered");
            alert.showAndWait();

            movePageToLogin(sign_up);
        } catch (SQLException e) {
            notificationError("Error connecting to the database");
            e.printStackTrace();
        }
    }
    void notificationError(String s){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setContentText(s);
        alert1.showAndWait();
    }
}