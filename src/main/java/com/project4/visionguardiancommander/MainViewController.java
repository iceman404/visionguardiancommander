package com.project4.visionguardiancommander;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class MainViewController {
    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome to the Main View!");
    }
}
