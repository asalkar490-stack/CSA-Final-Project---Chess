package Engine;

import Pieces.Piece;

/**
 * Converts the Board (Piece[][]) into a FEN string
 * that Stockfish can understand.
 *
 *  assumes player is playing white
 *   row 0 = Black back rank, row 7 = White back rank.
 * Bot always plays Black.
 */
public class FenConverter {

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

        // Active color
        sb.append(whiteTurn ? " w " : " b ");

        // Castling — simplify: always allow unless king or rook has moved.
        sb.append("KQkq ");

        // En passant — not tracked, so "-"
        sb.append("- ");

        // Half-move clock and full move 
        sb.append("0 1");

        return sb.toString();
    }

    private static char fenChar(Piece p) {
        char c = switch (p.getType()) {
            case "King"   -> 'k';
            case "Queen"  -> 'q';
            case "Rook"   -> 'r';
            case "Bishop" -> 'b';
            case "Knight" -> 'n';
            default       -> 'p'; // Pawn
        };
        return p.getColor().equals("White") ? Character.toUpperCase(c) : c;
    }
}