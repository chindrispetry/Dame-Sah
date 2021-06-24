package sample;

import java.util.Objects;

public class Pozitie implements Comparable<Pozitie>{
    private int x;
    private int y;
    private int prioritate;
    public Pozitie(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPrioritate() {
        return prioritate;
    }

    public void setPrioritate(int prioritate) {
        this.prioritate = prioritate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pozitie pozitie = (Pozitie) o;
        return x == pozitie.x && y == pozitie.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Pozitie{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int compareTo(Pozitie o) {
        if(x == o.getX() && y == o.getY() && prioritate == o.prioritate)
            return 0;
        if(x > o.getX() || y > getY() || prioritate > o.prioritate)
            return 1;
        return -1;
    }
}
