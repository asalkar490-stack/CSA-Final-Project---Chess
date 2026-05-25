package Pieces;

/**
 * Represents a Queen chess piece.
 * The Queen combines the movement of a {@link Rook} and a {@link Bishop},
 * moving any number of squares horizontally, vertically, or diagonally.
 * It cannot jump over other pieces in any direction.
 */
public class Queen extends Piece {

    /**
     * Constructs a Queen with the specified color and position.
     * @param color the color of the Queen ("White" or "Black")
     * @param row   the initial row position on the board
     * @param col   the initial column position on the board
     */
    public Queen(String color, int row, int col){
        super(color, row, col);
    }

    /**
     * Returns the type of this chess piece.
     * @return the string "Queen"
     */
    public String getType(){
        return "Queen";
    }

    /**
     * Determines whether moving this Queen to the specified position is a legal move.
     * Uses the isLegal algorithms from both rook and bishop
     * @param row   the target row to move to
     * @param col   the target column to move to
     * @param board the current state of the chess board, where {null} represents an empty square
     * @return {true} if the move is legal, {false} otherwise
     */
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

    /**
     * Returns the relative point value of the Queen.
     * The Queen is valued at 9 points,
     * making it the most valuable piece aside from the King.
     * @return the integer value {9}
     */
    public int getValue(){
        return 9;
    }
}
