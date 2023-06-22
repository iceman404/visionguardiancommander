package com.example.visionguardiancommander;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (validateLogin(username, password)) {
            showMainInterface();
        } else {
            showError("Invalid username or password");
        }
    }

    @FXML
    private void handleCancel() {
        // Close the application or perform any other necessary action
        usernameField.getScene().getWindow().hide();
    }

    private boolean validateLogin(String username, String password) {
        // Perform your login validation logic here
        // Return true if the login is successful, false otherwise
        // You can replace this with your own implementation
        return username.equals("admin") && password.equals("password");
    }

    private void showMainInterface() {
        // TODO: Implement your main interface logic
        // This method will be called when the login is successful
        // You can open a new window or replace the current scene with your main interface
        System.out.println("Login successful");
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
