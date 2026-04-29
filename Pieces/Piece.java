package Pieces;

public abstract class Piece{

    private String color;
    private int row;
    private int col;

    public Piece(String color, int row, int col){
        this.color = color;
        this.row = row;
        this.col = col;
    }

    public String getColor(String color){
        return color;
    }

    public abstract void move(int row, int col, Piece[][] board);
    public abstract boolean isLegal(int row, int col, Piece[][] board);
}