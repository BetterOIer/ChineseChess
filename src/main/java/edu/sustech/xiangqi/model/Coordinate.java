package edu.sustech.xiangqi.model;

public class Coordinate {
    private int row;
    private int col;
    public Coordinate(int row, int col){
        this.row = row;
        this.col = col;
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate c = (Coordinate) o;
        return row == c.row && col == c.col;
    }
    @Override
    public String toString() {
        return "("+row+","+col+")";
    }
}
