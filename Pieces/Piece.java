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

    public String getColor(String color){
        return color;
    }

    public int getRow(int row){
        return row;
    }

    public int getCol(int col){
        return col;
    }

    public void setRow(int row){
        this.row = row;
    }

    public void setCol(int col){
        this.col = col;
    }

    public int getValue(int value){
        return value;
    }

    public boolean isCapture(Piece[][] board, int row, int col){
        if (board[row][col] == null)
            return false;
        return true;
    }

    public abstract void move(int row, int col, Piece[][] board);
    public abstract boolean isLegal(int row, int col, Piece[][] board);
}