package Draughts;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TurnsController {
    private Colors currentPlayer = Colors.WHITE;
    private int scoreWhite = 0;
    private int scoreBrown = 0;
    //Default time fot each turn
    private long timeForTurn = 60000;
    private Timer timer = new Timer(true);
    private TimerTask task = getTask();
    public TurnsController() {
        timer.schedule(task, timeForTurn, timeForTurn);
    }

    public void changeTurn() {
        forceStart();
        changePlayer();
    }
    private void changePlayer() {
        currentPlayer = (currentPlayer == Colors.WHITE) ? Colors.BROWN : Colors.WHITE;
    }
    private void forceStart() {
        task.cancel();
        task = getTask();
        timer.purge();
        timer.schedule(task, timeForTurn, timeForTurn);
    }
    public Colors getCurrentPlayer() {
        return currentPlayer;
    }
    public void applyScore(int score) {
        if (currentPlayer == Colors.BROWN) {
            scoreBrown += score;
        } else {
            scoreWhite += score;
        }
    }
    public String getScore() {
        return scoreWhite + ":" + scoreBrown;
    }
    private TimerTask getTask() {
        return new TimerTask() {
            @Override
            public void run() {
                changePlayer();
                System.out.println(currentPlayer + "/" + new Date());

            }
        };
    }

}
