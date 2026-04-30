package Pieces;

public class Knight extends Piece {
    
    public Knight(String color, int row, int col, int value){
        super(color, row, col, value);
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
        if (Math.abs(row-this.row) == 2 && Math.abs(col-this.col) == 1 || Math.abs(row-this.row) == 1 && Math.abs(col-this.col) == 2) {
            if (board[row][col] != null && board[row][col].getColor().equals(this.getColor())) 
                return false;
            return true;
        }
        return false;
    }

    public int getValue(){
        return value;
    }
}
