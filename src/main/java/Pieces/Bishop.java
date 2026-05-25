package Pieces;

/**
 * Represents a Bishop chess piece.
 * The Bishop moves diagonally any number of squares in any direction,
 * and cannot jump over other pieces.
 */
public class Bishop extends Piece {
    
    /**
     * Constructs a Bishop with the specified color and position.
     *
     * @param color the color of the Bishop ("white" or "black")
     * @param row   the initial row position on the board
     * @param col   the initial column position on the board
     */
    public Bishop(String color, int row, int col){
        super(color, row, col);
    }

    /**
     * Returns the type of this chess piece.
     *
     * @return the string "Bishop"
     */
    public String getType(){
        return "Bishop";
    }

    /**
     * Determines whether moving this Bishop to the specified position is a legal move.
     * A move is legal if:
     *
     * @param row   the target row to move to
     * @param col   the target column to move to
     * @param board the current state of the chess board, where {null} represents an empty square
     * @return {true} if the move is legal, {false} otherwise
     */
    public boolean isLegal(int row, int col, Piece[][] board){
        if (board[row][col] != null && board[row][col].getColor().equals(this.getColor()))
            return false;

        if (Math.abs(row - this.row) != Math.abs(col - this.col))
            return false;

        if (Math.abs(row - this.row) == Math.abs(col - this.col)){

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

    /**
     * Returns the relative point value of the Bishop.
     * The Bishop is valued at 3 points,
     * roughly equivalent to a Knight.
     * @return the integer value {@code 3}
     */
    public int getValue(){
        return 3;
    }
}
