package com.project4.visionguardiancommander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

            FXMLLoader loginLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
            GridPane gridPane = loginLoader.load();

            // Get the LoginViewController instance and set the mainApplication reference
            loginController = loginLoader.getController();
            loginController.setMainApplication(this);

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
            //primaryStage.setTitle("Main View");

            //BorderPane mainPane = (BorderPane)FXMLLoader.load(getClass().getResource("sample.fxml"));

           // FXMLLoader mainLoader = FXMLLoader.load(getClass().getResource("sample.fxml"));
            //BorderPane mainPane = mainLoader.load();
            //Scene scene = new Scene(mainPane,1350,720);

            // Get the LoginViewController instance and set the mainApplication reference
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
            Parent root = loader.load();
            SampleController sampleController = loader.getController();

            Scene scene = new Scene(root, 1350, 720);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/images/logo.png")));
            primaryStage.setTitle("Vision Guardian Commander");
            primaryStage.setScene(scene);
            primaryStage.show();

            /*
            FXMLLoader mainLoader = new FXMLLoader(MainApplication.class.getResource("sample.fxml"));
            Parent root = loader.load();
            SampleController sampleController = loader.getController();
            BorderPane mainPane = mainLoader.load();
            mainController = mainLoader.getController();

            Scene scene = new Scene(mainPane);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Initialize the main view controller
            mainController.initialize();

            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
           // primaryStage.getIcons().add(new Image("resources/assets/images/logo.png"));
*/
        }
    public static void main(String[] args) {

            launch(args);
    }
}