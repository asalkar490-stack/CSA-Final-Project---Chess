package GUI;

import Board.Board;
import Engine.FenConverter;
import Engine.StockfishEngine;
import Engine.StockfishEngine.Difficulty;
import Game.Game;
import Pieces.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Chess — integrated with Stockfish AI.
 * Bot always plays Black. Human always plays White.
 * Menu adds: Two Player | vs Bot (Easy / Normal / Hard)
 */
public class ChessApp extends Application {

    // ── IMPORTANT: set your Stockfish binary path here ───────────
    private static final String STOCKFISH_PATH =
        "/Users/pranava/Downloads/stockfish/stockfish-macos-m1-apple-silicon"; 

    // ── Board colours (matching partners' original) ───────────────
    private static final Color LIGHT          = Color.web("#F0D9B5");
    private static final Color DARK           = Color.web("#B58863");
    private static final Color SELECTED_LIGHT = Color.web("#F6F669");
    private static final Color SELECTED_DARK  = Color.web("#BACA2B");
    private static final Color MOVE_DOT       = Color.web("#000000", 0.20);
    private static final int   BOARD_TILES    = 8;

    // ── State ─────────────────────────────────────────────────────
    private Stage          stage;
    private Scene          scene;
    private StackPane      root;
    private Game           game;
    private GridPane       grid;
    private DoubleBinding  tileSize;

    private Piece          selectedPiece = null;
    private int            selectedRow   = -1;
    private int            selectedCol   = -1;
    private List<int[]>    validMoves    = new ArrayList<>();

    // ── Bot state ─────────────────────────────────────────────────
    private boolean           botMode      = false;
    private boolean           botThinking  = false;
    private Difficulty        difficulty   = Difficulty.NORMAL;
    private StockfishEngine   engine;

    // ── Status label shown above board ───────────────────────────
    private Label statusLabel;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        root  = new StackPane();
        scene = new Scene(root, 800, 600);
        stage.setTitle("Project Chess");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        showMenu();
    }

    // ═══════════════════════════════════════════════════════════════
    // MENU
    // ═══════════════════════════════════════════════════════════════

    private void showMenu() {
        // Stop any running engine
        if (engine != null) { engine.stop(); engine = null; }

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

        // ── Mode separator label
        Label modeLabel = new Label("TWO PLAYERS");
        modeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(240,217,181,0.55);"
                         + "-fx-font-family: Georgia; -fx-font-style: italic;");

        Button btnTwo = menuButton("Play as White  (2 Players)");
        btnTwo.setOnAction(e -> { botMode = false; startGame(true); });

        // ── Bot separator
        Label botLabel = new Label("VS BOT  (you play White, bot plays Black)");
        botLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(240,217,181,0.55);"
                        + "-fx-font-family: Georgia; -fx-font-style: italic;");

        Button btnEasy   = menuButton("Easy Bot");
        Button btnNormal = menuButton("Normal Bot");
        Button btnHard   = menuButton("Hard Bot");

        btnEasy.setOnAction(e   -> { botMode = true; difficulty = Difficulty.EASY;   startGame(true); });
        btnNormal.setOnAction(e -> { botMode = true; difficulty = Difficulty.NORMAL; startGame(true); });
        btnHard.setOnAction(e   -> { botMode = true; difficulty = Difficulty.HARD;   startGame(true); });

        VBox menu = new VBox(14,
            title,
            modeLabel, btnTwo,
            botLabel, btnEasy, btnNormal, btnHard
        );
        menu.setAlignment(Pos.CENTER);
        root.getChildren().setAll(bg, overlay, menu);
    }

    private Button menuButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-font-size: 16px; -fx-font-family: Georgia; "
                 + "-fx-background-color: #B58863; -fx-text-fill: #F0D9B5; "
                 + "-fx-padding: 10 36; -fx-background-radius: 6; -fx-min-width: 280;");
        b.setCursor(Cursor.HAND);
        b.setOnMouseEntered(e -> b.setStyle(b.getStyle().replace("#B58863", "#8B6343")));
        b.setOnMouseExited(e  -> b.setStyle(b.getStyle().replace("#8B6343", "#B58863")));
        return b;
    }

    // ═══════════════════════════════════════════════════════════════
    // GAME SETUP
    // ═══════════════════════════════════════════════════════════════

    private void startGame(boolean isWhiteStart) {
        game = new Game(new Board("playaswhite"), isWhiteStart);
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        tileSize = Bindings.createDoubleBinding(
            () -> Math.min(scene.getWidth(), scene.getHeight() - 60) / BOARD_TILES,
            scene.widthProperty(), scene.heightProperty()
        );

        // Status bar
        statusLabel = new Label(botMode
            ? "Your turn (White)  |  Bot: " + difficulty.name()
            : "White's turn");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #F0D9B5; "
                           + "-fx-font-family: Georgia; -fx-font-style: italic;");

        // Top bar with status + buttons
        Button newGameBtn = smallButton("New Game");
        Button menuBtn    = smallButton("Main Menu");
        newGameBtn.setOnAction(e -> startGame(isWhiteStart));
        menuBtn.setOnAction(e    -> showMenu());

        HBox topBar = new HBox(16, statusLabel, newGameBtn, menuBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(8, 16, 8, 16));
        topBar.setStyle("-fx-background-color: #1a1a1a;");

        VBox layout = new VBox(0, topBar, grid);
        layout.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(grid, Priority.ALWAYS);

        root.getChildren().setAll(layout);
        drawBoard();

        // Init bot on background thread
        if (botMode) {
            statusLabel.setText("Starting engine…");
            engine = new StockfishEngine();
            Thread t = new Thread(() -> {
                boolean ok = engine.start(STOCKFISH_PATH);
                Platform.runLater(() -> {
                    if (ok) {
                        engine.setDifficulty(difficulty);
                        statusLabel.setText("Your turn (White)  |  Bot: " + difficulty.name());
                    } else {
                        statusLabel.setText("⚠ Stockfish not found — edit STOCKFISH_PATH in ChessApp.java");
                    }
                });
            }, "sf-init");
            t.setDaemon(true);
            t.start();
        }
    }

    private Button smallButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-font-size: 13px; -fx-font-family: Georgia; "
                 + "-fx-background-color: #B58863; -fx-text-fill: #F0D9B5; "
                 + "-fx-padding: 5 18; -fx-background-radius: 4;");
        b.setCursor(Cursor.HAND);
        b.setOnMouseEntered(e -> b.setStyle(b.getStyle().replace("#B58863", "#8B6343")));
        b.setOnMouseExited(e  -> b.setStyle(b.getStyle().replace("#8B6343", "#B58863")));
        return b;
    }

    // ═══════════════════════════════════════════════════════════════
    // BOARD RENDERING
    // ═══════════════════════════════════════════════════════════════

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

                Rectangle tile = new Rectangle();
                tile.widthProperty().bind(tileSize);
                tile.heightProperty().bind(tileSize);
                tile.setFill(isSelected
                    ? (isLight ? SELECTED_LIGHT : SELECTED_DARK)
                    : (isLight ? LIGHT : DARK));
                square.getChildren().add(tile);

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

                if (isMoveCell && piece == null) {
                    Circle dot = new Circle();
                    dot.radiusProperty().bind(tileSize.divide(5.5));
                    dot.setFill(MOVE_DOT);
                    square.getChildren().add(dot);
                }

                // Disable clicks while bot is thinking
                final int r = row, c = col;
                square.setOnMouseClicked(e -> { if (!botThinking) handleClick(r, c); });
                square.setCursor(botThinking ? Cursor.WAIT : Cursor.HAND);
                grid.add(square, col, row);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // CLICK / MOVE LOGIC
    // ═══════════════════════════════════════════════════════════════

    private void handleClick(int row, int col) {
        Piece[][] pieces = game.getBoard().getBoard();
        Piece target = pieces[row][col];

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

    private void select(Piece piece, int row, int col) {
        selectedPiece = piece;
        selectedRow   = row;
        selectedCol   = col;
        computeValidMoves();
        drawBoard();
    }

    private void executeMove(int toRow, int toCol) {
        Piece[][] pieces = game.getBoard().getBoard();
        pieces[selectedRow][selectedCol] = null;
        pieces[toRow][toCol]             = selectedPiece;
        selectedPiece.setRow(toRow);
        selectedPiece.setCol(toCol);
        selectedPiece.moved();

        if (selectedPiece instanceof Pawn && ((Pawn) selectedPiece).canPromote(selectedPiece.getColor())) {
            Pawn pawn = (Pawn) selectedPiece;
            game.updateTurn();
            deselect();
            showPromotionMenu(pawn, toRow, toCol, () -> afterHumanMove());
        } else {
            game.updateTurn();
            deselect();
            afterHumanMove();
        }
    }

    /** Called after human finishes their move (including promotion). */
    private void afterHumanMove() {
        // Check game state
        if (checkGameOver()) return;

        if (botMode && game.getCurrentPlayersColor().equals("Black")) {
            triggerBotMove();
        } else {
            statusLabel.setText(game.getCurrentPlayersColor() + "'s turn"
                + (botMode ? "  |  Bot: " + difficulty.name() : ""));
        }
    }

    private void deselect() {
        selectedPiece = null;
        selectedRow   = -1;
        selectedCol   = -1;
        validMoves.clear();
        drawBoard();
    }

    // ═══════════════════════════════════════════════════════════════
    // BOT MOVE
    // ═══════════════════════════════════════════════════════════════

    private void triggerBotMove() {
        if (engine == null || !engine.isReady()) {
            statusLabel.setText("⚠ Engine not ready");
            return;
        }

        botThinking = true;
        statusLabel.setText("⏳ Bot is thinking…");
        drawBoard(); // re-render with WAIT cursor

        String fen = FenConverter.toFEN(game.getBoard().getBoard(), false); // black's turn

        Thread t = new Thread(() -> {
            String uciMove = engine.getBestMove(fen, difficulty);
            Platform.runLater(() -> {
                botThinking = false;

                if (uciMove == null) {
                    statusLabel.setText("Bot has no moves — you win!");
                    return;
                }

                // Parse UCI move: e.g. "e7e5" or "e7e8q"
                int fromCol = uciMove.charAt(0) - 'a';
                int fromRow = 8 - Character.getNumericValue(uciMove.charAt(1));
                int toCol   = uciMove.charAt(2) - 'a';
                int toRow   = 8 - Character.getNumericValue(uciMove.charAt(3));

                Piece[][] pieces = game.getBoard().getBoard();
                Piece moving = pieces[fromRow][fromCol];

                if (moving == null) {
                    statusLabel.setText("Bot move error — piece not found");
                    drawBoard();
                    return;
                }

                pieces[fromRow][fromCol] = null;
                pieces[toRow][toCol]     = moving;
                moving.setRow(toRow);
                moving.setCol(toCol);
                moving.moved();

                // Auto-promote bot pawn to Queen
                if (moving instanceof Pawn && ((Pawn) moving).canPromote(moving.getColor())) {
                    pieces[toRow][toCol] = new Queen(moving.getColor(), toRow, toCol);
                }

                game.updateTurn();
                drawBoard();

                if (!checkGameOver()) {
                    statusLabel.setText("Your turn (White)  |  Bot: " + difficulty.name());
                }
            });
        }, "sf-move");
        t.setDaemon(true);
        t.start();
    }

    // ═══════════════════════════════════════════════════════════════
    // VALID MOVES
    // ═══════════════════════════════════════════════════════════════

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

    private boolean isInValidMoves(int row, int col) {
        for (int[] move : validMoves)
            if (move[0] == row && move[1] == col) return true;
        return false;
    }

    // ═══════════════════════════════════════════════════════════════
    // GAME OVER CHECK
    // ═══════════════════════════════════════════════════════════════

    private boolean checkGameOver() {
        try {
            if (game.isCheckmate()) {
                String winner = game.getCurrentPlayersColor().equals("White") ? "Black" : "White";
                statusLabel.setText("♛ CHECKMATE — " + winner + " wins!");
                return true;
            }
            if (game.isCheck()) {
                statusLabel.setText("⚠ CHECK! — " + game.getCurrentPlayersColor() + "'s turn");
            }
        } catch (Exception ignored) {}
        return false;
    }

    // ═══════════════════════════════════════════════════════════════
    // PROMOTION
    // ═══════════════════════════════════════════════════════════════

    private void showPromotionMenu(Pawn pawn, int row, int col, Runnable onDone) {
        String   c     = pawn.getColor().equals("White") ? "w" : "b";
        String[] types = { "Q", "R", "B", "N" };

        HBox options = new HBox(8);
        options.setAlignment(Pos.CENTER);

        for (String t : types) {
            ImageView iv = new ImageView(new Image(
                Piece.class.getResourceAsStream("/images/" + c + t + ".png"), 0, 0, true, true));
            iv.setFitWidth(70); iv.setFitHeight(70);
            iv.setPreserveRatio(true);
            iv.setCursor(Cursor.HAND);

            iv.setOnMouseClicked(e -> {
                Piece newPiece = switch (t) {
                    case "Q" -> new Queen(pawn.getColor(), row, col);
                    case "R" -> new Rook(pawn.getColor(), row, col);
                    case "B" -> new Bishop(pawn.getColor(), row, col);
                    default  -> new Knight(pawn.getColor(), row, col);
                };
                game.getBoard().promote(row, col, newPiece);
                root.getChildren().remove(root.getChildren().size() - 1);
                drawBoard();
                onDone.run();
            });
            options.getChildren().add(iv);
        }

        Rectangle bg = new Rectangle(340, 100);
        bg.setFill(Color.web("#1a1a1a", 0.92));
        bg.setArcWidth(12); bg.setArcHeight(12);

        root.getChildren().add(new StackPane(bg, options));
    }

    public static void main(String[] args) { launch(args); }
}