package Pieces;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Abstract base class representing a chess piece.
 * All chess pieces share a common color, board position, and move-tracking state.
 * Concrete subclasses must implement movement validation, piece type identification,
 * and point value, as each piece has unique movement rules.
 */
public abstract class Piece{

    /**
     * The color of this piece ("White" or "Black").
     */
    public String color;

    /**
     * The current row position of this piece on the board
     * Row 0 is the top of the board (Black's back rank),
     * row 7 is the bottom (White's back rank).
     */
    public int row;

    /**
     * The current column position of this piece on the board
     * Column 0 is the queenside, column 7 is the kingside.
     */
    public int col;

    /**
     * Tracks whether this piece has moved at any point during the game.
     * Relevant for castling eligibility ({@link King}, {@link Rook})
     * and the Pawn's two-square opening advance.
     */
    boolean hasMoved;

    /**
     * Constructs a Piece with the specified color and starting position.
     * All pieces begin the game having not yet moved.
     * @param color the color of the piece ("White" or "Black")
     * @param row   the initial row position on the board
     * @param col   the initial column position on the board
     */
    public Piece(String color, int row, int col){
        this.color = color;
        this.row = row;
        this.col = col;
        hasMoved = false;
    }

    /**
     * Returns the color of this piece.
     * @return "White" or "Black"
     */
    public String getColor(){
        return color;
    }

    /**
     * Indicates whether this piece is eligible for promotion.
     * Returns {false} by default; overridden by {@link Pawn} to apply
     * promotion rank logic.
     * @return {false} unless overridden by a subclass
     */
    public boolean canPromote(String color){
        return false;
    }

    /**
     * Returns the current row position of this piece.
     *
     * @return the row index
     */
    public int getRow(){
        return row;
    }

    /**
     * Returns the current column position of this piece.
     *
     * @return the column index (0-indexed)
     */
    public int getCol(){
        return col;
    }

    /**
     * Sets the row position of this piece.
     * @param row the new row index
     */
    public void setRow(int row){
        this.row = row;
    }

    /**
     * Sets the column position of this piece.
     * @param col the new column index
     */
    public void setCol(int col){
        this.col = col;
    }

    public String getImagePath() {
        String colorPrefix = color.equals("White") ? "w" : "b";

        String pieceChar;
        switch (getType()) {
            case "King":   pieceChar = "K"; break;
            case "Queen":  pieceChar = "Q"; break;
            case "Rook":   pieceChar = "R"; break;
            case "Bishop": pieceChar = "B"; break;
            case "Knight": pieceChar = "N"; break;
            default:       pieceChar = "P"; break;  // Pawn
        }

        // Returns the classpath resource path – works from IDE and from JAR
        return "/images/" + colorPrefix + pieceChar + ".png";
    }

    public Group draw() {
        Image img = new Image(
            Piece.class.getResourceAsStream(getImagePath()),
            80, 80, true, true
        );
        ImageView iv = new ImageView(img);
        iv.setFitWidth(80);
        iv.setFitHeight(80);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return new Group(iv);
    }

    /**
     * Returns whether this piece can attack the given square based purely on its
     * movement pattern, without considering castling. Used for threat detection
     * to avoid infinite recursion between kings checking each other's castling rights.
     * By default delegates to isLegal; overridden by {@link King}.
     * @param row   the target row
     * @param col   the target column
     * @param board the current board state
     * @return {true} if this piece attacks that square
     */
    public boolean isAttacking(int row, int col, Piece[][] board){
        return isLegal(row, col, board);
    }

    /**
     * Determines whether the piece at (row, col) has no legal moves that leave
     * its own king safe. Simulates every possible destination and checks if any
     * move results in the king not being in check.
     * @param row   the row of the piece to check (0-indexed)
     * @param col   the column of the piece to check (0-indexed)
     * @param board the current state of the chess board, where {null} represents an empty square
     * @return {true} if no legal move exists for the piece at {(row, col)},
     *         {false} if at least one legal move is found
     */
    public boolean hasNoLegal(int row, int col, Piece[][] board){
        Piece movingPiece = board[row][col];
        if (movingPiece == null) return true;
        String color = movingPiece.getColor();
        String opponentColor = color.equals("White") ? "Black" : "White";

        for (int r = 0; r < board.length; r++){
            for (int c = 0; c < board[0].length; c++){
                if (movingPiece.isLegal(r, c, board)){
                    // Simulate the move
                    Piece captured = board[r][c];
                    board[r][c] = movingPiece;
                    board[row][col] = null;
                    int oldRow = movingPiece.row;
                    int oldCol = movingPiece.col;
                    movingPiece.row = r;
                    movingPiece.col = c;

                    // Find king position after move
                    int kingRow = -1, kingCol = -1;
                    for (int kr = 0; kr < board.length; kr++){
                        for (int kc = 0; kc < board[0].length; kc++){
                            if (board[kr][kc] != null && board[kr][kc].getType().equals("King") && board[kr][kc].getColor().equals(color)){
                                kingRow = kr;
                                kingCol = kc;
                            }
                        }
                    }

                    boolean kingInCheck = false;
                    if (kingRow != -1){
                        for (int ar = 0; ar < board.length; ar++){
                            for (int ac = 0; ac < board[0].length; ac++){
                                if (board[ar][ac] != null && board[ar][ac].getColor().equals(opponentColor) && board[ar][ac].isAttacking(kingRow, kingCol, board)){
                                    kingInCheck = true;
                                }
                            }
                        }
                    }

                    // Undo the move
                    board[row][col] = movingPiece;
                    board[r][c] = captured;
                    movingPiece.row = oldRow;
                    movingPiece.col = oldCol;

                    if (!kingInCheck) return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines whether moving this piece to the specified position is a legal move.
     * Rules vary by piece type and are defined in each concrete subclass.
     * @param row   the target row to move to
     * @param col   the target column to move to
     * @param board the current state of the chess board, where {null} represents an empty square
     * @return {true} if the move is legal, {false} otherwise
     */
    public abstract boolean isLegal(int row, int col, Piece[][] board);

    /**
     * Returns the relative point value of this piece, used for material evaluation.
     * Values are: Pawn = 1, Knight = 3, Bishop = 3, Rook = 5, Queen = 9, King = 0.
     * @return the integer point value of this piece
     */
    public abstract int getValue();

    /**
     * Returns the type name of this piece (e.g. "Pawn", "Rook", "King").
     * @return a string identifying the type of this piece
     */
    public abstract String getType();

    /**
     * Marks this piece as having moved by setting {hasMoved} to {true}.
     * Should be called whenever this piece is successfully moved on the board.
     */
    public void moved() {
        hasMoved = true;
    }

    /**
     * Returns whether this piece has moved at any point during the game.
     * @return {true} if the piece has moved, {false} otherwise
     */
    public boolean hasMoved() {
        return hasMoved;
    }
}