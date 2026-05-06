package Pieces;

public abstract class Piece{

    public String color;
    public int row;
    public int col;
    public int value;

    public Piece(String color, int row, int col, int value){
        this.color = color;
        this.row = row;
        this.col = col;
        this.value = value;
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

    public abstract void move(int row, int col, Piece[][] board);
    public abstract boolean isLegal(int row, int col, Piece[][] board);
    public abstract int getValue();
    public abstract String type();
    public abstract boolean hasMoved();
}