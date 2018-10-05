package main;

import gui.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(new MainScreenController()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
