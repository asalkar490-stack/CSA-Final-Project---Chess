package Board;
import Pieces.Piece;
import Pieces.Pawn;

public class Board {
    private Piece[][] board;

    public Board() {
        board = new Piece[8][8];
        board[6][0] = new Pawn("white", 6, 0);
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void explode() {
        
    }


}
