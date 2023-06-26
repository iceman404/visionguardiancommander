package com.example.visionguardiancommander;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginApplication extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Login");

        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
        GridPane gridPane = fxmlLoader.load();

        GridPane root = new GridPane();

        Image gif = new Image(getClass().getResourceAsStream("/assets/images/bh.gif"));
        ImageView imageView = new ImageView(gif);
        root.getChildren().add(imageView);

        root.getChildren().add(gridPane); // Add gridPane as a child of root

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}