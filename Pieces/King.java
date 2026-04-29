package Pieces;

public class King extends Piece {
    
    public King(String color, int row, int col, int value){
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

    public boolean checkMate(){
        return true;
    }

    public boolean check(){
        return true;
    }

    public void castle(){

    }

    public boolean canCastle(){
        return true;
    }
}
