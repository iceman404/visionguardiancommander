package com.project.visionguardiancommander;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextField;

public class MainViewController {
    @FXML
    private Rectangle graphWindow;

    @FXML
    private TextField textField;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Rectangle webcamWindow;

    public void initialize() {
        // Perform any initialization logic for the main view

        // Example: Set styles for the rectangles
        graphWindow.setStyle("-fx-fill: lightblue;");
        webcamWindow.setStyle("-fx-fill: lightgreen;");
        //welcomeLabel.setText("Welcome to the Main View!");
    }

    @FXML
    private void handleButtonAction() {
        String text = textField.getText();
        System.out.println("Entered text: " + text);
    }
}