package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ChessApp extends Application {

    private static final int TILE_SIZE = 80;
    private static final Color LIGHT = Color.web("#F0D9B5");
    private static final Color DARK  = Color.web("#B58863");

    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill((row + col) % 2 == 0 ? LIGHT : DARK);
                grid.add(tile, col, row);
            }
        }

        Scene scene = new Scene(grid, 640, 640);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}