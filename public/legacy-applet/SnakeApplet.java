import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class SnakeApplet extends Applet implements Runnable, KeyListener {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int GRID_SIZE = 20;
    private static final int GRID_WIDTH = WIDTH / GRID_SIZE;
    private static final int GRID_HEIGHT = HEIGHT / GRID_SIZE;

    private Thread gameThread;
    private List<Point> snake;
    private Point fruit;
    private Point direction;
    private Point lastDirection;
    private boolean isMoving = false;
    private float speed = 0.1f;
    private int score = 0;
    private int lastMilestone = 0;
    private long lastUpdateTime;
    private boolean showMessage = false;
    private long messageShowTime;

    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private final Color GRID_COLOR = new Color(50, 50, 50);
    private final Color SNAKE_HEAD_COLOR = new Color(200, 200, 0);
    private final Color SNAKE_BODY_COLOR = new Color(0, 200, 0);
    private final Color FRUIT_COLOR = new Color(200, 0, 0);
    private final Color MESSAGE_COLOR = Color.YELLOW;

    private final int[][] HEAD_PATTERN = {
        {0, 1, 1, 1, 0},
        {1, 0, 1, 0, 1},
        {1, 1, 0, 1, 1},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1}
    };

    private final int[][] BODY_PATTERN = {
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1}
    };

    private final int[][] FRUIT_PATTERN = {
        {0, 1, 1, 1, 0},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
        {0, 1, 1, 1, 0}
    };

    public void init() {
        setSize(WIDTH, HEIGHT);
        setBackground(BACKGROUND_COLOR);
        addKeyListener(this);
        setFocusable(true);

        snake = new ArrayList<>();
        snake.add(new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2));
        snake.add(new Point(GRID_WIDTH / 2 - 1, GRID_HEIGHT / 2));
        snake.add(new Point(GRID_WIDTH / 2 - 2, GRID_HEIGHT / 2));

        direction = new Point(1, 0);
        lastDirection = new Point(1, 0);
        spawnFruit();
        lastUpdateTime = System.currentTimeMillis();

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        while (true) {
            updateGame();
            repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {}
        }
    }

    public void paint(Graphics g) {
        // Draw grid
        g.setColor(GRID_COLOR);
        for (int x = 0; x <= WIDTH; x += GRID_SIZE)
            g.drawLine(x, 0, x, HEIGHT);
        for (int y = 0; y <= HEIGHT; y += GRID_SIZE)
            g.drawLine(0, y, WIDTH, y);

        // Draw snake
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            if (i == 0)
                drawPattern(g, p.x * GRID_SIZE, p.y * GRID_SIZE, HEAD_PATTERN, SNAKE_HEAD_COLOR);
            else
                drawPattern(g, p.x * GRID_SIZE, p.y * GRID_SIZE, BODY_PATTERN, SNAKE_BODY_COLOR);
        }

        // Draw fruit
        drawPattern(g, fruit.x * GRID_SIZE, fruit.y * GRID_SIZE, FRUIT_PATTERN, FRUIT_COLOR);

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, 20);

        if (showMessage && System.currentTimeMillis() - messageShowTime < 2000) {
            g.setColor(MESSAGE_COLOR);
            g.drawString("MILESTONE: " + score + " POINTS!", WIDTH / 2 - 80, 40);
        }
    }

    private void updateGame() {
        long now = System.currentTimeMillis();
        if ((now - lastUpdateTime) / 1000.0 < speed || !isMoving)
            return;

        lastUpdateTime = now;
        Point newHead = new Point(snake.get(0).x + direction.x, snake.get(0).y + direction.y);

        // Wall collision
        if (newHead.x < 0 || newHead.x >= GRID_WIDTH || newHead.y < 0 || newHead.y >= GRID_HEIGHT) {
            reset();
            return;
        }

        // Self collision
        for (int i = 1; i < snake.size(); i++) {
            if (newHead.equals(snake.get(i))) {
                reset();
                return;
            }
        }

        // Fruit collision
        if (newHead.equals(fruit)) {
            snake.add(0, newHead);
            spawnFruit();
            score += 10;

            if (score >= 100 && score / 100 > lastMilestone / 100) {
                lastMilestone = score;
                showMessage = true;
                messageShowTime = now;
            }

            if (score % 50 == 0 && speed > 0.05f) {
                speed -= 0.01f;
            }
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }
    }

    private void spawnFruit() {
        Random rand = new Random();
        while (true) {
            int x = rand.nextInt(GRID_WIDTH);
            int y = rand.nextInt(GRID_HEIGHT);
            Point p = new Point(x, y);
            if (!snake.contains(p)) {
                fruit = p;
                return;
            }
        }
    }

    private void reset() {
        snake.clear();
        snake.add(new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2));
        snake.add(new Point(GRID_WIDTH / 2 - 1, GRID_HEIGHT / 2));
        snake.add(new Point(GRID_WIDTH / 2 - 2, GRID_HEIGHT / 2));
        direction.setLocation(1, 0);
        lastDirection.setLocation(1, 0);
        isMoving = false;
        speed = 0.1f;
        score = 0;
        lastMilestone = 0;
        spawnFruit();
    }

    private void drawPattern(Graphics g, int x, int y, int[][] pattern, Color color) {
        int scale = GRID_SIZE / 5;
        g.setColor(color);
        for (int py = 0; py < 5; py++) {
            for (int px = 0; px < 5; px++) {
                if (pattern[py][px] == 1) {
                    g.fillRect(x + px * scale, y + py * scale, scale, scale);
                }
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        boolean changed = false;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                if (direction.x == 0) { direction.setLocation(-1, 0); isMoving = true; changed = true; }
                break;
            case KeyEvent.VK_D:
                if (direction.x == 0) { direction.setLocation(1, 0); isMoving = true; changed = true; }
                break;
            case KeyEvent.VK_W:
                if (direction.y == 0) { direction.setLocation(0, -1); isMoving = true; changed = true; }
                break;
            case KeyEvent.VK_S:
                if (direction.y == 0) { direction.setLocation(0, 1); isMoving = true; changed = true; }
                break;
            case KeyEvent.VK_SPACE:
                isMoving = !isMoving;
                break;
        }

        if (changed && (direction.x != lastDirection.x || direction.y != lastDirection.y)) {
            lastDirection.setLocation(direction);
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
