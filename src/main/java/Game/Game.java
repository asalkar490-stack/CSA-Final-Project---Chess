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
     * @return Boolean: if the current player is in check, return true.
     */
    public boolean check(int row, int col) {
        if (currentPlayersColor == "White") {
            Board b = getBoard();
            if (b.isThreatened(row, col))
        }
    }


}
