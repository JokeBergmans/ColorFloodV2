package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class OptionsController extends BorderPane {

    private MainScreenController msc;

    @FXML
    private Button btnEasy;
    @FXML
    private Button btnNormal;
    @FXML
    private Button btnHard;

    public OptionsController(MainScreenController msc) {
        FXMLLoader f = new FXMLLoader(getClass().getResource("OptionsScreen.fxml"));
        f.setRoot(this);
        f.setController(this);
        try {
            f.load();
        } catch (IOException e) {
            System.out.println(e);
        }
        this.msc = msc;
    }

    @FXML
    private void setEasy() {
        msc.setSize(10, 10);
        close();
    }

    @FXML
    private void setNormal() {
        msc.setSize(30, 20);
        close();
    }

    @FXML
    private void setHard() {
        msc.setSize(50, 30);
        close();
    }

    private void close() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }
}
