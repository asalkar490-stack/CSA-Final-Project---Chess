package Pieces;

public class Queen extends Piece {

    public Queen(String color, int row, int col){
        super(color, row, col);
    }

    public void move(int row, int col, Piece[][] board){

    }

    public boolean isLegal(int row, int col, Piece[][] boardPieces){
        return true;
    }
}
