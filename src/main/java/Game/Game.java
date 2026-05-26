package Game;
import Board.Board;
import Pieces.Bishop;
import Pieces.King;
import Pieces.Piece;
public class Game {
    private int turn;
    private Board board;
    private boolean isWhiteStart;
    private String currentPlayersColor;
    
    /**
     * Constructer for the Game class. Initializes a board and sets the turn to 1.
     * @param board
     */
    public Game(Board board, boolean isWhiteStart) {
        this.board = board;
        turn = 1;
        this.isWhiteStart = isWhiteStart;
        if (isWhiteStart) {
            currentPlayersColor = "White";
        } else {
            currentPlayersColor = "Black";
        }
    }

    /**
     * Returns the current board.
     * @return
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the current players color
     * @return String currentPlayersColor, either "White" or "Black"
     */
    public String getCurrentPlayersColor() {
        return currentPlayersColor;
    }

    /**
     * Increases the turn counter. Switches the current player.
     */
    public void updateTurn() {
        turn++;
        switchColor();
    }

    /**
     * Switches the current player from black to white and vice versa.
     */
    public void switchColor() {
        if (currentPlayersColor.equals("White")) {
            currentPlayersColor = "Black";
        } else {
            currentPlayersColor = "White";
        }
    }

    /**
     * Returns the current turn number. 
     */
    public int getTurnNumber() {
        return turn;
    }

    /**
     * Returns a boolean. If white starts, it returns true. If black starts, it returns false.
     */
    public boolean isWhiteStart(){
        return isWhiteStart;
    }

    /**
     * Checks if the current player's king is in check.
     * @return Boolean: if the current player's king is under attack, return true.
     */
    public boolean isCheck() {
        Board b = getBoard();
        Piece king = b.findPiece("King", currentPlayersColor);
        int row = king.getRow();
        int col = king.getCol();
        return b.isThreatened(row, col, getCurrentPlayersColor());
    }


    /**
     * Checks if the current player is checkmated.
     * A player is in checkmate if their king is in check and no piece of theirs
     * has any legal move that would remove the check.
     * @return Boolean: if the current player is in checkmate, return true.
     */
    public boolean isCheckmate() {
        if (!isCheck()) return false;
        Board b = getBoard();
        Piece[][] pieces = b.getBoard();
        for (int r = 0; r < pieces.length; r++){
            for (int c = 0; c < pieces[0].length; c++){
                if (pieces[r][c] != null && pieces[r][c].getColor().equals(currentPlayersColor)){
                    if (!pieces[r][c].hasNoLegal(r, c, pieces))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the current player is in stalemate.
     * A player is in stalemate if they are not in check but have no legal moves.
     * @return Boolean: if the current player is in stalemate, return true.
     */
    public boolean isStalemate() {
        if (isCheck()) return false;
        Board b = getBoard();
        Piece[][] pieces = b.getBoard();
        for (int r = 0; r < pieces.length; r++){
            for (int c = 0; c < pieces[0].length; c++){
                if (pieces[r][c] != null && pieces[r][c].getColor().equals(currentPlayersColor)){
                    if (!pieces[r][c].hasNoLegal(r, c, pieces))
                        return false;
                }
            }
        }
        return true;
    }


    public static void main(String[] a) {
        Board testBoard = new Board("empty");
        Game testGame = new Game(testBoard, true);
        testGame.getBoard().printBoard();
        Bishop bishop = new Bishop("Black", 0, 0);
        King king = new King("White", 7, 7);
        testBoard.putPiece(bishop);
        testBoard.putPiece(king);
        testBoard.printBoard();
        System.out.println(testGame.isCheck());
    }
}