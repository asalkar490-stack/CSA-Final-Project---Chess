package Pieces;

/**
 * Represents a King chess piece.
 * The King can move one square in any direction, and can perform a 
 * castling move with a Rook if neither piece has moved and the path is clear.
 */
public class King extends Piece {
    
    /**
     * Tracks whether this King has moved at any point during the game.
     * Used to determine if castling is possible
     */
    private boolean hasMoved;

    /**
     * Constructs a King with the specified color and position.
     * The King starts the game having not yet moved.
     *
     * @param color the color of the King ("white" or "black")
     * @param row   the initial row position on the board
     * @param col   the initial column position on the board
     */
    public King(String color, int row, int col){
        super(color, row, col);
        hasMoved = false;
    }

    /**
     * Determines whether moving this King to the specified position is a legal move.
     * A move is legal if the destination is within one square in any direction
     * from the King's current position.
     * @param row   the target row to move to
     * @param col   the target column to move to
     * @param board the current state of the chess board, where {null} represents an empty square
     * @return {@code true} if the move is within one square, {alse} otherwise
     */
    public boolean isLegal(int row, int col, Piece[][] board){

        if (row == this.row && col == this.col)
            return false;

        if (Math.abs(row - this.row) <= 1 && Math.abs(col - this.col) <= 1)
            return true;

        return false;
    }

    /**
     * Attempts to perform a castling move for this King to the specified position.
     * If castling is legal, both the King and the corresponding Rook are repositioned.
     * The King moves two squares toward the Rook, and the Rook jumps to the other side of the King.
     * @param row   the target row of the King after castling
     * @param col   the target column of the King after castling
     * @param board the current state of the chess board, modified in place to reflect the castle
     */
    public void castle(int row, int col, Piece[][] board){
        //black queenside
        if (canCastle(row, col, board)){
            if (row == 0 && col == 2){
                board[this.row][this.col] = null;
                board[row][col] = this;
                Piece temp = board[row][0];
                board[row][0] = null;
                board[row][3] = temp;
            }

            //black kingside
            if (row == 0 && col == 6){
                board[this.row][this.col] = null;
                board[row][col] = this;
                Piece temp = board[row][7];
                board[row][7] = null;
                board[row][5] = temp;
            }

            //white, queenside
            if (row == 7 && col == 2){
                board[this.row][this.col] = null;
                board[row][col] = this;
                Piece temp = board[row][0];
                board[row][0] = null;
                board[row][3] = temp;
            }

            //white kingside
            if (row == 7 && col == 6){
                board[this.row][this.col] = null;
                board[row][col] = this;
                Piece temp = board[row][7];
                board[row][7] = null;
                board[row][5] = temp;
            }

            this.row = row;
            this.col = col;
            hasMoved = true;
        }
    }

    /**
     * Returns whether this King has moved at any point during the game.
     *
     * @return {true} if the King has moved, {false} otherwise
     */
    public boolean hasMoved(){
        return hasMoved;   
    }

    
    /**
     * Determines whether this King is eligible to castle to the specified position.
     * @param row   the target row of the castling destination
     * @param col   the target column of the castling destination
     * @param board the current state of the chess board
     * @return {true} if castling to the given position is legal, {false} otherwise
     */
    public boolean canCastle(int row, int col, Piece[][] board){
        if (hasMoved())return false;

        //check white side
        if (this.getColor().equals("White")){

            //white queenside
            if (row == 7 && col == 2 && board[7][0] != null && board[7][0].getType().equals("Rook") && !board[7][0].hasMoved() && board[7][1] == null && board[7][2] == null && board[7][3] == null)
                return true;

            //white kingside
            if (row == 7 && col == 6 && board[7][7] != null && board[7][7].getType().equals("Rook") && !board[7][7].hasMoved() && board[7][5] == null && board[7][6] == null)
                return true;
        }

        //check left side
        if (this.getColor().equals("Black")){

            //black queenside
            if (row == 0 && col == 2 && board[0][0] != null && board[0][0].getType().equals("Rook") && !board[0][0].hasMoved() && board[0][1] == null && board[0][2] == null && board[0][3] == null)
                return true;

            //black kingside
            if (row == 0 && col == 6 && board[0][7] != null && board[0][7].getType().equals("Rook") && !board[0][7].hasMoved() && board[0][5] == null && board[0][6] == null)
                return true;
        }
        return false;
    }

    /**
     * Returns the type of this chess piece.
     *
     * @return the string "King"
     */
    public String getType(){
        return "King";
    }

    /**
     * Returns the relative point value of the King.
     * The King has no standard point value in material counting,
     * as it cannot be captured or traded.
     *
     * @return {0}, the King has no material value
     */
    public int getValue(){
        return 0;
    }
}
