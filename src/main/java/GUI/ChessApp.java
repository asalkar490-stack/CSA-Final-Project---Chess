package GUI;

import Board.Board;
import Pieces.Piece;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ChessApp extends Application {

    // ── Board colours ──────────────────────────────────────────────
    private static final Color LIGHT            = Color.web("#F0D9B5");
    private static final Color DARK             = Color.web("#B58863");
    private static final Color SELECTED_LIGHT   = Color.web("#F6F669");
    private static final Color SELECTED_DARK    = Color.web("#BACA2B");
    private static final Color MOVE_DOT         = Color.web("#000000", 0.20);
    private static final Color CAPTURE_RING     = Color.web("#000000", 0.20);

    // ── Layout ─────────────────────────────────────────────────────
    private static final int BOARD_TILES = 8;

    // ── State ──────────────────────────────────────────────────────
    private Board         board;
    private GridPane      grid;
    private DoubleBinding tileSize;
    private Scene         scene;

    private Piece      selectedPiece = null;
    private int        selectedRow   = -1;
    private int        selectedCol   = -1;
    private List<int[]> validMoves   = new ArrayList<>();
    private String     currentTurn   = "White";

    // ── JavaFX entry point ─────────────────────────────────────────
    @Override
    public void start(Stage stage) {
        board = new Board("playaswhite");
        grid  = new GridPane();
        grid.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(grid);
        root.setStyle("-fx-background-color: #1a1a1a;");

        scene = new Scene(root, 640, 640);

        // tileSize = shortest window dimension / 8
        tileSize = Bindings.createDoubleBinding(
            () -> Math.min(scene.getWidth(), scene.getHeight()) / BOARD_TILES,
            scene.widthProperty(), scene.heightProperty()
        );

        drawBoard();

        stage.setTitle("Chess  –  " + currentTurn + " to move");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    // ── Render the full board ──────────────────────────────────────
    private void drawBoard() {
        grid.getChildren().clear();
        Piece[][] pieces = board.getBoard();

        for (int row = 0; row < BOARD_TILES; row++) {
            for (int col = 0; col < BOARD_TILES; col++) {

                boolean isLight   = (row + col) % 2 == 0;
                boolean isSelCell = (row == selectedRow && col == selectedCol);
                boolean isMoveCell = isInValidMoves(row, col);
                Piece   piece     = pieces[row][col];

                StackPane square = new StackPane();

                // ── 1. Base tile colour ─────────────────────────
                Rectangle tile = new Rectangle();
                tile.widthProperty().bind(tileSize);
                tile.heightProperty().bind(tileSize);

                if (isSelCell) {
                    tile.setFill(isLight ? SELECTED_LIGHT : SELECTED_DARK);
                } else {
                    tile.setFill(isLight ? LIGHT : DARK);
                }
                square.getChildren().add(tile);

                // ── 2. Piece image ──────────────────────────────
                if (piece != null) {
                    ImageView iv = new ImageView();
                    // Background-load the PNG – true = load in background thread
                    iv.setImage(new Image(Piece.class.getResourceAsStream(piece.getImagePath()), 0, 0, true, true));
                    iv.fitWidthProperty().bind(tileSize.multiply(0.82));
                    iv.fitHeightProperty().bind(tileSize.multiply(0.82));
                    iv.setPreserveRatio(true);
                    iv.setSmooth(true);
                    square.getChildren().add(iv);
                }

                // ── 3. Move / capture highlight overlay ─────────
                if (isMoveCell) {
                    boolean isCapture = (piece != null && !piece.getColor().equals(currentTurn));

                    if (isCapture) {
                        // Ring around enemy piece to show it can be captured
                        Circle ring = new Circle();
                        ring.radiusProperty().bind(tileSize.divide(2));
                        ring.setFill(Color.TRANSPARENT);
                        ring.setStroke(CAPTURE_RING);
                        ring.strokeWidthProperty().bind(tileSize.divide(8));
                        square.getChildren().add(ring);
                    } else {
                        // Filled dot for empty square
                        Circle dot = new Circle();
                        dot.radiusProperty().bind(tileSize.divide(5.5));
                        dot.setFill(MOVE_DOT);
                        square.getChildren().add(dot);
                    }
                }

                // ── 4. Click handler ────────────────────────────
                final int r = row, c = col;
                square.setOnMouseClicked(e -> handleClick(r, c));
                square.setCursor(Cursor.HAND);

                grid.add(square, col, row);
            }
        }
    }

    // ── Click logic ────────────────────────────────────────────────
    private void handleClick(int row, int col) {
        Piece[][] pieces = board.getBoard();
        Piece     target = pieces[row][col];

        if (selectedPiece == null) {
            // Nothing selected – try to select a piece belonging to the current player
            if (target != null && target.getColor().equals(currentTurn)) {
                select(target, row, col);
            }

        } else {
            if (isInValidMoves(row, col)) {
                // ─ Execute the move ─
                executeMove(row, col);

            } else if (target != null && target.getColor().equals(currentTurn)) {
                // ─ Switch selection to another own piece ─
                select(target, row, col);

            } else {
                // ─ Clicked on empty square or enemy with no valid move → deselect ─
                deselect();
            }
        }
    }

    private void select(Piece piece, int row, int col) {
        selectedPiece = piece;
        selectedRow   = row;
        selectedCol   = col;
        computeValidMoves();
        drawBoard();
    }

    private void executeMove(int toRow, int toCol) {
        Piece[][] pieces = board.getBoard();

        // Move piece on the board array
        pieces[selectedRow][selectedCol] = null;
        pieces[toRow][toCol]            = selectedPiece;

        // Update piece's internal position
        selectedPiece.setRow(toRow);
        selectedPiece.setCol(toCol);
        selectedPiece.moved();

        // Switch turn
        currentTurn = currentTurn.equals("White") ? "Black" : "White";

        deselect();   // also calls drawBoard()
    }

    private void deselect() {
        selectedPiece = null;
        selectedRow   = -1;
        selectedCol   = -1;
        validMoves.clear();
        drawBoard();
    }

    // ── Compute all legal destination squares for selected piece ───
    private void computeValidMoves() {
    validMoves.clear();
    Piece[][] pieces = board.getBoard();

    for (int r = 0; r < BOARD_TILES; r++) {
        for (int c = 0; c < BOARD_TILES; c++) {
            if (r == selectedRow && c == selectedCol) continue;
            try {
                if (selectedPiece.isLegal(r, c, pieces)
                        && (pieces[r][c] == null || !pieces[r][c].getColor().equals(currentTurn))) // ← add this
                    validMoves.add(new int[]{ r, c });
            } catch (Exception ignored) {}
        }
    }
    }

    private boolean isInValidMoves(int row, int col) {
        for (int[] move : validMoves)
            if (move[0] == row && move[1] == col) return true;
        return false;
    }

    // ── Main ────────────────────────────────────────────────────────
    public static void main(String[] args) {
        launch(args);
    }
}
