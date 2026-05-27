package Engine;

import Pieces.Piece;

/**
 * This class converts our board state into a FEN string that Stockfish can read.
 * FEN (Forsyth-Edwards Notation) is basically the universal language that chess engines
 * use to describe a position. It packs everything about the board into one line of text.
 * We always assume the player is playing White and the bot is playing Black,
 * so the board layout stays the same the whole game.
 */
public class FenConverter {

    /**
     * Takes our 8x8 board array and turns it into a valid FEN string.
     * The FEN string has multiple parts: first the piece positions row by row,
     * then whose turn it is, then castling rights, then en passant, then move counters.
     * We loop through every square on the board and either write a piece letter
     * or count up empty squares. Each row is separated by a slash
     * @param board     the current 8x8 grid of pieces, where null means empty
     * @param whiteTurn true if it's White's turn, false if it's Black's turn
     * @return a complete FEN string representing the current board position
     */
    public static String toFEN(Piece[][] board, boolean whiteTurn) {
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < 8; r++) {
            int empty = 0;
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null) {
                    empty++;
                } else {
                    if (empty > 0) { sb.append(empty); empty = 0; }
                    sb.append(fenChar(p));
                }
            }
            if (empty > 0) sb.append(empty);
            if (r < 7) sb.append('/');
        }

        sb.append(whiteTurn ? " w " : " b ");
        sb.append("KQkq ");
        sb.append("0 1");

        return sb.toString();
    }

    /**
     * Converts a single piece into its FEN character.
     * In FEN notation, uppercase letters are White pieces and lowercase are Black.
     * So a White Queen is 'Q' and a Black queen is 'q', a White Knight is 'N', and so on.
     * We get the piece type as a string from our Piece class and match it to the right letter.
     *
     * @param p the piece to convert
     * @return the FEN character representing that piece
     */
    private static char fenChar(Piece p) {
        char c = switch (p.getType()) {
            case "King"   -> 'k';
            case "Queen"  -> 'q';
            case "Rook"   -> 'r';
            case "Bishop" -> 'b';
            case "Knight" -> 'n';
            default       -> 'p';
        };
        return p.getColor().equals("White") ? Character.toUpperCase(c) : c;
    }
}