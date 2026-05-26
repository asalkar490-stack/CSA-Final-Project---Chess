package Pieces;

/**
 * Represents a Pawn chess piece.
 * The Pawn moves forward one square, or two squares from its starting position.
 * It captures diagonally one square forward. Upon reaching the opposite end of
 * the board, it can be promoted to another piece.
 */
public class Pawn extends Piece {

    /**
     * Tracks whether this Pawn has moved at any point during the game.
     * Used to determine eligibility for the two-square opening move.
     */
    private boolean hasMoved;

    /**
     * Constructs a Pawn with the specified color and position.
     * The Pawn starts the game having not yet moved.
     *
     * @param color the color of the Pawn ("White" or "Black")
     * @param row   the initial row position on the board
     * @param col   the initial column position on the board
     */
    public Pawn(String color, int row, int col){
        super(color, row, col);
        hasMoved = false;
    }

    /**
     * Determines whether moving this Pawn to the specified position is a legal move.
     * @param row   the target row to move to
     * @param col   the target column to move to
     * @param board the current state of the chess board, where {null} represents an empty square
     * @return {true} if the move is legal, {false} otherwise
     */
    public boolean isLegal(int row, int col, Piece[][] board){
        if(!hasMoved() && this.getColor().equals("White") && row < this.row && this.row - row <= 2 && this.col == col && board[row][col] == null && board[this.row - 1][this.col] == null)
            return true;

        else if (!hasMoved() && this.getColor().equals("Black") && row > this.row && row - this.row <= 2 && this.col == col && board[row][col] == null && board[this.row + 1][this.col] == null)
            return true;

        else if (this.hasMoved() && this.getColor().equals("White") && this.row - row == 1 && this.col == col && board[row][col] == null)
            return true;

        else if (this.hasMoved() && this.getColor().equals("Black") && row - this.row == 1 && this.col == col && board[row][col] == null)
            return true;

        else if (this.getColor().equals("White") && this.row - row == 1 && Math.abs(this.col - col) == 1 && board[row][col] != null && !board[row][col].getColor().equals(this.getColor()))
            return true;

        else if (this.getColor().equals("Black") && row - this.row == 1 && Math.abs(this.col - col) == 1 && board[row][col] != null && !board[row][col].getColor().equals(this.getColor()))
            return true;

        return false;
    }


    /**
     * Returns the type of this chess piece.
     *
     * @return the string "Pawn"
     */
    public String getType(){
        return "Pawn";
    }

    /**
     * Determines whether this Pawn is eligible for promotion.
     * A Pawn can be promoted when it reaches the opposite end of the board
     * @param color the color of the Pawn ("White" or "Black")
     * @return {true} if the Pawn has reached the promotion rank, {false} otherwise
     */
    public boolean canPromote(String color){
        if (color.equals("White") && this.row == 0)
            return true;
        if (color.equals("Black") && this.row == 7)
            return true;
        return false;
    }

    /**
     * Promotes this Pawn by replacing it on the board with the given piece.
     * The new piece is placed at the Pawn's current position.
     * Note: This method does not verify that the Pawn is eligible for promotion.
     * @param board    the current state of the chess board, modified in place
     * @param newPiece the piece to replace this Pawn with (e.g. Queen, Rook, Bishop, or Knight)
     */
    public void Promote(Piece[][] board, Piece newPiece){
        if (canPromote(getColor()))
            board[this.row][this.col] = newPiece;
    }

    /**
     * Returns the relative point value of the Pawn.
     * The Pawn is valued at 1 point.
     * @return the integer value {1}
     */
    public int getValue(){
        return 1;
    }

    /**
     * Returns whether this Pawn has moved at any point during the game.
     * @return {true} if the Pawn has moved, {false} otherwise
     */
    public boolean hasMoved(){
        return hasMoved;
    }
}
