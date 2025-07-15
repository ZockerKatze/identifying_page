import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;

public class TetrisApplet extends Applet implements Runnable, KeyListener {
    private static final int ROWS = 20, COLS = 10, BLOCK = 24;
    private static final int SIDEBAR = 120;
    private static final int APPLET_WIDTH = 640, APPLET_HEIGHT = 480;
    private static final int PLAYFIELD_WIDTH = COLS * BLOCK;
    private static final int TOTAL_WIDTH = PLAYFIELD_WIDTH + SIDEBAR;
    private static final int ORIGIN_X = (APPLET_WIDTH - TOTAL_WIDTH) / 2;
    private static final int ORIGIN_Y = (APPLET_HEIGHT - ROWS * BLOCK) / 2;
    private int[][] board = new int[ROWS][COLS];
    private Color[] colors = {Color.BLACK, Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED};
    private int score = 0;
    private boolean running = true, gameOver = false;
    private Thread gameThread;
    private Queue<Integer> nextPieces = new LinkedList<>();
    private int curType, curRot, curX, curY;
    private AudioClip themeMusic;
    private boolean paused = false;

    // Tetromino shapes: [type][rotation][block][x/y]
    private static final int[][][][] SHAPES = {
        // I
        {{{0,1},{1,1},{2,1},{3,1}},{{2,0},{2,1},{2,2},{2,3}},{{0,2},{1,2},{2,2},{3,2}},{{1,0},{1,1},{1,2},{1,3}}},
        // J
        {{{0,0},{0,1},{1,1},{2,1}},{{1,0},{2,0},{1,1},{1,2}},{{0,1},{1,1},{2,1},{2,2}},{{1,0},{1,1},{1,2},{0,2}}},
        // L
        {{{2,0},{0,1},{1,1},{2,1}},{{1,0},{1,1},{1,2},{2,2}},{{0,1},{1,1},{2,1},{0,2}},{{0,0},{1,0},{1,1},{1,2}}},
        // O
        {{{1,0},{2,0},{1,1},{2,1}},{{1,0},{2,0},{1,1},{2,1}},{{1,0},{2,0},{1,1},{2,1}},{{1,0},{2,0},{1,1},{2,1}}},
        // S
        {{{1,0},{2,0},{0,1},{1,1}},{{1,0},{1,1},{2,1},{2,2}},{{1,1},{2,1},{0,2},{1,2}},{{0,0},{0,1},{1,1},{1,2}}},
        // T
        {{{1,0},{0,1},{1,1},{2,1}},{{1,0},{1,1},{2,1},{1,2}},{{0,1},{1,1},{2,1},{1,2}},{{1,0},{0,1},{1,1},{1,2}}},
        // Z
        {{{0,0},{1,0},{1,1},{2,1}},{{2,0},{1,1},{2,1},{1,2}},{{0,1},{1,1},{1,2},{2,2}},{{1,0},{0,1},{1,1},{0,2}}}
    };

    public void init() {
        setSize(APPLET_WIDTH, APPLET_HEIGHT);
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        spawnPiece();
        gameThread = new Thread(this);
        gameThread.start();

        // Load and play theme music
        try {
            URL url = new URL(getDocumentBase(), "tetristheme.mid");
            themeMusic = getAudioClip(url);
            themeMusic.loop();
        } catch (Exception e) {
            System.out.println("Music not found or failed to load.");
        }
    }

    public void run() {
        while (running) {
            try { Thread.sleep(400); } catch (InterruptedException e) {}
            if (!gameOver && !paused) {
                if (!move(0, 1, curRot)) {
                    placePiece();
                    clearLines();
                    spawnPiece();
                }
                repaint();
            }
        }
    }

    public void paint(Graphics g) {
        // Playfield background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(ORIGIN_X, ORIGIN_Y, PLAYFIELD_WIDTH, ROWS * BLOCK);

        // Grid
        g.setColor(new Color(80, 80, 80));
        for (int r = 0; r <= ROWS; r++)
            g.drawLine(ORIGIN_X, ORIGIN_Y + r * BLOCK, ORIGIN_X + PLAYFIELD_WIDTH, ORIGIN_Y + r * BLOCK);
        for (int c = 0; c <= COLS; c++)
            g.drawLine(ORIGIN_X + c * BLOCK, ORIGIN_Y, ORIGIN_X + c * BLOCK, ORIGIN_Y + ROWS * BLOCK);

        // Draw blocks
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (board[r][c] != 0) {
                    g.setColor(colors[board[r][c]]);
                    g.fillRect(ORIGIN_X + c * BLOCK + 1, ORIGIN_Y + r * BLOCK + 1, BLOCK - 2, BLOCK - 2);
                }

        // Draw current piece
        g.setColor(colors[curType + 1]);
        for (int[] b : SHAPES[curType][curRot])
            g.fillRect(ORIGIN_X + (curX + b[0]) * BLOCK + 1, ORIGIN_Y + (curY + b[1]) * BLOCK + 1, BLOCK - 2, BLOCK - 2);

        // Sidebar background
        g.setColor(new Color(30,30,30));
        g.fillRect(ORIGIN_X + PLAYFIELD_WIDTH, ORIGIN_Y, SIDEBAR, ROWS * BLOCK);

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score", ORIGIN_X + PLAYFIELD_WIDTH + 10, ORIGIN_Y + 40);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("" + score, ORIGIN_X + PLAYFIELD_WIDTH + 10, ORIGIN_Y + 70);

        // Next piece
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Next", ORIGIN_X + PLAYFIELD_WIDTH + 10, ORIGIN_Y + 120);
        int next = nextPieces.peek() != null ? nextPieces.peek() : 0;
        g.setColor(colors[next + 1]);
        for (int[] b : SHAPES[next][0])
            g.fillRect(ORIGIN_X + PLAYFIELD_WIDTH + 30 + b[0]*BLOCK/2, ORIGIN_Y + 140 + b[1]*BLOCK/2, BLOCK/2, BLOCK/2);

        // Controls
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("←/→ or A/D: Move", ORIGIN_X + PLAYFIELD_WIDTH + 10, ORIGIN_Y + 200);
        g.drawString("↑ or W or R: Rotate", ORIGIN_X + PLAYFIELD_WIDTH + 10, ORIGIN_Y + 215);
        g.drawString("↓ or S: Soft drop", ORIGIN_X + PLAYFIELD_WIDTH + 10, ORIGIN_Y + 230);
        g.drawString("Space: Hard drop", ORIGIN_X + PLAYFIELD_WIDTH + 10, ORIGIN_Y + 245);
        g.drawString("ESC: Pause", ORIGIN_X + PLAYFIELD_WIDTH + 10, ORIGIN_Y + 260);

        // Game over
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("GAME OVER", ORIGIN_X + 30, ORIGIN_Y + 240);
        } else if (paused) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("PAUSED", ORIGIN_X + 60, ORIGIN_Y + 240);
        }
    }

    private void spawnPiece() {
        if (nextPieces.size() < 2) {
            java.util.List<Integer> bag = java.util.Arrays.asList(0,1,2,3,4,5,6);
            java.util.Collections.shuffle(bag);
            nextPieces.addAll(bag);
        }
        curType = nextPieces.poll();
        curRot = 0;
        curX = 3;
        curY = 0;
        if (!valid(curX, curY, curRot)) gameOver = true;
    }

    private boolean move(int dx, int dy, int rot) {
        if (valid(curX + dx, curY + dy, rot)) {
            curX += dx;
            curY += dy;
            curRot = rot;
            return true;
        }
        return false;
    }

    private boolean valid(int x, int y, int rot) {
        for (int[] b : SHAPES[curType][rot])
            if (x + b[0] < 0 || x + b[0] >= COLS || y + b[1] < 0 || y + b[1] >= ROWS || board[y + b[1]][x + b[0]] != 0)
                return false;
        return true;
    }

    private void placePiece() {
        for (int[] b : SHAPES[curType][curRot])
            board[curY + b[1]][curX + b[0]] = curType + 1;
    }

    private void clearLines() {
        int clears = 0;
        for (int r = ROWS - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < COLS; c++)
                if (board[r][c] == 0) full = false;
            if (full) {
                clears++;
                for (int rr = r; rr > 0; rr--)
                    System.arraycopy(board[rr - 1], 0, board[rr], 0, COLS);
                for (int c = 0; c < COLS; c++) board[0][c] = 0;
                r++; // recheck this row
            }
        }
        switch (clears) {
            case 1: score += 100; break;
            case 2: score += 200; break;
            case 3: score += 300; break;
            case 4: score += 400; break;
        }
    }

    // Controls
    public void keyPressed(KeyEvent e) {
        if (gameOver) return;
        int code = e.getKeyCode();
        char ch = e.getKeyChar();
        if (code == KeyEvent.VK_ESCAPE) {
            paused = !paused;
            repaint();
            return;
        }
        if (paused) return;
        switch (code) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                move(-1, 0, curRot); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                move(1, 0, curRot); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                move(0, 1, curRot); break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_R:
                move(0, 0, (curRot + 1) % 4); break;
            case KeyEvent.VK_SPACE:
                while (move(0, 1, curRot));
                placePiece();
                clearLines();
                spawnPiece();
                break;
        }
        repaint();
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}