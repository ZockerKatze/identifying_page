import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


/* I use a fucking template here. Java Applets are older than me anyways 

    I precompile the JavaApplet. Cause doing this in the GH Action is shit.

    Anyway. This code is stolen. Do you think I can do this complex fucking math?

*/

public class MyApplet extends Applet implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener{
    private Thread animator;
    private double angleX = 0, angleY = 0;
    private double dragAngleX = 0, dragAngleY = 0;
    private double zoom = 1.0;
    private int lastX, lastY;
    private boolean dragging = false;
    private float hue = 0f; // Rainbow hue value

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

    @Override
    public void init() {
        setBackground(Color.black);
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void start() {
        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void stop() {
        animator = null;
    }

    @Override
    public void run() {
        while (Thread.currentThread() == animator) {
            if (!dragging) {
                angleY += 0.01; // idle spin
                angleX += 0.01;
            }

            hue += 0.002f; // animate hue
            if (hue > 1f) hue -= 1f;

            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException ignored) {}
        }
    }

    private Point project(double x, double y, double z, int w, int h) {
        double scale = 100 * zoom;
        double distance = 3;
        double factor = scale / (z + distance);
        int x2d = (int)(x * factor + w / 2);
        int y2d = (int)(y * factor + h / 2);
        return new Point(x2d, y2d);
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
    public void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2 = (Graphics2D) g;

        // Clear screen
        g2.setColor(Color.black);
        g2.fillRect(0, 0, w, h);

        // Calculate current rainbow color
        Color rainbowColor = Color.getHSBColor(hue, 1.0f, 1.0f);
        g2.setColor(rainbowColor);
        g2.setStroke(new BasicStroke(2));

        // Project and draw cube
        Point[] projected = new Point[8];
        for (int i = 0; i < cubeVertices.length; i++) {
            double[] rotated = rotate(cubeVertices[i], angleX, angleY);
            projected[i] = project(rotated[0], rotated[1], rotated[2], w, h);
        }

        for (int[] edge : edges) {
            Point p1 = projected[edge[0]];
            Point p2 = projected[edge[1]];
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
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

        // Clamp shit so we dont vanish away
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

