package Pieces;

public class Bishop extends Piece {
    
    public Bishop(String color, int row, int col, int value){
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

    public boolean isLegal(int row, int col, Piece[][] board){
        if (board[row][col] != null && board[row][col].getColor().equals(this.getColor()))
            return false;

        if (Math.abs(row - this.row) != Math.abs(col - this.col))
            return false;

        if (Math.abs(row - this.row) == Math.abs(col - this.col)){
            int checkCol, checkRow;

            //check bottom right
            if (row - this.row > 0 && col - this.col > 0){
                for (int i = 1; i < Math.abs(row - this.row); i++){
                    if (board[this.row + i][this.col + i] != null)
                        return false;
                }
            }

            //check bottom left
            if (row - this.row > 0 && col - this.col < 0){
                for (int i = 1; i < Math.abs(row - this.row); i++){
                    if (board[this.row + i][this.col - i] != null)
                        return false;
                }
            }

            //check upper right
            if (row - this.row < 0 && col - this.col > 0){
                for (int i = 1; i < Math.abs(row - this.row); i++){
                    if (board[this.row - i][this.col + i] != null)
                        return false;
                }
            }

            //check upper left
            if (row - this.row < 0 && col - this.col < 0){
                for (int i = 1; i < Math.abs(row - this.row); i++){
                    if (board[this.row - i][this.col - i] != null)
                        return false;
                }
            }
        }
        
        return true;
    }

    public int getValue(){
        return 3;
    }
}
