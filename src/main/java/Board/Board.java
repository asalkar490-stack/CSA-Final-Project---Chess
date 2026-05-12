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
        } else if (input.equals("standard")) {
            for (int i = 0; i < board.length; i++) {
                board[6][i] = new Pawn("white", 6, i);
            }
            for (int i = 0; i < board.length; i++) {
                board[1][i] = new Pawn("black", 6, i);
            }
            board[7][0] = new Rook("white", 7, 0);
            board[7][1] = new Knight("white", 7, 1);
            board[7][2] = new Bishop("white", 7, 2);
            board[7][3] = new Queen("white", 7, 3);
            board[7][4] = new King("white", 7, 4);
            board[7][5] = new Bishop("white", 7, 5);
            board[7][6] = new Knight("white", 7, 6);
            board[7][7] = new Rook("white", 7, 7);

            board[0][0] = new Rook("black", 0, 0);
            board[0][1] = new Knight("black", 0, 1);
            board[0][2] = new Bishop("black", 0, 2);
            board[0][3] = new Queen("black", 0, 3);
            board[0][4] = new King("black", 0, 4);
            board[0][5] = new Bishop("black", 0, 5);
            board[0][6] = new Knight("black", 0, 6);
            board[0][7] = new Rook("black", 0, 7);
        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPieceAt(int r, int c) {
        System.out.println(getBoard()[r][c].type());
        return getBoard()[r][c];
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

    public static void main (String[] args) {
        Board b = new Board("standard");
        b.printBoard();
        Board c = new Board("empty");
        c.printBoard();
    }


}
