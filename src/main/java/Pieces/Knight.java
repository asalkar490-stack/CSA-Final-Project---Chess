package Pieces;

public class Knight extends Piece {
    
    public Knight(String color, int row, int col){
        super(color, row, col);
    }

    //useless, only added cause I have it as a abstract method
    public boolean hasMoved(){
        return true;
    }

    public boolean isLegal(int row, int col, Piece[][] board){
        if (Math.abs(row-this.row) == 2 && Math.abs(col-this.col) == 1 || Math.abs(row-this.row) == 1 && Math.abs(col-this.col) == 2) {
            if (board[row][col] != null && board[row][col].getColor().equals(this.getColor())) 
                return false;
            return true;
        }
        return false;
    }

    public String getType(){
        return "Knight";
    }

    public int getValue(){
        return 3;
    }
}
