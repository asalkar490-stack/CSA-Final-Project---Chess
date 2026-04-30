package Pieces;

public class King extends Piece {
    
    private boolean hasMoved;

    public King(String color, int row, int col, int value){
        super(color, row, col, value);
        hasMoved = false;
    }

    public void move(int row, int col, Piece[][] board){
        if (isLegal(row, col, board)){
           super.row = row;
           super.col = col;
        }
    }

    public boolean isLegal(int row, int col, Piece[][] boardPieces){

        if (Math.abs(row - this.row) <= 1 && Math.abs(col - this.col) <= 1)
            return true;

        else if (canCastle())//FIX THIS
            return true;
        return false;
    }

    public boolean checkMate(){
        return true;
    }

    public boolean check(){
        return true;
    }

    public void castle(){

    }

    public boolean hasMoved(){
        return hasMoved;
    }

    public boolean canCastle(){
        if (hasMoved())
            return false;
        return true;
    }

    public int getValue(){
        return 0;
    }
}
