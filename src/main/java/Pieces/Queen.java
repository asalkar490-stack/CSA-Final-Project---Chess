package Pieces;

public class Queen extends Piece {

    public Queen(String color, int row, int col, int value){
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

        // Check if it moves like a rook
        if (row == this.row || col == this.col){
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

            return true;
        }

        // check if it moves like a bishop
        if (Math.abs(row - this.row) == Math.abs(col - this.col)){
            // check bottom right
            if (row - this.row > 0 && col - this.col > 0){
                for (int i = 1; i < Math.abs(row - this.row); i++){
                    if (board[this.row + i][this.col + i] != null)
                        return false;
                }
            }

            // check bottom left
            if (row - this.row > 0 && col - this.col < 0){
                for (int i = 1; i < Math.abs(row - this.row); i++){
                    if (board[this.row + i][this.col - i] != null)
                        return false;
                }
            }

            // check upper right
            if (row - this.row < 0 && col - this.col > 0){
                for (int i = 1; i < Math.abs(row - this.row); i++){
                    if (board[this.row - i][this.col + i] != null)
                        return false;
                }
            }

            // check upper left
            if (row - this.row < 0 && col - this.col < 0){
                for (int i = 1; i < Math.abs(row - this.row); i++){
                    if (board[this.row - i][this.col - i] != null)
                        return false;
                }
            }

            return true;
        }
        
        return false;
    }

    public int getValue(){
        return 9;
    }
}
