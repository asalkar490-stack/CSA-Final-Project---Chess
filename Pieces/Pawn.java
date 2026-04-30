package Pieces;

public class Pawn extends Piece {

    private boolean hasMoved;

    public Pawn(String color, int row, int col, int value){
        super(color, row, col, value);
        hasMoved = false;
    }

    public void move(int row, int col, Piece[][] board){
        if (isLegal(row, col, board)){
           board[row][col] = this;
           board[this.row][this.col] = null;
           this.row = row;
           this.col = col;
           hasMoved = true;
        }
    }

    public boolean isLegal(int row, int col, Piece[][] board){
        if(!hasMoved() && this.getColor().equals("white") && row > this.row && row - this.row <= 2 && this.col == col)
            return true;
        else if (!hasMoved() && this.getColor().equals("black") && row < this.row && row - this.row >= -2 && this.col == col)
            return true;
        else if (this.hasMoved() && this.getColor().equals("white") && row - this.row == 1 && this.col == col)
            return true;
        else if (this.hasMoved() && this.getColor().equals("black") && row - this.row == -1 && this.col == col)
            return true;
        else if (this.getColor().equals("white") && row - this.row == 1 && row > this.row && Math.abs(this.col - col) == 1 && board[row][col] != null && !board[row][col].getColor().equals(this.getColor()))
            return true;
        else if (this.getColor().equals("black") && row - this.row == -1 && row < this.row && Math.abs(this.col - col) == 1 && board[row][col] != null && !board[row][col].getColor().equals(this.getColor()))
            return true;
        return false;
    }

    public boolean canPromote(String color, int col){
        if (color.equals("white") && row == 7)
            return true;
        if (color.equals("black") && row == 0)
            return true;
        return false;
    }

    public void Promote(){
        
    }

    public int getValue(){
        return 1;
    }

    public boolean hasMoved(){
        return hasMoved;
    }
}
