package Draughts;

public class Vector {
    public int x;
    public int y;

    public Vector(int i, int j) {
        this.x = i;
        this.y = j;
    }
    public boolean check() {
        return (this.x > -1 && this.x < 8) && (this.y > -1 && this.y < 8);
    }
    @Override
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) return false;
        Vector vector = (Vector) other;
        return this.x == vector.x && this.y == vector.y;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }
}
