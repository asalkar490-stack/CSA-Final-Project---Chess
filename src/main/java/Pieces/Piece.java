package Pieces;

public abstract class Piece{

    public String color;
    public int row;
    public int col;
    boolean hasMoved;
    public Piece(String color, int row, int col){
        this.color = color;
        this.row = row;
        this.col = col;
        hasMoved = false;
    }

    public String getColor(){
        return color;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public void setRow(int row){
        this.row = row;
    }

    public void setCol(int col){
        this.col = col;
    }

    // public abstract void move(int row, int col, Piece[][] board);
    public abstract boolean isLegal(int row, int col, Piece[][] board);
    public abstract int getValue();
    public abstract String type();
    public void moved() {
        hasMoved = true;
    }
    public boolean hasMoved() {
        return hasMoved;
    }
}