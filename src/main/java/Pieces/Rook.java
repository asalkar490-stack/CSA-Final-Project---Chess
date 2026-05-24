package Pieces;

/**
 * Represents a Rook chess piece.
 * The Rook moves any number of squares horizontally or vertically,
 * and cannot jump over other pieces. It participates in castling
 * with the {@link King} if neither piece has previously moved.
 */
public class Rook extends Piece  {

    /**
     * Tracks whether this Rook has moved at any point during the game.
     * Used to determine castling eligibility alongside the {@link King}.
     */
    private boolean hasMoved;

    /**
     * Constructs a Rook with the specified color and position.
     * The Rook starts the game having not yet moved.
     * @param color the color of the Rook ("White" or "Black")
     * @param row   the initial row position on the board
     * @param col   the initial column position on the board
     */
    public Rook(String color, int row, int col){
        super(color, row, col);
        hasMoved = false;
    }

    /**
     * Returns the type of this chess piece.
     * @return the string "Rook"
     */
    public String getType(){
        return "Rook";
    }

    /**
     * Determines whether moving this Rook to the specified position is a legal move.
     * @param row   the target row to move to
     * @param col   the target column to move to
     * @param board the current state of the chess board, where {null} represents an empty square
     * @return {true} if the move is legal, {false} otherwise
     */
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

    /**
     * Returns the relative point value of the Rook.
     * The Rook is valued at 5 points.
     * @return the integer value {@code 5}
     */
    public int getValue(){
        return 5;
    }

    /**
     * Marks this Rook as having moved, disqualifying it from future castling.
     * Overrides {@link Piece#moved()} to update the Rook's own {hasMoved} field
     * in addition to any base class behavior.
     */
    @Override
    public void moved() {
        hasMoved = true;
    }

    /**
     * Returns whether this Rook has moved at any point during the game.
     * Used by {@link King#canCastle(int, int, Piece[][])} to verify castling eligibility.
     * @return {true} if the Rook has moved, {false} otherwise
     */
    public boolean hasMoved() {
        return hasMoved;
    }
}
