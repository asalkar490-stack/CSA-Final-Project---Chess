package Pieces;

public class Knight extends Piece {
    
    public Knight(String color, int row, int col){
        super(color, row, col);
    }

    public void move(int row, int col, Piece[][] board){

    }

    public boolean isLegal(int row, int col, Piece[][] boardPieces){
        return true;
    }
}
