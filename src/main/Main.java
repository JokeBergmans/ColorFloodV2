package main;

import gui.MainScreenController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainScreenController msc = new MainScreenController();
        primaryStage.setScene(new Scene(msc));
        primaryStage.show();
        msc.openOptions();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
