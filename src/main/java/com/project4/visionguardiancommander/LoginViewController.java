package com.project4.visionguardiancommander;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginViewController {
    private MainApplication mainApplication;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    public void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

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
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (validateLogin(username, password)) {
                showMainInterface();
            } else {
                showError("Invalid username or password");
            }
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
        return username.equals("admin") && password.equals("admin");
    }

    private void showMainInterface() {
        try {
            mainApplication.showMainView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
