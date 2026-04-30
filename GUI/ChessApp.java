package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ChessApp extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("♟ JavaFX is working!");
        label.setStyle("-fx-font-size: 24px;");
        Scene scene = new Scene(new StackPane(label), 400, 300);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
