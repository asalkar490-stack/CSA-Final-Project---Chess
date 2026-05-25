package GUI;

import Board.Board;
import Pieces.Piece;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ChessApp extends Application {

    // ── Board colours ──────────────────────────────────────────────
    private static final Color LIGHT          = Color.web("#F0D9B5");
    private static final Color DARK           = Color.web("#B58863");
    private static final Color SELECTED_LIGHT = Color.web("#F6F669");
    private static final Color SELECTED_DARK  = Color.web("#BACA2B");
    private static final Color MOVE_DOT       = Color.web("#000000", 0.20);
    private static final Color CAPTURE_RING   = Color.web("#000000", 0.20);
    private static final int   BOARD_TILES    = 8;

    // ── State ──────────────────────────────────────────────────────
    private Board          board;
    private GridPane       grid;
    private DoubleBinding  tileSize;
    private Scene          scene;
    private StackPane      root;
    private Piece          selectedPiece = null;
    private int            selectedRow   = -1;
    private int            selectedCol   = -1;
    private List<int[]>    validMoves    = new ArrayList<>();
    private String         currentTurn   = "White";

    // ── Entry point ────────────────────────────────────────────────
    @Override
    public void start(Stage stage) {
        root  = new StackPane();
        scene = new Scene(root, 800, 550);
        stage.setTitle("Project Chess");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        showMenu();
    }

    // ── MENU SCREEN ────────────────────────────────────────────────
    /**
     * Builds and displays the main menu with a background image,
     * game title, and colour-selection buttons.
     * Teammate hook: add difficulty buttons (Easy / Medium / Hard)
     * inside the {@code VBox menu} before the colour buttons.
     */
    private void showMenu() {
        // Background image
        ImageView bg = new ImageView(new Image(
            Piece.class.getResourceAsStream("/images/menu_bg.png")));
        bg.fitWidthProperty().bind(scene.widthProperty());
        bg.fitHeightProperty().bind(scene.heightProperty());
        bg.setPreserveRatio(false);

        // Dark overlay so text is readable
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(scene.widthProperty());
        overlay.heightProperty().bind(scene.heightProperty());
        overlay.setFill(Color.web("#000000", 0.55));

        // Title
        Label title = new Label("Project Chess");
        title.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; "
                     + "-fx-text-fill: #F0D9B5; -fx-font-family: Georgia;");

        // Colour buttons
        Button playWhite = menuButton("Play as White");
        Button playBlack = menuButton("Play as Black");
        playWhite.setOnAction(e -> startGame("playaswhite", "White"));
        playBlack.setOnAction(e -> startGame("playasblack", "Black"));

        // Layout
        VBox menu = new VBox(24, title, playWhite, playBlack);
        menu.setAlignment(Pos.CENTER);

        root.getChildren().setAll(bg, overlay, menu);
    }

    /** Shared button style for the menu. */
    private Button menuButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-font-size: 18px; -fx-font-family: Georgia; "
                 + "-fx-background-color: #B58863; -fx-text-fill: #F0D9B5; "
                 + "-fx-padding: 10 40; -fx-background-radius: 6;");
        b.setCursor(Cursor.HAND);
        b.setOnMouseEntered(e -> b.setStyle(b.getStyle()
                .replace("#B58863", "#8B6343")));
        b.setOnMouseExited(e -> b.setStyle(b.getStyle()
                .replace("#8B6343", "#B58863")));
        return b;
    }

    // ── GAME START ─────────────────────────────────────────────────
    /**
     * Initialises the board for the chosen colour and switches
     * the root to the game view.
     *
     * @param mode        {@code "playaswhite"} or {@code "playasblack"}
     * @param firstTurn   which colour moves first
     */
    private void startGame(String mode, String firstTurn) {
        board       = new Board(mode);
        currentTurn = firstTurn;
        grid        = new GridPane();
        grid.setAlignment(Pos.CENTER);

        StackPane gameRoot = new StackPane(grid);
        gameRoot.setStyle("-fx-background-color: #1a1a1a;");

        tileSize = Bindings.createDoubleBinding(
            () -> Math.min(scene.getWidth(), scene.getHeight()) / BOARD_TILES,
            scene.widthProperty(), scene.heightProperty()
        );

        root.getChildren().setAll(gameRoot);
        drawBoard();
    }

    // ── DRAW BOARD ─────────────────────────────────────────────────
    /**
     * Clears and redraws every square on the board, including
     * piece images and move-highlight overlays.
     */
    private void drawBoard() {
        grid.getChildren().clear();
        Piece[][] pieces = board.getBoard();

        for (int row = 0; row < BOARD_TILES; row++) {
            for (int col = 0; col < BOARD_TILES; col++) {
                boolean isLight    = (row + col) % 2 == 0;
                boolean isSelected = (row == selectedRow && col == selectedCol);
                boolean isMoveCell = isInValidMoves(row, col);
                Piece   piece      = pieces[row][col];

                StackPane square = new StackPane();

                // 1. Tile
                Rectangle tile = new Rectangle();
                tile.widthProperty().bind(tileSize);
                tile.heightProperty().bind(tileSize);
                tile.setFill(isSelected
                    ? (isLight ? SELECTED_LIGHT : SELECTED_DARK)
                    : (isLight ? LIGHT : DARK));
                square.getChildren().add(tile);

                // 2. Piece image
                if (piece != null) {
                    ImageView iv = new ImageView();
                    iv.setImage(new Image(
                        Piece.class.getResourceAsStream(piece.getImagePath()), 0, 0, true, true));
                    iv.fitWidthProperty().bind(tileSize.multiply(0.82));
                    iv.fitHeightProperty().bind(tileSize.multiply(0.82));
                    iv.setPreserveRatio(true);
                    iv.setSmooth(true);
                    square.getChildren().add(iv);
                }

                // 3. Move highlight
                if (isMoveCell) {
                    if (piece != null && !piece.getColor().equals(currentTurn)) {
                        Circle ring = new Circle();
                        ring.radiusProperty().bind(tileSize.divide(2));
                        ring.setFill(Color.TRANSPARENT);
                        ring.setStroke(CAPTURE_RING);
                        ring.strokeWidthProperty().bind(tileSize.divide(8));
                        square.getChildren().add(ring);
                    } else {
                        Circle dot = new Circle();
                        dot.radiusProperty().bind(tileSize.divide(5.5));
                        dot.setFill(MOVE_DOT);
                        square.getChildren().add(dot);
                    }
                }

                // 4. Click
                final int r = row, c = col;
                square.setOnMouseClicked(e -> handleClick(r, c));
                square.setCursor(Cursor.HAND);
                grid.add(square, col, row);
            }
        }
    }

    // ── CLICK LOGIC ────────────────────────────────────────────────
    /**
     * Handles all square click events:
     * selects a piece, moves it, or deselects.
     *
     * @param row clicked row
     * @param col clicked column
     */
    private void handleClick(int row, int col) {
        Piece[][] pieces = board.getBoard();
        Piece     target = pieces[row][col];

        if (selectedPiece == null) {
            if (target != null && target.getColor().equals(currentTurn))
                select(target, row, col);
        } else {
            if (isInValidMoves(row, col))
                executeMove(row, col);
            else if (target != null && target.getColor().equals(currentTurn))
                select(target, row, col);
            else
                deselect();
        }
    }

    private void select(Piece piece, int row, int col) {
        selectedPiece = piece;
        selectedRow   = row;
        selectedCol   = col;
        computeValidMoves();
        drawBoard();
    }

    /**
     * Moves the selected piece to the target square,
     * updates its internal position, and switches turns.
     *
     * @param toRow destination row
     * @param toCol destination column
     */
    private void executeMove(int toRow, int toCol) {
        Piece[][] pieces = board.getBoard();
        pieces[selectedRow][selectedCol] = null;
        pieces[toRow][toCol]             = selectedPiece;
        selectedPiece.setRow(toRow);
        selectedPiece.setCol(toCol);
        selectedPiece.moved();
        currentTurn = currentTurn.equals("White") ? "Black" : "White";
        deselect();
    }

    private void deselect() {
        selectedPiece = null;
        selectedRow   = -1;
        selectedCol   = -1;
        validMoves.clear();
        drawBoard();
    }

    /**
     * Populates {@code validMoves} with every square the selected
     * piece can legally move to, excluding friendly-occupied squares.
     */
    private void computeValidMoves() {
        validMoves.clear();
        Piece[][] pieces = board.getBoard();
        for (int r = 0; r < BOARD_TILES; r++) {
            for (int c = 0; c < BOARD_TILES; c++) {
                if (r == selectedRow && c == selectedCol) continue;
                try {
                    if (selectedPiece.isLegal(r, c, pieces)
                            && (pieces[r][c] == null
                                || !pieces[r][c].getColor().equals(currentTurn)))
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

    public static void main(String[] args) { launch(args); }
}