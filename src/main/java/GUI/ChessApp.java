package GUI;

import Board.Board;
import Game.Game;
import Pieces.Bishop;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Main JavaFX application class for Project Chess.
 * Manages the menu screen, game initialization, board rendering,
 * piece selection, movement, and pawn promotion.
 * Both "Play as White" and "Play as Black" use the same board layout
 * (White at bottom, Black at top). Playing as Black simply means
 * Black moves first.
 */
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
    private Game           game;
    private GridPane       grid;
    private DoubleBinding  tileSize;
    private Scene          scene;
    private StackPane      root;
    private Piece          selectedPiece = null;
    private int            selectedRow   = -1;
    private int            selectedCol   = -1;
    private List<int[]>    validMoves    = new ArrayList<>();

    /**
     * JavaFX entry point. Sets up the window and shows the main menu.
     * @param stage the primary stage provided by JavaFX
     */
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

    // ── MENU ───────────────────────────────────────────────────────

    /**
     * Builds and displays the main menu with a background image,
     * title, and colour-selection buttons.
     * Teammate hook: add difficulty buttons (Easy / Medium / Hard)
     * inside the VBox before the colour buttons.
     */
    private void showMenu() {
        ImageView bg = new ImageView(new Image(
            Piece.class.getResourceAsStream("/images/menu_bg.png")));
        bg.fitWidthProperty().bind(scene.widthProperty());
        bg.fitHeightProperty().bind(scene.heightProperty());
        bg.setPreserveRatio(false);

        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(scene.widthProperty());
        overlay.heightProperty().bind(scene.heightProperty());
        overlay.setFill(Color.web("#000000", 0.55));

        Label title = new Label("Project Chess");
        title.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; "
                     + "-fx-text-fill: #F0D9B5; -fx-font-family: Georgia;");

        Button playWhite = menuButton("Play as White");
        Button playBlack = menuButton("Play as Black");

        // Both modes use the same board layout (White at bottom, Black at top).
        // Playing as Black just means Black moves first (isWhiteStart = false).
        playWhite.setOnAction(e -> startGame(true));
        playBlack.setOnAction(e -> startGame(false));

        VBox menu = new VBox(24, title, playWhite, playBlack);
        menu.setAlignment(Pos.CENTER);
        root.getChildren().setAll(bg, overlay, menu);
    }

    /**
     * Creates a styled menu button with hover colour effects.
     * @param text the label to display on the button
     * @return a styled {@code Button}
     */
    private Button menuButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-font-size: 18px; -fx-font-family: Georgia; "
                 + "-fx-background-color: #B58863; -fx-text-fill: #F0D9B5; "
                 + "-fx-padding: 10 40; -fx-background-radius: 6;");
        b.setCursor(Cursor.HAND);
        b.setOnMouseEntered(e -> b.setStyle(b.getStyle().replace("#B58863", "#8B6343")));
        b.setOnMouseExited(e  -> b.setStyle(b.getStyle().replace("#8B6343", "#B58863")));
        return b;
    }

    // ── GAME SETUP ─────────────────────────────────────────────────

    /**
     * Initialises a new {@link Game} with the standard board layout and
     * switches the display from the menu to the game board.
     * White is always at the bottom. If {@code isWhiteStart} is false,
     * Black simply moves first.
     * @param isWhiteStart {@code true} if White moves first, {@code false} if Black moves first
     */
    private void startGame(boolean isWhiteStart) {
        game = new Game(new Board("playaswhite"), isWhiteStart);
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        tileSize = Bindings.createDoubleBinding(
            () -> Math.min(scene.getWidth(), scene.getHeight()) / BOARD_TILES,
            scene.widthProperty(), scene.heightProperty()
        );

        StackPane gameRoot = new StackPane(grid);
        gameRoot.setStyle("-fx-background-color: #1a1a1a;");
        root.getChildren().setAll(gameRoot);
        drawBoard();
    }

    // ── BOARD RENDERING ────────────────────────────────────────────

    /**
     * Clears and redraws every square on the board.
     * Each square is a layered {@code StackPane} containing:
     * a coloured tile, an optional piece image, and an optional move highlight.
     */
    private void drawBoard() {
        grid.getChildren().clear();
        Piece[][] pieces = game.getBoard().getBoard();

        for (int row = 0; row < BOARD_TILES; row++) {
            for (int col = 0; col < BOARD_TILES; col++) {
                boolean isLight    = (row + col) % 2 == 0;
                boolean isSelected = (row == selectedRow && col == selectedCol);
                boolean isMoveCell = isInValidMoves(row, col);
                Piece   piece      = pieces[row][col];

                StackPane square = new StackPane();

                // 1. Tile colour
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
                if (isMoveCell && piece == null) {
                Circle dot = new Circle();
                dot.radiusProperty().bind(tileSize.divide(5.5));
                dot.setFill(MOVE_DOT);
                square.getChildren().add(dot);
                    }

                // 4. Click handler
                final int r = row, c = col;
                square.setOnMouseClicked(e -> handleClick(r, c));
                square.setCursor(Cursor.HAND);
                grid.add(square, col, row);
            }
        }
    }

    // ── CLICK & MOVE LOGIC ─────────────────────────────────────────

    /**
     * Handles all square click events. Selects a piece if none is selected,
     * executes a move if the clicked square is a valid destination,
     * switches selection to another friendly piece, or deselects.
     * @param row the row of the clicked square
     * @param col the column of the clicked square
     */
    private void handleClick(int row, int col) {
        Piece[][] pieces = game.getBoard().getBoard();
        Piece     target = pieces[row][col];

        if (selectedPiece == null) {
            if (target != null && target.getColor().equals(game.getCurrentPlayersColor()))
                select(target, row, col);
        } else {
            if (isInValidMoves(row, col))
                executeMove(row, col);
            else if (target != null && target.getColor().equals(game.getCurrentPlayersColor()))
                select(target, row, col);
            else
                deselect();
        }
    }

    /**
     * Sets the given piece as the selected piece and computes its legal moves.
     * @param piece the piece to select
     * @param row   the row of the piece
     * @param col   the column of the piece
     */
    private void select(Piece piece, int row, int col) {
        selectedPiece = piece;
        selectedRow   = row;
        selectedCol   = col;
        computeValidMoves();
        drawBoard();
    }

    /**
     * Moves the selected piece to the target square, updates its internal
     * position, and advances the turn via {@link Game#updateTurn()}.
     * If the moved piece is a Pawn that can promote, the promotion menu
     * is shown before the turn switches.
     * @param toRow the destination row
     * @param toCol the destination column
     */
    private void executeMove(int toRow, int toCol) {
        Piece[][] pieces = game.getBoard().getBoard();
        pieces[selectedRow][selectedCol] = null;
        pieces[toRow][toCol]             = selectedPiece;
        selectedPiece.setRow(toRow);
        selectedPiece.setCol(toCol);
        selectedPiece.moved();

        // Check promotion BEFORE switching turn so pawn color is still current
        if (selectedPiece instanceof Pawn && ((Pawn) selectedPiece).canPromote(selectedPiece.getColor())) {
            Pawn pawn = (Pawn) selectedPiece;
            game.updateTurn();
            deselect();
            showPromotionMenu(pawn, toRow, toCol);
        } else {
            game.updateTurn();
            deselect();
        }
    }

    /**
     * Clears the current selection and valid moves, then redraws the board.
     */
    private void deselect() {
        selectedPiece = null;
        selectedRow   = -1;
        selectedCol   = -1;
        validMoves.clear();
        drawBoard();
    }

    // ── VALID MOVES ────────────────────────────────────────────────

    /**
     * Populates {@code validMoves} with every square the selected piece
     * can legally move to. Squares occupied by a friendly piece are excluded.
     */
    private void computeValidMoves() {
        validMoves.clear();
        Piece[][] pieces = game.getBoard().getBoard();
        for (int r = 0; r < BOARD_TILES; r++) {
            for (int c = 0; c < BOARD_TILES; c++) {
                if (r == selectedRow && c == selectedCol) continue;
                try {
                    if (selectedPiece.isLegal(r, c, pieces)
                            && (pieces[r][c] == null
                                || !pieces[r][c].getColor().equals(game.getCurrentPlayersColor())))
                        validMoves.add(new int[]{ r, c });
                } catch (Exception ignored) {}
            }
        }
    }

    /**
     * Returns whether the given square is in the current list of valid moves.
     * @param row the row to check
     * @param col the column to check
     * @return {@code true} if the square is a valid move destination
     */
    private boolean isInValidMoves(int row, int col) {
        for (int[] move : validMoves)
            if (move[0] == row && move[1] == col) return true;
        return false;
    }

    // ── PROMOTION ──────────────────────────────────────────────────

    /**
     * Displays a promotion picker overlay when a Pawn reaches the back rank.
     * Uses {@link Board#promote(int, int, Piece)} to replace the pawn on the board.
     * The image prefix is taken from the pawn's own color, not the current turn,
     * since the turn has already been switched by the time this is called.
     * @param pawn the Pawn eligible for promotion
     * @param row  the row the Pawn promoted on
     * @param col  the column the Pawn promoted on
     */
    private void showPromotionMenu(Pawn pawn, int row, int col) {
        String   c     = pawn.getColor().equals("White") ? "w" : "b";
        String[] types = { "Q", "R", "B", "N" };

        HBox options = new HBox(8);
        options.setAlignment(Pos.CENTER);

        for (String t : types) {
            ImageView iv = new ImageView(new Image(
                Piece.class.getResourceAsStream("/images/" + c + t + ".png"), 0, 0, true, true));
            iv.setFitWidth(70);
            iv.setFitHeight(70);
            iv.setPreserveRatio(true);
            iv.setCursor(Cursor.HAND);

            iv.setOnMouseClicked(e -> {
                Piece newPiece = switch (t) {
                    case "Q" -> new Queen(pawn.getColor(), row, col);
                    case "R" -> new Rook(pawn.getColor(), row, col);
                    case "B" -> new Bishop(pawn.getColor(), row, col);
                    default  -> new Knight(pawn.getColor(), row, col);
                };
                // Use Board's built-in promote method
                game.getBoard().promote(row, col, newPiece);
                root.getChildren().remove(root.getChildren().size() - 1);
                drawBoard();
            });
            options.getChildren().add(iv);
        }

        Rectangle bg = new Rectangle(340, 100);
        bg.setFill(Color.web("#1a1a1a", 0.92));
        bg.setArcWidth(12);
        bg.setArcHeight(12);

        root.getChildren().add(new StackPane(bg, options));
    }

    /**
     * Application entry point.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) { launch(args); }
}