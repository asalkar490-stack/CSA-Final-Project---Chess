package Engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * This class handles all communication between our Java chess game and the Stockfish engine.
 * Stockfish is a separate program that runs in the background and figures out the best chess moves.
 * We talk to it by sending text commands back and forth, which is called the UCI protocol.
 */
public class StockfishEngine {

    /**
     * These are the three difficulty levels the player can choose from.
     * Each one has two numbers: skillLevel controls how often Stockfish
     * makes mistakes on purpose 0 = lots of mistakes, 20 = perfect play
     * depth controls how many moves ahead it looks before deciding.
     * Lower depth means it thinks faster but plays worse.
     */
    public enum Difficulty {
        EASY      (1,  1),
        NORMAL    (2, 2),
        HARD      (5, 5);

        public final int skillLevel;
        public final int depth;

        Difficulty(int skillLevel, int depth) {
            this.skillLevel = skillLevel;
            this.depth = depth;
        }
    }

    private Process                   process;
    private PrintWriter               writer;
    private boolean                   ready = false;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private Thread                    readerThread;

    /**
     * This is the file path to the Stockfish binary on the computer.
     * If you move the Stockfish file somewhere else you need to update this.
     */
    public static final String DEFAULT_PATH =
        "/Users/pranava/Downloads/stockfish/stockfish-macos-m1-apple-silicon";

    /**
     * Starts up the Stockfish engine by launching it as a background process.
     * Once it's running, we open a reader and writer so we can send and receive text.
     * We also start a separate thread that constantly listens for output from Stockfish
     * and puts each line into a queue so nothing gets missed.
     * At the end we do the UCI handshake which is basically just saying hello and waiting
     * for Stockfish to confirm it's ready before we start sending positions.
     *
     * @param path the file path to the Stockfish executable
     * @return true if the engine started successfully, false if something went wrong
     */
    public boolean start(String path) {
        try {
            ProcessBuilder pb = new ProcessBuilder(path);
            pb.redirectErrorStream(true);
            process = pb.start();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            writer = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream())), true);

            readerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) queue.put(line);
                } catch (Exception ignored) {}
            }, "sf-reader");
            readerThread.setDaemon(true);
            readerThread.start();

            send("uci");
            if (!waitFor("uciok", 5000)) return false;
            send("ucinewgame");
            send("isready");
            if (!waitFor("readyok", 5000)) return false;

            ready = true;
            return true;
        } catch (Exception e) {
            System.err.println("Stockfish start failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns whether the engine has successfully started and is ready to receive positions.
     * We check this before trying to ask for a move so we don't crash if something went wrong.
     *
     * @return true if the engine is running and ready, false otherwise
     */
    public boolean isReady() { return ready; }

    /**
     * Tells Stockfish how hard to play by setting the Skill Level option.
     * This uses the skillLevel number from whichever Difficulty was selected.
     *  only do this after confirming the engine is ready.
     *
     * @param d the difficulty level to set
     */
    public void setDifficulty(Difficulty d) {
        if (!ready) return;
        send("setoption name Skill Level value " + d.skillLevel);
    }

    /**
     * Asks Stockfish for the best move in the current board position.
     * We send the position as a FEN string and tell it how deep to search.
     * Then we just loop through the queue waiting for a line that starts with "bestmove".
     * That line looks something like "bestmove e2e4 ponder d7d5" so we grab the second word.
     * This method blocks the thread it runs on, so we always call it from a background thread
     * so the game window doesn't freeze while Stockfish is thinking.
     *
     * @param fen the current board position in FEN notation
     * @param d   the difficulty level, which determines how deep Stockfish searches
     * @return the best move as a UCI string like "e2e4", or null if nothing came back
     */
    public String getBestMove(String fen, Difficulty d) {
        if (!ready) return null;
        queue.clear();
        send("position fen " + fen);
        send("go depth " + d.depth);

        long deadline = System.currentTimeMillis() + 30_000;
        while (System.currentTimeMillis() < deadline) {
            try {
                String line = queue.poll(500, TimeUnit.MILLISECONDS);
                if (line == null) continue;
                System.out.println("[SF] " + line);
                if (line.startsWith("bestmove")) {
                    String[] p = line.trim().split("\\s+");
                    return (p.length >= 2 && !p[1].equals("(none)")) ? p[1] : null;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return null;
    }

    /**
     * Resets the engine's internal state so it starts fresh for a new game.
     * We clear the queue first so there are no leftover lines from the previous game,
     * then send ucinewgame to let Stockfish know the history no longer applies.
     */
    public void newGame() {
        if (!ready) return;
        queue.clear();
        send("ucinewgame");
        send("isready");
        waitFor("readyok", 3000);
    }

    /**
     * Shuts the engine down cleanly by sending the quit command and then
     * force-killing the process just in case it doesn't stop on its own.
     * We also set ready to false right away so nothing tries to use it after this.
     */
    public void stop() {
        ready = false;
        try { send("quit"); } catch (Exception ignored) {}
        if (process != null) process.destroyForcibly();
    }

    /**
     * Sends a single line of text to Stockfish through the writer stream.
     * Every command we send -- like "uci", "go depth 5", "position fen ..." --
     * goes through this method.
     *
     * @param cmd the UCI command string to send
     */
    private void send(String cmd) {
        if (writer != null) { writer.println(cmd); writer.flush(); }
    }

    /**
     * Waits for a specific response from Stockfish, up to a time limit.
     * We pull lines out of the queue one at a time and check if they match
     * what we're looking for. If the time runs out before we see it, we return false.
     * We use this during startup to confirm Stockfish is actually responding
     * before we try to use it.
     *
     * @param token the exact response string we're waiting for, like "uciok" or "readyok"
     * @param ms    the maximum number of milliseconds to wait before giving up
     * @return true if we got the expected response in time, false if we timed out
     */
    private boolean waitFor(String token, long ms) {
        long deadline = System.currentTimeMillis() + ms;
        while (System.currentTimeMillis() < deadline) {
            try {
                String line = queue.poll(deadline - System.currentTimeMillis(),
                                         TimeUnit.MILLISECONDS);
                if (line == null) break;
                System.out.println("[SF init] " + line);
                if (line.trim().equals(token)) return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }
}