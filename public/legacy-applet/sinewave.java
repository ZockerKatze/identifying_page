import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
// Removed: import javax.swing.*;

public class sinewave extends Applet implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener, Runnable {

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

    // --- In-applet input form state ---
    boolean showWaveInputDialog = false;
    String inputAmp = "1.0";
    String inputFreq = "1.0";
    String inputPhase = "0.0";
    int inputField = 0; // 0=amp, 1=freq, 2=phase
    WaveType pendingWaveType = WaveType.SINE;
    boolean inputError = false;
    boolean modifyMode = false; // true if modifying, false if creating
    boolean[] fieldEdited = new boolean[] {false, false, false};

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

        // Draw in-applet input dialog if needed
        if (showWaveInputDialog) {
            drawWaveInputDialog(g2, getWidth(), getHeight());
        }
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
                "M - Modify Currently Selected Wave",
                "Del - Remove Wave",
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

    void drawWaveInputDialog(Graphics2D g2, int width, int height) {
        int boxWidth = 260;
        int boxHeight = 180;
        int x = (width - boxWidth) / 2;
        int y = (height - boxHeight) / 2;
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2.drawString("New " + pendingWaveType + " Wave", x + 20, y + 30);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        // Labels
        g2.drawString("Amplitude:", x + 20, y + 60);
        g2.drawString("Frequency:", x + 20, y + 90);
        g2.drawString("Phase Shift:", x + 20, y + 120);
        // Fields
        g2.setColor(inputField == 0 ? Color.YELLOW : Color.WHITE);
        g2.drawString(inputAmp, x + 120, y + 60);
        g2.setColor(inputField == 1 ? Color.YELLOW : Color.WHITE);
        g2.drawString(inputFreq, x + 120, y + 90);
        g2.setColor(inputField == 2 ? Color.YELLOW : Color.WHITE);
        g2.drawString(inputPhase, x + 120, y + 120);
        // Buttons
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(x + 30, y + 140, 70, 28, 10, 10);
        g2.fillRoundRect(x + 150, y + 140, 70, 28, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawString("OK", x + 55, y + 160);
        g2.drawString("Cancel", x + 165, y + 160);
        if (inputError) {
            g2.setColor(Color.RED);
            g2.drawString("Invalid input!", x + 80, y + 135);
        }
    }

    // Replace createWaveDialog with AWT-only dialog
    // Remove createWaveDialog (no longer needed)

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
    public void mousePressed(MouseEvent e) {
        if (showWaveInputDialog) {
            int width = getWidth(), height = getHeight();
            int boxWidth = 260, boxHeight = 180;
            int x = (width - boxWidth) / 2, y = (height - boxHeight) / 2;
            int mx = e.getX(), my = e.getY();
            // Field selection
            if (mx > x + 120 && mx < x + 220) {
                if (my > y + 48 && my < y + 68) { inputField = 0; fieldEdited[0] = false; }
                else if (my > y + 78 && my < y + 98) { inputField = 1; fieldEdited[1] = false; }
                else if (my > y + 108 && my < y + 128) { inputField = 2; fieldEdited[2] = false; }
            }
            repaint();
            return;
        }
        mouseDragged(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (showWaveInputDialog && e.getButton() == MouseEvent.BUTTON1) {
            int width = getWidth(), height = getHeight();
            int boxWidth = 260, boxHeight = 180;
            int x = (width - boxWidth) / 2, y = (height - boxHeight) / 2;
            int mx = e.getX(), my = e.getY();
            // OK button
            if (mx > x + 30 && mx < x + 100 && my > y + 140 && my < y + 168) {
                try {
                    double amp = Double.parseDouble(inputAmp);
                    double freq = Double.parseDouble(inputFreq);
                    double phase = Double.parseDouble(inputPhase);
                    Color color = availableColors[waves.size() % availableColors.length];
                    if (modifyMode) {
                        // Update selected wave
                        Wave wave = waves.get(selectedWave);
                        wave.amplitude = amp;
                        wave.frequency = freq;
                        wave.phaseShift = phase;
                        // Don't change color or type
                    } else {
                        waves.add(new Wave(amp, freq, phase, color, pendingWaveType));
                        selectedWave = waves.size() - 1;
                    }
                    showWaveInputDialog = false;
                    inputError = false;
                    staticLayer = null;
                    repaint();
                } catch (NumberFormatException ex) {
                    inputError = true;
                    repaint();
                }
                return;
            }
            // Cancel button
            if (mx > x + 150 && mx < x + 220 && my > y + 140 && my < y + 168) {
                showWaveInputDialog = false;
                inputError = false;
                repaint();
                return;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (showWaveInputDialog) {
            // Navigation
            if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (e.isShiftDown() || e.getKeyCode() == KeyEvent.VK_UP) {
                    inputField = (inputField + 2) % 3; // Up or Shift+Tab
                } else {
                    inputField = (inputField + 1) % 3; // Down or Tab
                }
                fieldEdited[inputField] = false;
                repaint();
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                inputField = (inputField + 2) % 3;
                fieldEdited[inputField] = false;
                repaint();
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                inputField = (inputField + 1) % 3;
                fieldEdited[inputField] = false;
                repaint();
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                try {
                    double amp = Double.parseDouble(inputAmp);
                    double freq = Double.parseDouble(inputFreq);
                    double phase = Double.parseDouble(inputPhase);
                    Color color = availableColors[waves.size() % availableColors.length];
                    if (modifyMode) {
                        // Update selected wave
                        Wave wave = waves.get(selectedWave);
                        wave.amplitude = amp;
                        wave.frequency = freq;
                        wave.phaseShift = phase;
                        // Don't change color or type
                    } else {
                        waves.add(new Wave(amp, freq, phase, color, pendingWaveType));
                        selectedWave = waves.size() - 1;
                    }
                    showWaveInputDialog = false;
                    inputError = false;
                    staticLayer = null;
                    repaint();
                } catch (NumberFormatException ex) {
                    inputError = true;
                    repaint();
                }
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                showWaveInputDialog = false;
                inputError = false;
                repaint();
                return;
            }
            // Backspace
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (inputField == 0 && inputAmp.length() > 0) inputAmp = inputAmp.substring(0, inputAmp.length() - 1);
                if (inputField == 1 && inputFreq.length() > 0) inputFreq = inputFreq.substring(0, inputFreq.length() - 1);
                if (inputField == 2 && inputPhase.length() > 0) inputPhase = inputPhase.substring(0, inputPhase.length() - 1);
                fieldEdited[inputField] = true;
                repaint();
                return;
            }
            // Accept numbers, dot, minus
            char c = e.getKeyChar();
            if ((c >= '0' && c <= '9') || c == '.' || c == '-') {
                if (!fieldEdited[inputField]) {
                    // Replace value on first keypress after focus
                    if (inputField == 0) inputAmp = "" + c;
                    if (inputField == 1) inputFreq = "" + c;
                    if (inputField == 2) inputPhase = "" + c;
                    fieldEdited[inputField] = true;
                } else {
                    if (inputField == 0) inputAmp += c;
                    if (inputField == 1) inputFreq += c;
                    if (inputField == 2) inputPhase += c;
                }
                repaint();
            }
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            animateMode = !animateMode;
            repaint();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            // Modify selected wave
            if (!waves.isEmpty()) {
                Wave wave = waves.get(selectedWave);
                inputAmp = Double.toString(wave.amplitude);
                inputFreq = Double.toString(wave.frequency);
                inputPhase = Double.toString(wave.phaseShift);
                inputField = 0;
                fieldEdited[0] = fieldEdited[1] = fieldEdited[2] = false;
                pendingWaveType = wave.type;
                modifyMode = true;
                showWaveInputDialog = true;
                inputError = false;
                repaint();
            }
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
            case KeyEvent.VK_N:
                pendingWaveType = WaveType.SINE;
                showWaveInputDialog = true;
                inputAmp = "1.0"; inputFreq = "1.0"; inputPhase = "0.0"; inputField = 0; inputError = false;
                fieldEdited[0] = fieldEdited[1] = fieldEdited[2] = false;
                modifyMode = false;
                repaint();
                break;
            case KeyEvent.VK_T:
                pendingWaveType = WaveType.TRIANGLE;
                showWaveInputDialog = true;
                inputAmp = "1.0"; inputFreq = "1.0"; inputPhase = "0.0"; inputField = 0; inputError = false;
                fieldEdited[0] = fieldEdited[1] = fieldEdited[2] = false;
                modifyMode = false;
                repaint();
                break;
            case KeyEvent.VK_Q:
                pendingWaveType = WaveType.SQUARE;
                showWaveInputDialog = true;
                inputAmp = "1.0"; inputFreq = "1.0"; inputPhase = "0.0"; inputField = 0; inputError = false;
                fieldEdited[0] = fieldEdited[1] = fieldEdited[2] = false;
                modifyMode = false;
                repaint();
                break;
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