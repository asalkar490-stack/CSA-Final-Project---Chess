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

    public boolean canPromote(){
        return false;
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

    public boolean hasNoLegal(int row, int col, Piece[][] board){
        boolean notLegal = true;
        for (int r = 0; r < board.length; r++){
            for (int c = 0; c < board[0].length; c++){
                if (board[r][c].isLegal(row, col, board))
                    notLegal = false;
            }
        }
        return notLegal;
    }

    // public abstract void move(int row, int col, Piece[][] board);
    public abstract boolean isLegal(int row, int col, Piece[][] board);
    public abstract int getValue();
    public abstract String getType();
    public void moved() {
        hasMoved = true;
    }
    public boolean hasMoved() {
        return hasMoved;
    }
}