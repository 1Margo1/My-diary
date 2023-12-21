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

import java.io.IOException;
import java.sql.*;

public class Login {
    @FXML
    private Button Login_id;
    @FXML
    private Button click_to_sign_up;
    @FXML
    private TextField username_id;
    @FXML
    private PasswordField password_id;

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Akaru@2003";

    private static int currentUser;

    public class LoginController {
        private static final String LOGIN_FXML_PATH = "Login.fxml";

        private PageLoader pageLoader;

        public LoginController(PageLoader pageLoader) {
            this.pageLoader = pageLoader;
        }


        public LoginController createLoginInstance() {
            try {
                Parent root = pageLoader.loadPage(LOGIN_FXML_PATH);
                LoginController login = new LoginController(pageLoader);
                return login;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @FXML
    void handleSignUpButton(ActionEvent event) throws IOException {
        if (event.getSource() == click_to_sign_up) {
            moveToSignUpPage(click_to_sign_up);
        }
    }

    public static int getCurrentUser() {
        return currentUser;
    }

    private void moveToSignUpPage(Button click_to_sign_up) throws IOException {
        Stage stage = (Stage) click_to_sign_up.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Signup.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void moveToMainUpPage(Button click_to_sign_up) throws IOException {
        Stage stage = (Stage) click_to_sign_up.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void login() {
        String username = username_id.getText();
        String password = password_id.getText();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT user_id FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        currentUser = resultSet.getInt("user_id");

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Login successful");
                        alert.setContentText("Your account has been authorized");
                        alert.show();

                        moveToMainUpPage(Login_id);
                    } else {
                        notificationError("Invalid username or password");
                    }
                }
            }
        } catch (SQLException e) {
            notificationError(e.getMessage());
        } catch (Exception e) {
            notificationError("Login failed");
        }
    }

    @FXML
    void handleLoginButton(ActionEvent event) {
        if (event.getSource() == Login_id) {
            try {
                LoginController controller = new LoginController(new FXMLPageLoader());
                controller.createLoginInstance();
                login();
            } catch (Exception e) {
                notificationError(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    void notificationError(String s) {
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setContentText(s);
        alert1.showAndWait();
    }
}
