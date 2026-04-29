package Pieces;

public class King extends Piece {
    
    public King(String color, int row, int col){
        super(color, row, col);
    }

    public void move(int row, int col, Piece[][] board){

    }

    public boolean isLegal(int row, int col, Piece[][] boardPieces){
        return true;
    }

    public boolean checkMate(){
        return true;
    }

    public boolean check(){
        return true;
    }
}
