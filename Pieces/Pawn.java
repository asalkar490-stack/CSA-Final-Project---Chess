package Pieces;

public class Pawn extends Piece {
    
    public Pawn(String color, int row, int col){
        super(color, row, col);
    }

    public void move(int row, int col, Piece[][] board){

    }

    public boolean isLegal(int row, int col, Piece[][] boardPieces){
        return true;
    }
}
