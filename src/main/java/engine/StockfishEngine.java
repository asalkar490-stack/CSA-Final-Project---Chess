package Engine;

import java.io.*;
import java.util.concurrent.*;

/**
 * Wraps the Stockfish chess engine binary via the UCI protocol.
 * Must always be called from a background thread.
 */
public class StockfishEngine {

    public enum Difficulty {
        EASY      (1,  2),
        NORMAL    (5, 10),
        HARD      (20, 20);

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

    // ── SET YOUR STOCKFISH PATH HERE ──────────────────────────────
    public static final String DEFAULT_PATH =
        "/usr/local/bin/stockfish"; // change to your binary path

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

    public boolean isReady() { return ready; }

    public void setDifficulty(Difficulty d) {
        if (!ready) return;
        send("setoption name Skill Level value " + d.skillLevel);
    }

    /** Blocking — call only from a background thread. */
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

    public void newGame() {
        if (!ready) return;
        queue.clear();
        send("ucinewgame");
        send("isready");
        waitFor("readyok", 3000);
    }

    public void stop() {
        ready = false;
        try { send("quit"); } catch (Exception ignored) {}
        if (process != null) process.destroyForcibly();
    }

    private void send(String cmd) {
        if (writer != null) { writer.println(cmd); writer.flush(); }
    }

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