package Engine;

import Pieces.*;

/**
 * Converts the partners' Board (Piece[][]) into a FEN string
 * that Stockfish can understand.
 *
 * Always assumes "play as white" layout:
 *   row 0 = Black back rank, row 7 = White back rank.
 * Bot always plays Black.
 */
public class FenConverter {

    /**
     * Build a FEN string from the current board state.
     *
     * @param board       the 8x8 piece grid
     * @param whiteTurn   true if it is White's turn
     * @return a valid FEN string
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

        // Active color
        sb.append(whiteTurn ? " w " : " b ");

        // Castling — simplify: always allow all unless we track hasMoved
        // (partners don't expose castling state, so we grant it permissively)
        sb.append("KQkq ");

        // En passant — not tracked, so "-"
        sb.append("- ");

        // Half-move clock and full move (simplified)
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