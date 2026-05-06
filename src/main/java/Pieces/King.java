package Pieces;

import javafx.beans.property.FloatPropertyBase;

public class King extends Piece {
    
    private boolean hasMoved;

    public King(String color, int row, int col, int value){
        super(color, row, col, value);
        hasMoved = false;
    }

    public void move(int row, int col, Piece[][] board){
        if (isLegal(row, col, board)){
            board[row][col] = this;
            board[this.row][this.col] = null;
            this.row = row;
            this.col = col;
        }
    }

    public boolean isLegal(int row, int col, Piece[][] board){

        if (Math.abs(row - this.row) <= 1 && Math.abs(col - this.col) <= 1)
            return true;

        else if (canCastle(row, col, board))//FIX THIS
            return true;
        return false;
    }

    public boolean checkMate(){
        return true;
    }

    public boolean check(){
        return true;
    }

    public void castle(){

    }

    public boolean hasMoved(){
        return hasMoved;   
    }

    

    public boolean canCastle(int row, int col, Piece[][] board){
        if (hasMoved())return false;
        if (check()) return false;

        //check white side
        if (this.getColor().equals("white")){
            if (row == 0 && col == 1 && board[row][col] != null && board[row][col].type().equals("Rook") && !board[row][col].hasMoved())
                return true;
            if (row == 0 && col == 7 && board[row][col] != null && board[row][col].type().equals("Rook") && !board[row][col].hasMoved())
                return true;
        }

        //check left side
        if (this.getColor().equals("black")){
            if (row == 7 && col == 0 && board[row][col] != null && board[row][col].type().equals("Rook") && !board[row][col].hasMoved())
                return true;
            if (row == 7 && col == 7 && board[row][col] != null && board[row][col].type().equals("Rook") && !board[row][col].hasMoved())
                return true;
        }
        return false;
    }

    public String type(){
        return "King";
    }

    public int getValue(){
        return 0;
    }
}
