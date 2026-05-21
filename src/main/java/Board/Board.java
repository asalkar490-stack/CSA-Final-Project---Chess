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
                board[6][i] = new Pawn("White", 6, i);
            }
            for (int i = 0; i < board.length; i++) {
                board[1][i] = new Pawn("Black", 6, i);
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

        } else if (input.equals("playasblack")) {
            for (int i = 0; i < board.length; i++) {
                board[6][i] = new Pawn("Black", 6, i);
            }
            for (int i = 0; i < board.length; i++) {
                board[1][i] = new Pawn("White", 6, i);
            }
            board[7][0] = new Rook("Black", 7, 0);
            board[7][1] = new Knight("Black", 7, 1);
            board[7][2] = new Bishop("Black", 7, 2);
            board[7][3] = new King("Black", 7, 3);
            board[7][4] = new Queen("Black", 7, 4);
            board[7][5] = new Bishop("Black", 7, 5);
            board[7][6] = new Knight("Black", 7, 6);
            board[7][7] = new Rook("Black", 7, 7);

            board[0][0] = new Rook("White", 0, 0);
            board[0][1] = new Knight("White", 0, 1);
            board[0][2] = new Bishop("White", 0, 2);
            board[0][3] = new King("White", 0, 3);
            board[0][4] = new Queen("White", 0, 4);
            board[0][5] = new Bishop("White", 0, 5);
            board[0][6] = new Knight("White", 0, 6);
            board[0][7] = new Rook("White", 0, 7);
        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    public int getLength() {
        return board.length;
    }

    public Piece getPieceAt(int r, int c) {
        System.out.println(getBoard()[r][c].getType());
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
                if (getPieceAt(r,c).getType().equals(type) && getPieceAt(r, c).getColor().equals(color)) {
                    return new int[]{r, c};
                }
            }
        } 
        return new int[0];
    }

    /**
     * Puts a piece at a given location.
     * Precondition: Location is a valid tile on the board.
     * @param row
     * @param col
     */
    public void putPiece(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    public String toString() {
        String s = "";
        for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    if (getBoard()[r][c] != null) {
                        s += (getBoard()[r][c].getType());
                    }
                }
            }
        return s;
    }

    /**
     * Prints out the board, with the pieces as their names.
     */
    public void printBoard() {
        for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    if (getBoard()[r][c] != null) {
                        System.out.print(getBoard()[r][c].getType() + " ");
                    } else {
                        System.out.print("null" + " ");
                    }
                }
                System.out.print("\n");
        }
        System.out.print("\n");
    }
    /**
     * Prints out the board, with the pieces as their color. Mainly for testing purposes
     */
    public void printBoardWithColors() {
        for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    if (getBoard()[r][c] != null) {
                        System.out.print(getBoard()[r][c].getColor() + " ");
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
        // Board b = new Board("playaswhite");
        // b.printBoard();
        // b.printBoardWithColors();
        Board c = new Board("empty");
        Pawn p = new Pawn("Black", 0, 0);
        Pawn p2 = new Pawn("White", 1, 1);
        c.putPiece(1, 0, p);
        c.putPiece(2, 1, p2);
        c.printBoardWithColors();
        System.out.println(p2.isLegal(p.getRow(),p.getCol(), c.getBoard()));
    }


}
