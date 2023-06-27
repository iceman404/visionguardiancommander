package com.project4.visionguardiancommander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class MainApplication extends Application {
        private Stage primaryStage;
        private LoginViewController loginController;
        private MainViewController mainController;

        @Override
        public void start(Stage primaryStage) throws Exception {
            this.primaryStage = primaryStage;
            showLoginView();
        }

        public void showLoginView() throws Exception {
            primaryStage.setTitle("Login");

            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
            GridPane gridPane = fxmlLoader.load();

            GridPane root = new GridPane();

            Image gif = new Image(getClass().getResourceAsStream("/assets/images/bh.gif"));
            ImageView imageView = new ImageView(gif);
            root.getChildren().add(imageView);

            root.getChildren().add(gridPane); // Add gridPane as a child of root

            Scene scene = new Scene(root, 1041, 585);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    public void showMainView() throws Exception {
        primaryStage.setTitle("Main View");

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/project4/visionguardiancommander/main-view.fxml"));
        AnchorPane mainPane = mainLoader.load();
        mainController = mainLoader.getController();

        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}