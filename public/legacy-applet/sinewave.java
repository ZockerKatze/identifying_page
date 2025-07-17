import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
// Removed: import javax.swing.*;

public class sinewave extends Applet implements MouseWheelListener, MouseMotionListener, KeyListener, Runnable {

    enum WaveType { SINE, SQUARE, TRIANGLE }

    class Wave {
        double amplitude;
        double frequency;
        double phaseShift;
        Color color;
        WaveType type;

        Wave(double amplitude, double frequency, double phaseShift, Color color, WaveType type) {
            this.amplitude = amplitude;
            this.frequency = frequency;
            this.phaseShift = phaseShift;
            this.color = color;
            this.type = type;
        }

        double getY(double x) {
            double val = frequency * x + phaseShift;
            switch (type) {
                case SINE:
                    return amplitude * Math.sin(val);
                case SQUARE:
                    return amplitude * Math.signum(Math.sin(val));
                case TRIANGLE:
                    return amplitude * (2 / Math.PI) * Math.asin(Math.sin(val));
                default:
                    return 0;
            }
        }
    }

    ArrayList<Wave> waves = new ArrayList<>();
    int selectedWave = 0;

    double zoomX = 50.0;
    double zoomY = 50.0;
    int offsetX = 0;
    int offsetY = 0;

    int lastMouseX, lastMouseY;
    Color[] availableColors = {
        Color.GREEN, Color.CYAN, Color.MAGENTA,
        Color.YELLOW, Color.ORANGE, Color.PINK, Color.WHITE
    };

    boolean animateMode = false;
    boolean showHelp = false;
    double animationTime = 0;
    double animationSpeed = 0.05;

    Thread animationThread;
    Image staticLayer;

    public void init() {
        setBackground(Color.BLACK);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();

        waves.add(new Wave(1.0, 1.0, 0.0, Color.GREEN, WaveType.SINE));

        animationThread = new Thread(this);
        animationThread.start();
    }

    public void paint(Graphics g) {
        if (staticLayer == null || staticLayer.getWidth(null) != getWidth() || staticLayer.getHeight(null) != getHeight()) {
            staticLayer = createImage(getWidth(), getHeight());
            drawStaticLayer(staticLayer.getGraphics());
        }

        g.drawImage(staticLayer, 0, 0, null);

        Graphics2D g2 = (Graphics2D) g;

        if (animateMode) {
            drawAnimatedWave(g2, getWidth(), getHeight());
        } else {
            drawSine(g2, getWidth(), getHeight());
        }
        g2.setColor(Color.GRAY);
        g2.drawString("H - Help", getWidth() - 70, getHeight() - 10);
    }

    void drawStaticLayer(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);

        // Grid
        g2.setColor(new Color(50, 50, 50));
        int gridSpacing = 50;
        for (int x = -offsetX % gridSpacing; x < width; x += gridSpacing)
            g2.drawLine(x, 0, x, height);
        for (int y = -offsetY % gridSpacing; y < height; y += gridSpacing)
            g2.drawLine(0, y, width, y);

        g2.setColor(Color.GRAY);
        g2.drawLine(0, height / 2 + offsetY, width, height / 2 + offsetY);
        g2.drawLine(width / 2 + offsetX, 0, width / 2 + offsetX, height);

        drawHelp(g2, width, height);
    }

    void drawSine(Graphics2D g2, int width, int height) {
        for (int w = 0; w < waves.size(); w++) {
            Wave wave = waves.get(w);
            g2.setColor(wave.color);

            int numPoints = width;
            double[] y = new double[numPoints];
            for (int i = 0; i < numPoints; i++) {
                double mathX = (i - offsetX) / zoomX;
                y[i] = wave.getY(mathX);
            }

            for (int i = 0; i < numPoints - 1; i++) {
                int x1 = i;
                int y1 = (int) (height / 2 - y[i] * zoomY) + offsetY;
                int x2 = i + 1;
                int y2 = (int) (height / 2 - y[i + 1] * zoomY) + offsetY;
                g2.drawLine(x1, y1, x2, y2);
            }

            // Label
            int labelX = width / 4;
            int labelY = (int) (height / 2 - y[width / 4] * zoomY) + offsetY - 5;
            char waveLabel = (char) ('A' + w);
            g2.setColor(Color.WHITE);
            g2.drawString((w == selectedWave ? "[" : " ") + waveLabel + (w == selectedWave ? "]" : " "), labelX, labelY);
        }
    }

    void drawAnimatedWave(Graphics2D g2, int width, int height) {
        g2.setColor(Color.GRAY);
        g2.drawLine(0, height / 2, width, height / 2);

        for (Wave wave : waves) {
            g2.setColor(wave.color);
            for (int x = 0; x < width - 1; x++) {
                double t1 = (animationTime + x * 0.01);
                double t2 = (animationTime + (x + 1) * 0.01);
                double y1 = wave.getY(t1);
                double y2 = wave.getY(t2);

                int screenY1 = (int) (height / 2 - y1 * 80);
                int screenY2 = (int) (height / 2 - y2 * 80);

                g2.drawLine(x, screenY1, x + 1, screenY2);
            }
        }

        g2.setColor(Color.WHITE);
        g2.drawString("Animated Signal Mode (Press E to Exit)", 10, 20);
    }

    void drawHelp(Graphics2D g2, int width, int height) {
        if (!showHelp) return;

        String[] helpText = animateMode ? new String[]{
                "E - Exit Animated Mode",
                "H - Hide Help"
        } : new String[]{
                "S - Switch Wave",
                "N - New Sine Wave",
                "T - New Triangle Wave",
                "Q - New Square Wave",
                "Del - Remove Wave (Not A)",
                "Arrow Keys - Adjust Amplitude / Phase",
                "Mouse Drag - Pan View",
                "Wheel - Zoom Y",
                "Shift+Wheel - Zoom X",
                "E - Animated Signal Mode",
                "H - Hide Help"
        };

        int boxWidth = 280;
        int boxHeight = helpText.length * 15 + 20;
        int x = (width - boxWidth) / 2;
        int y = (height - boxHeight) / 2;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(x, y, boxWidth, boxHeight, 15, 15);

        for (int i = 0; i < helpText.length; i++) {
            g2.drawString(helpText[i], x + 15, y + 25 + i * 15);
        }
    }

    // Replace createWaveDialog with AWT-only dialog
    Wave createWaveDialog(WaveType type) {
        final Dialog dialog = new Dialog((Frame) null, "New " + type + " Wave Parameters", true);
        dialog.setLayout(new GridLayout(0, 2));
        Label ampLabel = new Label("Amplitude:");
        TextField ampField = new TextField("1.0");
        Label freqLabel = new Label("Frequency:");
        TextField freqField = new TextField("1.0");
        Label phaseLabel = new Label("Phase Shift:");
        TextField phaseField = new TextField("0.0");
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        dialog.add(ampLabel);
        dialog.add(ampField);
        dialog.add(freqLabel);
        dialog.add(freqField);
        dialog.add(phaseLabel);
        dialog.add(phaseField);
        dialog.add(okButton);
        dialog.add(cancelButton);

        final Wave[] result = new Wave[1];

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double amp = Double.parseDouble(ampField.getText());
                    double freq = Double.parseDouble(freqField.getText());
                    double phase = Double.parseDouble(phaseField.getText());
                    Color color = availableColors[waves.size() % availableColors.length];
                    result[0] = new Wave(amp, freq, phase, color, type);
                } catch (NumberFormatException ex) {
                    result[0] = null;
                }
                dialog.setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result[0] = null;
                dialog.setVisible(false);
            }
        });

        dialog.setSize(300, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.dispose();
        return result[0];
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (animateMode) return;
        int notches = e.getWheelRotation();
        if (e.isShiftDown()) {
            zoomX *= (notches > 0) ? 0.9 : 1.1;
        } else {
            zoomY *= (notches > 0) ? 0.9 : 1.1;
        }
        staticLayer = null;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (animateMode) return;
        int dx = e.getX() - lastMouseX;
        int dy = e.getY() - lastMouseY;
        offsetX += dx;
        offsetY += dy;
        lastMouseX = e.getX();
        lastMouseY = e.getY();
        staticLayer = null;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_E) {
            animateMode = !animateMode;
            repaint();
            return;
        }

        if (animateMode) return;
        if (waves.isEmpty()) return;
        Wave wave = waves.get(selectedWave);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                wave.amplitude += 0.1;
                break;
            case KeyEvent.VK_DOWN:
                wave.amplitude -= 0.1;
                break;
            case KeyEvent.VK_LEFT:
                wave.phaseShift -= 0.1;
                break;
            case KeyEvent.VK_RIGHT:
                wave.phaseShift += 0.1;
                break;
            case KeyEvent.VK_N: {
                Wave newWave = createWaveDialog(WaveType.SINE);
                if (newWave != null) {
                    waves.add(newWave);
                    selectedWave = waves.size() - 1;
                }
                break;
            }
            case KeyEvent.VK_T: {
                Wave newWave = createWaveDialog(WaveType.TRIANGLE);
                if (newWave != null) {
                    waves.add(newWave);
                    selectedWave = waves.size() - 1;
                }
                break;
            }
            case KeyEvent.VK_Q: {
                Wave newWave = createWaveDialog(WaveType.SQUARE);
                if (newWave != null) {
                    waves.add(newWave);
                    selectedWave = waves.size() - 1;
                }
                break;
            }
            case KeyEvent.VK_DELETE:
                if (selectedWave > 0) {
                    waves.remove(selectedWave);
                    if (selectedWave >= waves.size()) selectedWave = waves.size() - 1;
                }
                break;
            case KeyEvent.VK_S:
                selectedWave = (selectedWave + 1) % waves.size();
                break;
            case KeyEvent.VK_H:
                showHelp = !showHelp;
                repaint();
                break;
        }
        staticLayer = null;
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void run() {
        while (true) {
            if (animateMode) {
                animationTime += animationSpeed;
                repaint();
            }
            try {
                Thread.sleep(64);
            } catch (InterruptedException ignored) {}
        }
    }
}