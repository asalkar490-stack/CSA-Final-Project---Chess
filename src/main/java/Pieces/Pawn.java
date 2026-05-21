package Pieces;

public class Pawn extends Piece {

    private boolean hasMoved;

    public Pawn(String color, int row, int col){
        super(color, row, col);
        hasMoved = false;
    }

    public boolean isLegal(int row, int col, Piece[][] board){
        if(!hasMoved() && this.getColor().equals("White") && row > this.row && row - this.row <= 2 && this.col == col && board[row][col] == null && board[this.row + 1][this.col] == null)
            return true;

        else if (!hasMoved() && this.getColor().equals("Black") && row < this.row && row - this.row >= -2 && this.col == col && board[row][col] == null && board[this.row - 1][this.col] == null)
            return true;

        else if (this.hasMoved() && this.getColor().equals("White") && row - this.row == 1 && this.col == col && board[row][col] == null)
            return true;

        else if (this.hasMoved() && this.getColor().equals("Black") && row - this.row == -1 && this.col == col && board[row][col] == null)
            return true;

        else if (this.getColor().equals("White") && row - this.row == 1 && Math.abs(this.col - col) == 1 && board[row][col] != null && !board[row][col].getColor().equals(this.getColor()))
            return true;

        else if (this.getColor().equals("Black") && row - this.row == -1 && Math.abs(this.col - col) == 1 && board[row][col] != null && !board[row][col].getColor().equals(this.getColor()))
            return true;

        return false;
    }

    public String getType(){
        return "Pawn";
    }

    public boolean canPromote(String color){
        if (color.equals("White") && this.row == 0)
            return true;
        if (color.equals("Black") && this.row == 7)
            return true;
        return false;
    }

    public void Promote(Piece[][] board, Piece newPiece){
        board[this.row][this.col] = newPiece;
    }

    public int getValue(){
        return 1;
    }

    public boolean hasMoved(){
        return hasMoved;
    }
}
