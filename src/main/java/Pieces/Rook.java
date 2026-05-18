package Pieces;

public class Rook extends Piece  {

    private boolean hasMoved;

    public Rook(String color, int row, int col){
        super(color, row, col);
        hasMoved = false;
    }

    public String type(){
        return "Rook";
    }

    public boolean isLegal(int row, int col, Piece[][] board){
        if (row != this.row && col != this.col)
            return false;
        
        if (row == this.row) {
            int minCol = Math.min(col, this.col);
            int maxCol = Math.max(col, this.col);
            for (int c = minCol + 1; c < maxCol; c++) {
                if (board[row][c] != null) return false;
            }
        }

        if (col == this.col) {
            int minRow = Math.min(row, this.row);
            int maxRow = Math.max(row, this.row);
            for (int r = minRow + 1; r < maxRow; r++) {
                if (board[r][col] != null) return false;
            }
        }

        if (board[row][col] != null && board[row][col].getColor().equals(this.getColor()))
            return false;

        return true;
    }

    public boolean hasMoved(){
        return hasMoved;
    }

    public int getValue(){
        return 5;
    }
}
