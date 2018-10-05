package gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainScreenController extends BorderPane {

    private FXMLLoader f;

    @FXML
    private GridPane field;
    @FXML
    private HBox hBoxBtns;
    @FXML
    private Button btnNewGame;
    @FXML
    private Button btnOptions;
    @FXML
    private Label lblScore;

    private IntegerProperty score;
    private int rows;
    private int cols;

    private Paint corner;

    public MainScreenController() {
        f = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        f.setRoot(this);
        f.setController(this);
        try {
            f.load();
        } catch (IOException e) {
            System.out.println(e);
        }

        score = new SimpleIntegerProperty();
        lblScore.textProperty().bind(score.asString());
        rows = 10;
        cols = 10;
        createField();
        startNewGame();
    }

    // flood fill algorithm
    // adds same colored neighbours if rectangle is target color
    private void fill(Paint target) {
        Stack<Rectangle> stack = new Stack<>();
        stack.push(getNodeByCoord(0, 0));
        while (!stack.isEmpty()) {
            Rectangle rectangle = stack.pop();
            int x = GridPane.getRowIndex(rectangle);
            int y = GridPane.getColumnIndex(rectangle);
            if (rectangle.getFill() == corner) {
                rectangle.setFill(target);
                if (x > 0) stack.push(getNodeByCoord(x - 1, y));
                if (y > 0) stack.push(getNodeByCoord(x, y - 1));
                if (x < rows - 1) stack.push(getNodeByCoord(x + 1, y));
                if (y < cols - 1) stack.push(getNodeByCoord(x, y + 1));
            }
        }
        corner = target;
    }

    // add rows & columns
    // fill field with rectangles
    private void createField() {
        for (int r = 0; r < rows; r++) {
            field.getRowConstraints().add(new RowConstraints());
            for (int c = 0; c < cols; c++) {
                field.getColumnConstraints().add(new ColumnConstraints());
                field.add(new Rectangle(10, 10), r, c);
            }
        }
    }

    // fill board with colors, reset values
    @FXML
    private void startNewGame() {
        generateColors();
        score.setValue(0);
        corner = getNodeByCoord(0, 0).getFill();
    }

    // set colors to random
    private void generateColors() {
        field.getChildren().stream()
                .map(c -> (Rectangle) c)
                .forEach(c -> c.setFill(getRandomColor()));
    }

    // returns random color
    private Paint getRandomColor() {
        return getColors()[new Random().nextInt(5)];
    }

    private Paint[] getColors() {
        return hBoxBtns.getChildren().stream()
                .map(c -> (Rectangle) c)
                .map(Rectangle::getFill)
                .toArray(Paint[]::new);
    }

    // won if no rectangle with different color is present
    private boolean won() {
        Stream<Rectangle> rectangles = field.getChildren().stream()
                .map(c -> (Rectangle) c);
        Optional<Rectangle> result = rectangles.filter(c -> c.getFill() != corner)
                .findAny();
        return !result.isPresent();
    }

    private void showWonScreen(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You won!");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("You won in " + score.getValue() + " attempts!");
        alert.setOnCloseRequest(event -> startNewGame());
        alert.showAndWait();
    }

    // find first rectangle with coords
    private Rectangle getNodeByCoord(int row, int col) {
        Optional<Node> result = field.getChildren().stream()
                .filter(c -> GridPane.getRowIndex(c) == row && GridPane.getColumnIndex(c) == col)
                .findFirst();
        return (Rectangle) result.orElse(null);
    }

    // call fill with pressed color as target
    @FXML
    private void changeColor(MouseEvent ae) {
        Rectangle source = (Rectangle) ae.getSource();
        score.setValue(score.getValue() + 1);
        fill(source.getFill());
        if (won()) {
            showWonScreen();
        }
    }

    @FXML
    public void openOptions() {
        Stage stage = (Stage) getScene().getWindow();
        Stage newStage = new Stage();
        newStage.initOwner(stage);
        Scene scene = new Scene(new OptionsController(this));
        newStage.setScene(scene);
        newStage.show();
    }

    public void changeTheme(Paint[] colors) {
        Rectangle[] rectangles = hBoxBtns.getChildren().stream()
                .map(c -> (Rectangle) c)
                .toArray(Rectangle[]::new);
        IntStream.range(0, 5)
                .forEach(i -> rectangles[i].setFill(colors[i]));
    }

    public void setSize(int r, int c) {
        rows = r;
        cols = c;
        emptyField();
        createField();
        startNewGame();
    }

    // reload to remove all rows & columns
    private void emptyField() {
        try {
            f.load();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
