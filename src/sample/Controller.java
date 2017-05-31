package sample;

import Draughts.Board;
import Draughts.Colors;
import Draughts.Vector;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Controller {
    private Board b;
    private Pane root;
    private boolean waitForPosition = false;
    private Board.Draught activeDraught;
    public Controller() {

    }
    @FXML
    private void initialize() {

    }

    public void link(Board board, Pane root) {
        b = board;
        this.root = root;
        b.draughts.forEach(d -> d.getCircle().setOnMouseClicked(draughtEvent));
        root.getChildren().forEach(N -> { if (N instanceof Rectangle) N.setOnMouseClicked(boardEvent); });


    }
    //Для шашки
    EventHandler draughtEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            waitForPosition = true;
            Circle c = (Circle) event.getSource();
            activeDraught = b.draughts.stream().filter(d -> d.getCircle() == c).findFirst().orElse(null);
        }
    };
    //Для клетки
    EventHandler boardEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (waitForPosition && activeDraught != null) {
                Rectangle r = (Rectangle) event.getSource();
                int x = (int)r.getX() / 80;
                int y = (int)r.getY() / 80;
                if (!activeDraught.isQueen()) {
                    if (activeDraught.move(new Vector(x, y))) {
                        reDraw();
                    }
                } else {
                    if (activeDraught.moveQueen(new Vector(x, y))) {
                        reDraw();
                    }
                }
            }
            waitForPosition = false;
            activeDraught = null;
        }
    };
    private void reDraw() {
        root.getChildren().forEach(N -> {
            if (N instanceof Circle) {
                Circle c = (Circle) N;
                Vector pos = new Vector(((int)c.getCenterX() - 40) / 80, ((int)c.getCenterY() - 40) / 80);
                Board.Draught draught = b.draughts.stream().filter(d -> d.getCircle() == c).findFirst().orElse(null);
                if (draught != null && !draught.getPos().equals(pos)) {
                    c.setCenterX(draught.getPos().x * 80 + 40);
                    c.setCenterY(draught.getPos().y * 80 + 40);
                }
            }
        });
        root.getChildren().removeIf(N -> {
            if (N instanceof Circle) {
                Circle c = (Circle) N;
                return b.draughts.stream().filter(d -> d.getCircle() == c).findFirst().orElse(null) == null;
            }
            return false;
        });
    }

}
