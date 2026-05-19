package Pieces;

public class Tyler extends Piece{
    public Tyler(String color, int row, int col) {
        super("", row, col);
    }
    public int getValue() {
        return 0;
    }
    public String type() {
        return "Tyler";
    }
    public boolean isLegal(int row, int col, Piece[][] board) {
        return true;
    }
}
