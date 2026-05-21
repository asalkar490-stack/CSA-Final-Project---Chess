package Game;
import Board.Board;
import Pieces.*;
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
        if (currentPlayersColor == "White") {
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
     * Checks if the current player is in check
     * @return Boolean: if the current player is in check and not in checkmate, return true.
     */
    public boolean isCheck() {
        Board b = getBoard();
        int[] location = b.findPiece("King", currentPlayersColor);
        int row = location[0];
        int col = location[1];
        if (b.isThreatened(row, col) && !(b.getPieceAt(row, col).hasNoLegal(row, col, b.getBoard()))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the current player is checkmated
     * Precondition: The piece at the specified location is a King
     * @return Boolean: if the current player is in checkmate, return true.
     */
    public boolean isCheckmate(int row, int col) {
        Board b = getBoard();
        if (b.isThreatened(row, col) && b.getPieceAt(row, col).hasNoLegal(row, col, b.getBoard())) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] a) {
        Board testBoard = new Board("playaswhite");
        Game testGame = new Game(testBoard, true);
        testGame.getBoard().printBoard();
    }
}
