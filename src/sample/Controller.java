package sample;

import Draughts.Board;
import Draughts.Colors;
import Draughts.TurnsController;
import Draughts.Vector;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Controller {
    private Board b;
    private Pane root;
    private boolean waitForPosition = false;
    private Board.Draught activeDraught;
    private TurnsController controller;
    @FXML
    private Label Header;
    @FXML
    private Label Score;
    public Controller() {

    }
    @FXML
    private void initialize() {
        Header.setText("Ходят\r\nбелые");
        Thread updater = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("That's incredible!");
                    }
                    Platform.runLater(() -> updateHeader());
                }
            }
        };
        updater.setDaemon(true);
        updater.start();
    }

    private void updateHeader() {
        Header.setText((controller.getCurrentPlayer() == Colors.WHITE) ? "Ходят\r\nбелые" : "Ходят коричневые");
    }

    public void link(Board board, Pane root, TurnsController turns) {
        b = board;
        this.root = root;
        controller = turns;
        b.draughts.forEach(d -> d.getCircle().setOnMouseClicked(draughtEvent));
        root.getChildren().forEach(N -> { if (N instanceof Rectangle) N.setOnMouseClicked(boardEvent); });


    }
    //Для шашки
    EventHandler draughtEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Circle c = (Circle) event.getSource();
            Board.Draught draught = b.draughts.stream().filter(d -> d.getCircle() == c).findFirst().orElse(null);
            if (draught != null && draught.getColor() == controller.getCurrentPlayer()) {
                waitForPosition = true;
                activeDraught = draught;
            }
        }
    };
    //Для клетки
    EventHandler boardEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (waitForPosition && activeDraught != null && activeDraught.getColor() == controller.getCurrentPlayer()) {
                Rectangle r = (Rectangle) event.getSource();
                int x = (int)r.getX() / 80;
                int y = (int)r.getY() / 80;
                if (!activeDraught.isQueen()) {
                    if (activeDraught.move(new Vector(x, y))) {
                        reDraw();
                        controller.changeTurn();
                        updateHeader();
                        Score.setText(controller.getScore());
                    }
                } else {
                    if (activeDraught.moveQueen(new Vector(x, y))) {
                        reDraw();
                        controller.changeTurn();
                        updateHeader();
                        Score.setText(controller.getScore());
                    }
                }

            }
            waitForPosition = false;
            activeDraught = null;
        }
    };
    private void reDraw() {
        int score = (int)root.getChildren().stream().filter(node -> {
            if (node instanceof Circle) {
                Circle c = (Circle) node;
                return b.draughts.stream().filter(d -> d.getCircle() == c).findFirst().orElse(null) == null;
            }
            return false;
        }).count();
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
        controller.applyScore(score);
    }

}
