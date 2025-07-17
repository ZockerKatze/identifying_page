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
    int inputField = 0; // 0=type, 1=amp, 2=freq, 3=phase
    WaveType pendingWaveType = WaveType.SINE;
    boolean inputError = false;
    boolean modifyMode = false; // true if modifying, false if creating
    boolean[] fieldEdited = new boolean[] {false, false, false, false};

    boolean showAnalysisView = false;

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
        if (showAnalysisView) {
            drawAnalysisView((Graphics2D) g, getWidth(), getHeight());
            return;
        }
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

        // Draw help box LAST so it is always on top
        if (showHelp) {
            drawHelp(g2, getWidth(), getHeight());
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
                "A - View Wave Metrics",
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

        g2.setColor(new Color(0, 0, 0)); 
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(x, y, boxWidth, boxHeight, 15, 15);

        for (int i = 0; i < helpText.length; i++) {
            g2.drawString(helpText[i], x + 15, y + 25 + i * 15);
        }
    }

    void drawWaveInputDialog(Graphics2D g2, int width, int height) {
        int boxWidth = 260;
        int boxHeight = 210;
        int x = (width - boxWidth) / 2;
        int y = (height - boxHeight) / 2;
        g2.setColor(new Color(0, 0, 0)); // Solid black
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2.drawString((modifyMode ? "Modify " : "New ") + "Wave", x + 20, y + 30);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        // Type field
        g2.setColor(inputField == 0 ? Color.YELLOW : Color.WHITE);
        g2.drawString("Type: " + pendingWaveType, x + 20, y + 55);
        // Labels
        g2.setColor(Color.WHITE);
        g2.drawString("Amplitude:", x + 20, y + 85);
        g2.drawString("Frequency:", x + 20, y + 115);
        g2.drawString("Phase Shift:", x + 20, y + 145);
        // Fields
        g2.setColor(inputField == 1 ? Color.YELLOW : Color.WHITE);
        g2.drawString(inputAmp, x + 120, y + 85);
        g2.setColor(inputField == 2 ? Color.YELLOW : Color.WHITE);
        g2.drawString(inputFreq, x + 120, y + 115);
        g2.setColor(inputField == 3 ? Color.YELLOW : Color.WHITE);
        g2.drawString(inputPhase, x + 120, y + 145);
        // Phase helper: show degrees if input is in radians, always inside box
        try {
            double phaseVal = parsePhaseInput(inputPhase);
            double deg = Math.toDegrees(phaseVal);
            String degStr = String.format("(%.1f°)", deg);
            FontMetrics fm = g2.getFontMetrics();
            int phaseFieldRight = x + 120 + fm.stringWidth(inputPhase) + 8;
            int degStrWidth = fm.stringWidth(degStr);
            int maxRight = x + boxWidth - 15;
            int degX = phaseFieldRight;
            if (degX + degStrWidth > maxRight) {
                degX = x + 120; // move below if it would clip
                g2.drawString(degStr, degX, y + 145 + fm.getHeight());
            } else {
                g2.drawString(degStr, degX, y + 145);
            }
        } catch (Exception ex) {}
        // Buttons
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(x + 30, y + 180, 70, 28, 10, 10);
        g2.fillRoundRect(x + 150, y + 180, 70, 28, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawString("OK", x + 55, y + 200);
        g2.drawString("Cancel", x + 165, y + 200);
        if (inputError) {
            g2.setColor(Color.RED);
            g2.drawString("Invalid input!", x + 80, y + 170);
        }
    }

    // Helper to parse phase input (supports degrees)
    double parsePhaseInput(String s) {
        s = s.trim();
        if (s.endsWith("°")) {
            return Math.toRadians(Double.parseDouble(s.substring(0, s.length() - 1)));
        }
        if (s.endsWith("d") || s.endsWith("D")) {
            return Math.toRadians(Double.parseDouble(s.substring(0, s.length() - 1)));
        }
        return Double.parseDouble(s);
    }

    void drawAnalysisView(Graphics2D g2, int width, int height) {
        if (waves.isEmpty()) return;
        Wave wave = waves.get(selectedWave);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);
        // Draw axes
        g2.setColor(Color.GRAY);
        int midY = height / 2;
        g2.drawLine(0, midY, width, midY); // x-axis
        g2.drawLine(60, 0, 60, height); // y-axis
        // Plot settings
        int plotLeft = 60, plotRight = width - 60;
        int plotWidth = plotRight - plotLeft;
        double zoomX = plotWidth / (2 * Math.PI); // show one period
        double zoomY = (height - 120) / (2.2 * Math.max(1, Math.abs(wave.amplitude))); // fit amplitude
        // Draw wave
        g2.setColor(wave.color);
        int numPoints = plotWidth;
        double[] y = new double[numPoints];
        double phaseStart = -wave.phaseShift / wave.frequency;
        for (int i = 0; i < numPoints; i++) {
            double mathX = phaseStart + (i * (2 * Math.PI) / numPoints);
            y[i] = wave.getY(mathX);
        }
        for (int i = 0; i < numPoints - 1; i++) {
            int x1 = plotLeft + i;
            int y1 = (int) (midY - y[i] * zoomY);
            int x2 = plotLeft + i + 1;
            int y2 = (int) (midY - y[i + 1] * zoomY);
            g2.drawLine(x1, y1, x2, y2);
        }
        // Find max and min in visible range for amplitude marker
        double maxY = y[0], minY = y[0];
        int maxIdx = 0, minIdx = 0;
        for (int i = 1; i < numPoints; i++) {
            if (y[i] > maxY) { maxY = y[i]; maxIdx = i; }
            if (y[i] < minY) { minY = y[i]; minIdx = i; }
        }
        // Draw amplitude marker (peak)
        g2.setColor(Color.RED);
        int ampX = plotLeft + maxIdx;
        int ampY = (int) (midY - maxY * zoomY);
        g2.drawLine(ampX, midY, ampX, ampY);
        g2.fillOval(ampX - 4, ampY - 4, 8, 8);
        g2.setColor(Color.WHITE);
        g2.drawString("Amplitude (peak)", ampX + 10, ampY);
        // Draw period marker (one period from phase offset)
        double period = (wave.type == WaveType.SQUARE || wave.type == WaveType.SINE || wave.type == WaveType.TRIANGLE) ? (2 * Math.PI / Math.abs(wave.frequency)) : 0;
        if (period > 0) {
            int px1 = plotLeft;
            int px2 = plotLeft + (int) (period * zoomX);
            g2.setColor(Color.CYAN);
            g2.drawLine(px1, midY + 40, px2, midY + 40);
            g2.drawLine(px1, midY + 35, px1, midY + 45);
            g2.drawLine(px2, midY + 35, px2, midY + 45);
            g2.drawString("Period", (px1 + px2) / 2 - 20, midY + 55);
        }
        // Draw phase marker (vertical line at phase offset, more obvious)
        g2.setColor(Color.MAGENTA);
        Stroke oldStroke = g2.getStroke();
        float[] dash = {8.0f, 8.0f};
        g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
        int phasePx = plotLeft;
        int phaseY1 = midY - (int)(zoomY * Math.abs(wave.amplitude));
        int phaseY2 = midY + (int)(zoomY * Math.abs(wave.amplitude));
        g2.drawLine(phasePx, phaseY1, phasePx, phaseY2);
        g2.setStroke(oldStroke);
        // Draw arrow and label for phase
        int arrowY = phaseY1 + 20;
        g2.setColor(Color.MAGENTA);
        g2.drawLine(phasePx, arrowY, phasePx + 30, arrowY - 15);
        g2.setColor(Color.WHITE);
        g2.drawString("Phase Offset", phasePx + 35, arrowY - 15);
        // Draw info box
        int boxW = 260, boxH = 180;
        int infoX = width - boxW - 20, infoY = 30;
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(infoX, infoY, boxW, boxH, 15, 15);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(infoX, infoY, boxW, boxH, 15, 15);
        int textX = infoX + 10, textY = infoY + 20;
        g2.setFont(new Font("Monospaced", Font.BOLD, 15));
        g2.drawString("Wave Analysis", textX, textY);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g2.drawString("Type: " + wave.type, textX, textY + 20);
        g2.drawString(String.format("Amplitude: %.3f", wave.amplitude), textX, textY + 40);
        g2.drawString(String.format("Frequency: %.3f", wave.frequency), textX, textY + 60);
        g2.drawString(String.format("Phase Shift: %.3f rad (%.1f°)", wave.phaseShift, Math.toDegrees(wave.phaseShift)), textX, textY + 80);
        if (period > 0) g2.drawString(String.format("Period: %.3f", period), textX, textY + 100);
        g2.drawString(String.format("Formula:"), textX, textY + 120);
        String formula = "";
        switch (wave.type) {
            case SINE:
                formula = String.format("y = %.3f * sin(%.3f * x + %.3f)", wave.amplitude, wave.frequency, wave.phaseShift);
                break;
            case SQUARE:
                formula = String.format("y = %.3f * sign(sin(%.3f * x + %.3f))", wave.amplitude, wave.frequency, wave.phaseShift);
                break;
            case TRIANGLE:
                formula = String.format("y = %.3f * (2/pi) * asin(sin(%.3f * x + %.3f))", wave.amplitude, wave.frequency, wave.phaseShift);
                break;
        }
        // Wrap formula text if too long
        FontMetrics fm = g2.getFontMetrics();
        int maxFormulaWidth = boxW - 20;
        int fY = textY + 140;
        if (fm.stringWidth(formula) < maxFormulaWidth) {
            g2.drawString(formula, textX, fY);
        } else {
            // Split at spaces
            String[] words = formula.split(" ");
            String line = "";
            int lineY = fY;
            for (String word : words) {
                String test = line.isEmpty() ? word : line + " " + word;
                if (fm.stringWidth(test) > maxFormulaWidth) {
                    g2.drawString(line, textX, lineY);
                    line = word;
                    lineY += fm.getHeight();
                } else {
                    line = test;
                }
            }
            if (!line.isEmpty()) g2.drawString(line, textX, lineY);
        }
        // Move exit message below the info box
        g2.setColor(Color.YELLOW);
        g2.drawString("Press A or Esc to return", textX, infoY + boxH + 25);
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
            int boxWidth = 260, boxHeight = 210;
            int x = (width - boxWidth) / 2, y = (height - boxHeight) / 2;
            int mx = e.getX(), my = e.getY();
            // Field selection
            if (mx > x + 80 && mx < x + 220 && my > y + 40 && my < y + 60) { inputField = 0; fieldEdited[0] = false; repaint(); return; }
            if (mx > x + 120 && mx < x + 220) {
                if (my > y + 73 && my < y + 93) { inputField = 1; fieldEdited[1] = false; }
                else if (my > y + 103 && my < y + 123) { inputField = 2; fieldEdited[2] = false; }
                else if (my > y + 133 && my < y + 153) { inputField = 3; fieldEdited[3] = false; }
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
                    double phase = parsePhaseInput(inputPhase);
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
                } catch (Exception ex) {
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
        if (showAnalysisView) {
            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                showAnalysisView = false;
                repaint();
            }
            return;
        }
        if (showWaveInputDialog) {
            // Navigation
            if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (e.isShiftDown() || e.getKeyCode() == KeyEvent.VK_UP) {
                    inputField = (inputField + 3) % 4; // Up or Shift+Tab
                } else {
                    inputField = (inputField + 1) % 4; // Down or Tab
                }
                fieldEdited[inputField] = false;
                repaint();
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                inputField = (inputField + 3) % 4;
                fieldEdited[inputField] = false;
                repaint();
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                inputField = (inputField + 1) % 4;
                fieldEdited[inputField] = false;
                repaint();
                return;
            }
            // Type field: left/right arrows or S/Q/T
            if (inputField == 0) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    WaveType[] types = WaveType.values();
                    int idx = 0;
                    for (int i = 0; i < types.length; i++) if (types[i] == pendingWaveType) idx = i;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) idx = (idx + types.length - 1) % types.length;
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) idx = (idx + 1) % types.length;
                    pendingWaveType = types[idx];
                    repaint();
                    return;
                }
                char c = Character.toUpperCase(e.getKeyChar());
                if (c == 'S') pendingWaveType = WaveType.SINE;
                if (c == 'Q') pendingWaveType = WaveType.SQUARE;
                if (c == 'T') pendingWaveType = WaveType.TRIANGLE;
                repaint();
                // Enter should confirm even on type field
                if (e.getKeyCode() == KeyEvent.VK_ENTER || (inputField == 0 && e.getKeyCode() == KeyEvent.VK_ENTER)) {
                    try {
                        double amp = Double.parseDouble(inputAmp);
                        double freq = Double.parseDouble(inputFreq);
                        double phase = parsePhaseInput(inputPhase);
                        Color color = availableColors[waves.size() % availableColors.length];
                        if (modifyMode) {
                            // Update selected wave
                            Wave wave = waves.get(selectedWave);
                            wave.amplitude = amp;
                            wave.frequency = freq;
                            wave.phaseShift = phase;
                            wave.type = pendingWaveType;
                        } else {
                            waves.add(new Wave(amp, freq, phase, color, pendingWaveType));
                            selectedWave = waves.size() - 1;
                        }
                        showWaveInputDialog = false;
                        inputError = false;
                        staticLayer = null;
                        repaint();
                    } catch (Exception ex) {
                        inputError = true;
                        repaint();
                    }
                    return;
                }
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER || (inputField == 0 && e.getKeyCode() == KeyEvent.VK_ENTER)) {
                try {
                    double amp = Double.parseDouble(inputAmp);
                    double freq = Double.parseDouble(inputFreq);
                    double phase = parsePhaseInput(inputPhase);
                    Color color = availableColors[waves.size() % availableColors.length];
                    if (modifyMode) {
                        // Update selected wave
                        Wave wave = waves.get(selectedWave);
                        wave.amplitude = amp;
                        wave.frequency = freq;
                        wave.phaseShift = phase;
                        wave.type = pendingWaveType;
                    } else {
                        waves.add(new Wave(amp, freq, phase, color, pendingWaveType));
                        selectedWave = waves.size() - 1;
                    }
                    showWaveInputDialog = false;
                    inputError = false;
                    staticLayer = null;
                    repaint();
                } catch (Exception ex) {
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
                if (inputField == 1 && inputAmp.length() > 0) inputAmp = inputAmp.substring(0, inputAmp.length() - 1);
                if (inputField == 2 && inputFreq.length() > 0) inputFreq = inputFreq.substring(0, inputFreq.length() - 1);
                if (inputField == 3 && inputPhase.length() > 0) inputPhase = inputPhase.substring(0, inputPhase.length() - 1);
                fieldEdited[inputField] = true;
                repaint();
                return;
            }
            // Accept numbers, dot, minus, and for phase: d, D, °
            char c = e.getKeyChar();
            if ((c >= '0' && c <= '9') || c == '.' || c == '-' || (inputField == 3 && (c == 'd' || c == 'D' || c == '°'))) {
                if (inputField > 0) {
                    if (!fieldEdited[inputField]) {
                        // Replace value on first keypress after focus
                        if (inputField == 1) inputAmp = "" + c;
                        if (inputField == 2) inputFreq = "" + c;
                        if (inputField == 3) inputPhase = "" + c;
                        fieldEdited[inputField] = true;
                    } else {
                        if (inputField == 1) inputAmp += c;
                        if (inputField == 2) inputFreq += c;
                        if (inputField == 3) inputPhase += c;
                    }
                    repaint();
                }
                return;
            }
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (!showWaveInputDialog && !showAnalysisView && !waves.isEmpty()) {
                showAnalysisView = true;
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
                inputPhase = Double.toString(Math.toDegrees(wave.phaseShift)); // Display in degrees
                inputField = 0;
                fieldEdited[0] = fieldEdited[1] = fieldEdited[2] = fieldEdited[3] = false;
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