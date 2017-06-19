package sample;

import Draughts.Board;
import Draughts.Colors;
import Draughts.TurnsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Pane root = loader.load();
        Controller controller = loader.getController();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle square = new Rectangle(i * 80, j * 80, 80, 80);
                square.setFill(((i + j) % 2 == 0) ? Paint.valueOf("#ffffff") : Paint.valueOf("#000000"));
                root.getChildren().add(square);
            }
        }
        Board board = new Board();
        board.draughts.forEach(d -> {
            Circle circle = new Circle(d.getPos().x * 80 + 40, d.getPos().y * 80 + 40, 30);
            circle.setFill((d.getColor() == Colors.WHITE) ? Paint.valueOf("#ffffff") : Paint.valueOf("#72321b"));
            d.bind(circle);
            root.getChildren().add(circle);

        });
        TurnsController turnsController = new TurnsController();
        controller.link(board, root, turnsController);
        primaryStage.setTitle("Russian draughts");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
