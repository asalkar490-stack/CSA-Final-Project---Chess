package Pieces;

/**
 * Represents a Knight chess piece.
 * The Knight moves in an "L-shape"
 * It is the only piece that can jump over other pieces.
 */
public class Knight extends Piece {
    
    /**
     * Constructs a Knight with the specified color and position.
     *
     * @param color the color of the Knight ("white" or "black")
     * @param row   the initial row position on the board
     * @param col   the initial column position on the board
     */
    public Knight(String color, int row, int col){
        super(color, row, col);
    }

    /**
     * Determines whether moving this Knight to the specified position is a legal move.
     * @param row   the target row to move to
     * @param col   the target column to move to
     * @param board the current state of the chess board, where {null} represents an empty square
     * @return {true} if the move is a legal L-shape and the destination is not friendly-occupied,
     *         {false} otherwise
     */
    public boolean isLegal(int row, int col, Piece[][] board){
        if (Math.abs(row-this.row) == 2 && Math.abs(col-this.col) == 1 || Math.abs(row-this.row) == 1 && Math.abs(col-this.col) == 2) {
            if (board[row][col] != null && board[row][col].getColor().equals(this.getColor())) 
                return false;
            return true;
        }
        return false;
    }

    /**
     * Returns the type of this chess piece.
     *
     * @return the string "Knight"
     */
    public String getType(){
        return "Knight";
    }

    /**
     * Returns the relative point value of the Knight.
     * The Knight is valued at 3 points,
     * roughly equivalent to a Bishop.
     * @return the integer value {@code 3}
     */
    public int getValue(){
        return 3;
    }
}
