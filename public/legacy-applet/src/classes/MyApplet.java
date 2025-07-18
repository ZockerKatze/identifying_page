package src.classes;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


/* I use a fucking template here. Java Applets are older than me anyways 

    I precompile the JavaApplet. Cause doing this in the GH Action is shit.

    Anyway. This code is stolen. Do you think I can do this complex fucking math?

*/

public class MyApplet extends Applet implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener{
    private volatile Thread animator;
    private double angleX = 0, angleY = 0;
    private double dragAngleX = 0, dragAngleY = 0;
    private double zoom = 1.0;
    private int lastX, lastY;
    private boolean dragging = false;
    private float hue = 0f; // Rainbow hue value

    // Double buffering
    private Image offscreenImage;
    private Graphics2D offscreenGraphics;
    private int offscreenW = -1, offscreenH = -1;

    private final double[][] cubeVertices = {
        {-1, -1, -1}, {1, -1, -1},
        {1,  1, -1}, {-1, 1, -1},
        {-1, -1,  1}, {1, -1,  1},
        {1,  1,  1}, {-1, 1,  1}
    };

    private final int[][] edges = {
        {0,1}, {1,2}, {2,3}, {3,0},
        {4,5}, {5,6}, {6,7}, {7,4},
        {0,4}, {1,5}, {2,6}, {3,7}
    };

    // Reusable arrays for projection
    private final int[] projX = new int[8];
    private final int[] projY = new int[8];

    // Reuse Color and Stroke
    private final BasicStroke stroke = new BasicStroke(2);

    @Override
    public void init() {
        setBackground(Color.black);
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void start() {
        if (animator == null) {
            animator = new Thread(this);
            animator.start();
        }
    }

    @Override
    public void stop() {
        animator = null;
    }

    @Override
    public void run() {
        final int targetFPS = 60;
        final long targetFrameTime = 1000 / targetFPS;
        while (Thread.currentThread() == animator) {
            long startTime = System.currentTimeMillis();
            if (!dragging) {
                angleY += 0.01; // idle spin
                angleX += 0.01;
            }

            hue += 0.002f; // animate hue
            if (hue > 1f) hue -= 1f;

            repaint();
            long elapsed = System.currentTimeMillis() - startTime;
            long sleepTime = targetFrameTime - elapsed;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignored) {}
            } else {
                Thread.yield();
            }
        }
    }

    private void ensureOffscreen(int w, int h) {
        if (offscreenImage == null || w != offscreenW || h != offscreenH) {
            offscreenImage = createImage(w, h);
            offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
            offscreenW = w;
            offscreenH = h;
        }
    }

    private void project(double x, double y, double z, int w, int h, int idx) {
        double scale = 100 * zoom;
        double distance = 3;
        double factor = scale / (z + distance);
        projX[idx] = (int)(x * factor + w / 2);
        projY[idx] = (int)(y * factor + h / 2);
    }

    private double[] rotate(double[] p, double ax, double ay) {
        // Rotate around X
        double cosX = Math.cos(ax);
        double sinX = Math.sin(ax);
        double y = p[1] * cosX - p[2] * sinX;
        double z = p[1] * sinX + p[2] * cosX;

        // Rotate around Y
        double cosY = Math.cos(ay);
        double sinY = Math.sin(ay);
        double x = p[0] * cosY + z * sinY;
        z = -p[0] * sinY + z * cosY;

        return new double[] { x, y, z };
    }

    @Override
    public void update(Graphics g) {
        // Override update to prevent flicker
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        ensureOffscreen(w, h);
        Graphics2D g2 = offscreenGraphics;

        // Clear screen
        g2.setColor(Color.black);
        g2.fillRect(0, 0, w, h);

        // Calculate current rainbow color
        Color rainbowColor = Color.getHSBColor(hue, 1.0f, 1.0f);
        g2.setColor(rainbowColor);
        g2.setStroke(stroke);

        // Project and draw cube
        for (int i = 0; i < cubeVertices.length; i++) {
            double[] rotated = rotate(cubeVertices[i], angleX, angleY);
            project(rotated[0], rotated[1], rotated[2], w, h, i);
        }

        for (int[] edge : edges) {
            int x1 = projX[edge[0]];
            int y1 = projY[edge[0]];
            int x2 = projX[edge[1]];
            int y2 = projY[edge[1]];
            g2.drawLine(x1, y1, x2, y2);
        }

        // Blit offscreen image
        g.drawImage(offscreenImage, 0, 0, this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragging = true;
        lastX = e.getX();
        lastY = e.getY();
        dragAngleX = angleX;
        dragAngleY = angleY;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;
        angleY = dragAngleY + dx * 0.01;
        angleX = dragAngleX + dy * 0.01;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            zoom *= 1.1;
        } else {
            zoom /= 1.1;
        }
        // Clamp so we don't vanish away
        zoom = Math.max(1.0, Math.min(zoom,2.0));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    // Required overrides
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}

