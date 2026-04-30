package Pieces;

public class Queen extends Piece {

    public Queen(String color, int row, int col, int value){
        super(color, row, col, value);
    }

    public void move(int row, int col, Piece[][] board){
        if (isLegal(row, col, board)){
           super.row = row;
           super.col = col; 
        }
    }

    public boolean isLegal(int row, int col, Piece[][] boardPieces){
        return true;
    }
}
