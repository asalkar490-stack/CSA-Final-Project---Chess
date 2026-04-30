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

    public boolean isLegal(int row, int col, Piece[][] boardPieces){
        return true;
    }

    public boolean promotion(String color, int col){
        if (color.equals("white") && row == 7)
            return true;
        if (color.equals("black") && row == 0)
            return true;
        return false;
    }

    public int getValue(){
        return 1;
    }

    public boolean hasMoved(boolean hasMoved){
        return hasMoved;
    }
}
