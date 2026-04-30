package Pieces;

public class Rook extends Piece  {

    public Rook(String color, int row, int col, int value){
        super(color, row, col, value);
    }

    public void move(int row, int col, Piece[][] board){
        if (isLegal(row, col, board)){
            board[row][col] = this;
            board[this.row][this.col] = null;
            this.row = row;
            this.col = col;
        }
    }

    public boolean isLegal(int row, int col, Piece[][] boardPieces){
        return true;
    }

    public int getValue(){
        return 5;
    }
}
