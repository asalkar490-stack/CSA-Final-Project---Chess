package Game;
import Board.*;
import Pieces.*;
import java.util.*;
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
     * Checks if the current player is in check and not in checkmate
     * @return Boolean: if the current player is in check and not in checkmate, return true.
     */
    public boolean isCheck() {
        Board b = getBoard();
        Piece piece = b.findPiece("King", currentPlayersColor);
        int row = piece.getRow();
        int col = piece.getCol();
        if (b.isThreatened(row, col) && !(b.getPieceAt(row, col).hasNoLegal(row, col, b.getBoard()))) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Checks if the current player is checkmated
     * @return Boolean: if the current player is in checkmate, return true.
     */
    public boolean isCheckmate() {
        Board b = getBoard();
        Piece piece = b.findPiece("King", currentPlayersColor);
        int row = piece.getRow();
        int col = piece.getCol();
        if (isCheck() && b.getPieceAt(row, col).hasNoLegal(row, col, b.getBoard())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the check on the current king can be blocked by a friendly piece. If it can, returns true. If that friendly piece were to move and 
     * expose a different check or there are no pieces that can block the check, return false.
     * @param attackingPiece
     * @return
     */
    public boolean checkCanBeBlocked(Piece attackingPiece) {
        if (attackingPiece instanceof Knight || attackingPiece instanceof Pawn) {
            return false;
        }
        ArrayList<Location> piecesBetween = getBoard().getLocationsBetween(attackingPiece, getBoard().findPiece("King", currentPlayersColor));
        Location attacklocation = new Location(attackingPiece.getRow(), attackingPiece.getCol());
        piecesBetween.add(attacklocation);
        for (int r = 0; r < board.getLength(); r++) {
            for (int c = 0; c < board.getLength(); c++) {
                Piece p = getBoard().getPieceAt(r, c);
                if (p != null && !(p.equals(attackingPiece)) && !(p.equals(getBoard().findPiece("King", currentPlayersColor))) && p.getColor() == getCurrentPlayersColor()) {
                    for (Location l: piecesBetween) {
                        if (p.isLegal(l.getRow(), l.getCol(), getBoard().getBoard())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
