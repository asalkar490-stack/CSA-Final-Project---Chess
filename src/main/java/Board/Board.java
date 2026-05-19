package Board;
import Pieces.*;

public class Board {
    private Piece[][] board;

    public Board(String input) {
        board = new Piece[8][8];
        if (input.equals("empty")) {
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    board[r][c] = null;
                }
            }
        } else if (input.equals("playaswhite")) {
            for (int i = 0; i < board.length; i++) {
                board[6][i] = new Pawn("Black", 6, i);
            }
            for (int i = 0; i < board.length; i++) {
                board[1][i] = new Pawn("White", 6, i);
            }
            board[7][0] = new Rook("White", 7, 0);
            board[7][1] = new Knight("White", 7, 1);
            board[7][2] = new Bishop("White", 7, 2);
            board[7][3] = new Queen("White", 7, 3);
            board[7][4] = new King("White", 7, 4);
            board[7][5] = new Bishop("White", 7, 5);
            board[7][6] = new Knight("White", 7, 6);
            board[7][7] = new Rook("White", 7, 7);

            board[0][0] = new Rook("Black", 0, 0);
            board[0][1] = new Knight("Black", 0, 1);
            board[0][2] = new Bishop("Black", 0, 2);
            board[0][3] = new Queen("Black", 0, 3);
            board[0][4] = new King("Black", 0, 4);
            board[0][5] = new Bishop("Black", 0, 5);
            board[0][6] = new Knight("Black", 0, 6);
            board[0][7] = new Rook("Black", 0, 7);
        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    public int getLength() {
        return board.length;
    }

    public Piece getPieceAt(int r, int c) {
        System.out.println(getBoard()[r][c].type());
        return getBoard()[r][c];
    }

    /**
     * Finds the piece at a given location.
     * @param args
     * @return Returns an array where the first element is the row and the second element is the column.
     */
    public int[] findPiece(String type, String color) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (getPieceAt(r,c).)
            }
        }
    }

    public String toString() {
        String s = "";
        for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    if (getBoard()[r][c] != null) {
                        s += (getBoard()[r][c].type());
                    }
                }
            }
        return s;
    }

    public void printBoard() {
        for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    if (getBoard()[r][c] != null) {
                        System.out.print(getBoard()[r][c].type() + " ");
                    } else {
                        System.out.print("null" + " ");
                    }
                }
                System.out.print("\n");
        }
    }

    public boolean isThreatened(int thisRow, int thisCol) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c].isLegal(thisRow, thisCol, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main (String[] args) {
        Board b = new Board("playaswhite");
        b.printBoard();
        Board c = new Board("empty");
        c.printBoard();
    }


}
