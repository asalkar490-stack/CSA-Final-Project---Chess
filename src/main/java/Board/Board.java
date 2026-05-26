package Board;
import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;

public class Board {
    private Piece[][] board;

    /**
     * The constructer for the Board class. If the input is "empty," then the Board will be initialized to all null.
     * If it is "playasblack" then black will be at the bottom.
     * If it is "playaswhite" then white will be at the bottom.
     * @param input
     */
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
                board[1][i] = new Pawn("Black", 1, i);
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
                board[1][i] = new Pawn("White", 1, i);
            }
            board[7][0] = new Rook("Black", 7, 0);
            board[7][1] = new Knight("Black", 7, 1);
            board[7][2] = new Bishop("Black", 7, 2);
            board[7][3] = new Queen("Black", 7, 3);
            board[7][4] = new King("Black", 7, 4);
            board[7][5] = new Bishop("Black", 7, 5);
            board[7][6] = new Knight("Black", 7, 6);
            board[7][7] = new Rook("Black", 7, 7);

            board[0][0] = new Rook("White", 0, 0);
            board[0][1] = new Knight("White", 0, 1);
            board[0][2] = new Bishop("White", 0, 2);
            board[0][3] = new Queen("White", 0, 3);
            board[0][4] = new King("White", 0, 4);
            board[0][5] = new Bishop("White", 0, 5);
            board[0][6] = new Knight("White", 0, 6);
            board[0][7] = new Rook("White", 0, 7);
        }
    }

    /**
     * Returns the board.
     * @return
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * Returns the row length.
     * @return
     */
    public int getLength() {
        return board.length;
    }

    /**
     * Checks if the given position has no Piece.
     * @param row
     * @param column
     * @return
     */
    public boolean isNull(int row, int column) {
        if (board[row][column] == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the Piece at a specified position.
     * @param r
     * @param c
     * @return
     */
    public Piece getPieceAt(int r, int c) {
        return getBoard()[r][c];
    }

    /**
     * Checks if the pawn at a location can promote. If it can, this method calls the canPromote method of the Piece on it and returns true.
     * @param row
     * @param col
     * @param piece
     * @return
     */
    public boolean promote(int row, int col, Piece newPiece) {
        if (!(isNull(row, col)) && getPieceAt(row, col).canPromote(getPieceAt(row, col).getColor())) {
            putPiece(newPiece);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Finds the piece at a given location.
     * @param args
     * @return Returns an array where the first element is the row and the second element is the column.
     */
    public int[] findPiece(String type, String color) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (getPieceAt(r,c) != null && getPieceAt(r,c).getType().equals(type) && getPieceAt(r, c).getColor().equals(color)) {
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
    public void putPiece(Piece piece) {
        board[piece.getRow()][piece.getCol()] = piece;
    }

    /**
     * Returns the board as a really long string.
     */
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
     * Checks if a square is threatened by any piece of the opposing color.
     * @param thisRow
     * @param thisCol
     * @param byColor the attacking color
     * @return
     */
    public boolean isThreatenedBy(int thisRow, int thisCol, String byColor) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (!isNull(r, c) && board[r][c].getColor().equals(byColor) && board[r][c].isLegal(thisRow, thisCol, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a Piece at a given location is threatened by any opposing piece.
     * @param thisRow
     * @param thisCol
     * @return
     */
    public boolean isThreatened(int thisRow, int thisCol) {
        if (isNull(thisRow, thisCol)) return false;
        String ownColor = board[thisRow][thisCol].getColor();
        String opponentColor = ownColor.equals("White") ? "Black" : "White";
        return isThreatenedBy(thisRow, thisCol, opponentColor);
    }

    /**
     * Removes all pieces from the Board.
     */
    public void clearBoard() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                board[r][c] = null;
            }
        }
    }

    public static void main (String[] args) {
        Board b = new Board("playaswhite");
        b.printBoard();
        System.out.println(b.isNull(0, 0));
        System.out.println(b.isThreatened(0, 0));
    }


}