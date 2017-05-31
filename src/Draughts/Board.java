package Draughts;

import javafx.scene.paint.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import static java.lang.Math.*;

public class Board {
    public Colors[][] board = new Colors[8][8];
    public List<Draught> draughts = new ArrayList<>();
    public Board() {
        Colors black = Colors.BLACK;
        Colors white = Colors.WHITE;
        Colors brown = Colors.BROWN;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j] = white;
                } else {
                    board[i][j] = black;
                }
            }
        }
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 8; i++) {
                if (board[i][j] == black)
                    draughts.add(new Draught(brown, new Vector(i, j)));
            }
        }
        for (int j = 5; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (board[i][j] == black)
                    draughts.add(new Draught(white, new Vector(i, j)));
            }
        }
    }
    public boolean isEmpty(Vector position) {
        return draughts.stream().filter(draught -> draught.position.equals(position)).findFirst().orElse(null) == null;
    }
    public Draught getDraught(Vector position) {
        return draughts.stream().filter(draught -> draught.position.equals(position)).findFirst().orElse(null);
    }
    public class Draught {
        private Colors color;
        private Vector position;
        private boolean isQueen;
        private int lastHorizontal;
        private Circle circle;
        private Draught(Colors colors, Vector pos) {
            this.color = colors;
            this.position = pos;
            lastHorizontal = (colors == Colors.BROWN) ? 7 : 0;
        }

        public void bind(Circle circle) {
            this.circle = circle;
        }

        public boolean move(Vector newPos) {
            if (!newPos.check()) return false;
            if (abs(newPos.x - this.position.x) == 1 && (abs(newPos.y - this.position.y) == 1)) {
                int delta = newPos.y - this.position.y;
                if ((this.color == Colors.BROWN && delta > 0 ) || (this.color == Colors.WHITE && delta < 0))
                    if ((newPos.x + newPos.y) % 2 != 0 && isEmpty(newPos)) {
                        this.position = newPos;
                        checkQueen();
                        return true;
                    }
            }
            if (abs(newPos.x - this.position.x) == 2 && (abs(newPos.y - this.position.y) == 2)) {
                if ((newPos.x + newPos.y) % 2 != 0 && isEmpty(newPos)) {
                    Vector between = new Vector((newPos.x + this.position.x) / 2, (newPos.y + this.position.y) / 2);
                    if (!isEmpty(between) && this.color != getDraught(between).color) {
                        this.position = newPos;
                        draughts.remove(getDraught(between));
                        checkQueen();
                        return true;
                    }
                }
            }
            return false;
        }
        private void checkQueen() {
            this.isQueen = (this.position.y == lastHorizontal);
            if (isQueen())
                circle.setFill((this.color == Colors.WHITE) ? Paint.valueOf("#dddddd") : Paint.valueOf("#99471e"));
        }
        public boolean moveQueen(Vector newPos) {
            if ((newPos.x + newPos.y) % 2 != 0 && isEmpty(newPos)) {
                if (abs(newPos.x - this.position.x) == abs(newPos.y - this.position.y)) {
                    int delta = abs(newPos.x - this.position.x);
                    int xShift = (newPos.x - this.position.x) / delta;
                    int yShift = (newPos.y - this.position.y) / delta;
                    boolean isPreviousNotEmpty = false;
                    List<Draught> kicked = new ArrayList<>();
                    for (int i = 1; i < delta; i++) {
                        Vector subPosition = new Vector(this.position.x + xShift * i, this.position.y + yShift * i);
                        if (!isEmpty(subPosition)) {
                            if (isPreviousNotEmpty) return false;
                            if (getDraught(subPosition).color == this.color) return false;
                            kicked.add(getDraught(subPosition));
                            isPreviousNotEmpty = true;
                        } else {
                            isPreviousNotEmpty = false;
                        }
                    }
                    this.position = newPos;
                    kicked.forEach(d -> draughts.remove(d));
                    return true;
                }
            }
            return false;
        }
        public boolean isQueen() {
            return isQueen;
        }
        public Circle getCircle() {
            return this.circle;
        }
        public Vector getPos() {
            return position;
        }
        public Colors getColor() {
            return this.color;
        }
    }
    public static void main(String[] args) {
        Board b = new Board();
        System.out.println(b);
        System.out.println(b.getDraught(new Vector(0,5)).move(new Vector(1,4)));
        System.out.println(b);
        System.out.println(b.getDraught(new Vector(3, 2)).move(new Vector(2, 3)));
        System.out.println(b);
        System.out.println(b.getDraught(new Vector(1, 4)).move(new Vector(3, 2)));
        System.out.println(b);
    }
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                String str1 = "X ";
                Vector pos = new Vector(i,j);
                if (!isEmpty(pos)) {
                    str1 = (getDraught(pos).color == Colors.BROWN) ? "B " : "W ";
                }
                str.append(str1);
            }
            str.append("\r\n");
        } return str.toString();
    }
}
