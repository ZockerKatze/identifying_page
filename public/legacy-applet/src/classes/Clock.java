import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * You can freely reuse this Code. This Code is licensed under MiT (see at bottom)
 * It was precompiled and will be recompiled every Update
 */
public class Clock extends Applet {
    private final int WIDTH = 640;
    private final int HEIGHT = 480;
    private BufferedImage buffer;
    private Graphics2D gBuffer;
    private String timeStr = "00:00:00";
    private String dateStr = "";
    private ZoneId currentZone = ZoneId.of("Europe/Vienna");
    private final List<String> zoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds())
        .stream().sorted().toList();
    private int zoneIndex = zoneIds.indexOf("Europe/Vienna");

    public void init() {
        setSize(WIDTH, HEIGHT);
        setFocusable(true); // Needed to receive key input

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        gBuffer = buffer.createGraphics();
        gBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Timer update every second
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ZonedDateTime now = ZonedDateTime.now(currentZone);
                timeStr = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                dateStr = now.format(DateTimeFormatter.ofPattern("EEE MMM dd yyyy")) + " (" + currentZone + ")";
                drawClock();
                repaint();
            }
        }, 0, 1000);

        // Key listener for zone switching
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    zoneIndex = (zoneIndex + 1) % zoneIds.size();
                    currentZone = ZoneId.of(zoneIds.get(zoneIndex));
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    zoneIndex = (zoneIndex - 1 + zoneIds.size()) % zoneIds.size();
                    currentZone = ZoneId.of(zoneIds.get(zoneIndex));
                }
            }
        });
    }

    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, this);
    }

    public void update(Graphics g) {
        paint(g);
    }

    private void drawClock() {
        gBuffer.setColor(Color.black);
        gBuffer.fillRect(0, 0, WIDTH, HEIGHT);
        gBuffer.setColor(Color.white);

        // Draw date 
        Font dateFont = new Font("Monospaced", Font.PLAIN, 18);
        gBuffer.setFont(dateFont);
        FontMetrics fm = gBuffer.getFontMetrics();
        int dateWidth = fm.stringWidth(dateStr);
        int dateX = (WIDTH - dateWidth) / 2;
        int dateY = HEIGHT / 2 - 50;
        gBuffer.drawString(dateStr, dateX, dateY);

        int digitWidth = 40;
        int digitHeight = 80;
        int spacing = 10;
        int totalWidth = timeStr.length() * (digitWidth + spacing);
        int startX = (WIDTH - totalWidth + spacing) / 2;
        int y = (HEIGHT - digitHeight) / 2;

        for (int i = 0; i < timeStr.length(); i++) {
            char c = timeStr.charAt(i);
            int x = startX + i * (digitWidth + spacing);
            if (c == ':') {
                drawColon(gBuffer, x, y + 10, digitWidth, digitHeight);
            } else {
                drawSevenSegmentDigit(gBuffer, c - '0', x, y, digitWidth, digitHeight);
            }
        }
    }

    private void drawColon(Graphics2D g, int x, int y, int w, int h) {
        int dotSize = w / 4;
        g.fillOval(x + w / 2 - dotSize / 2, y + h / 3 - dotSize / 2, dotSize, dotSize);
        g.fillOval(x + w / 2 - dotSize / 2, y + 2 * h / 3 - dotSize / 2, dotSize, dotSize);
    }

    private void drawSevenSegmentDigit(Graphics2D g, int digit, int x, int y, int w, int h) {
        boolean[][] segments = {
            /* Segments for the LCD Segmented Display */
            {true, true, true, true, true, true, false},   // 0
            {false, true, true, false, false, false, false}, // 1
            {true, true, false, true, true, false, true},    // 2
            {true, true, true, true, false, false, true},    // 3
            {false, true, true, false, false, true, true},   // 4
            {true, false, true, true, false, true, true},    // 5
            {true, false, true, true, true, true, true},     // 6
            {true, true, true, false, false, false, false}, // 7
            {true, true, true, true, true, true, true},    // 8
            {true, true, true, true, false, true, true}      // 9
        };
        boolean[] seg = segments[digit];
        Polygon[] segmentPolygons = createSegments(x, y, w, h);
        for (int i = 0; i < 7; i++) {
            if (seg[i]) {
                g.fillPolygon(segmentPolygons[i]);
            }
        }
    }

    private Polygon[] createSegments(int x, int y, int w, int h) {
        int thickness = w / 6;
        int slant = thickness / 2;
        Polygon[] segments = new Polygon[7];

        segments[0] = new Polygon(
            new int[]{x + slant, x + w - slant, x + w - thickness, x + thickness},
            new int[]{y, y, y + thickness, y + thickness}, 4);

        segments[1] = new Polygon(
            new int[]{x + w - thickness, x + w, x + w, x + w - thickness},
            new int[]{y + thickness, y + thickness + slant, y + h / 2 - slant, y + h / 2}, 4);

        segments[2] = new Polygon(
            new int[]{x + w - thickness, x + w, x + w, x + w - thickness},
            new int[]{y + h / 2, y + h / 2 + slant, y + h - thickness - slant, y + h - thickness}, 4);

        segments[3] = new Polygon(
            new int[]{x + thickness, x + w - thickness, x + w - slant, x + slant},
            new int[]{y + h - thickness, y + h - thickness, y + h, y + h}, 4);

        segments[4] = new Polygon(
            new int[]{x, x + thickness, x + thickness, x},
            new int[]{y + h - thickness - slant, y + h - thickness, y + h / 2 + slant, y + h / 2}, 4);

        segments[5] = new Polygon(
            new int[]{x, x + thickness, x + thickness, x},
            new int[]{y + h / 2, y + h / 2 - slant, y + thickness + slant, y + thickness}, 4);

        segments[6] = new Polygon(
            new int[]{x + thickness, x + w - thickness, x + w - slant, x + slant},
            new int[]{y + h / 2 - thickness / 2, y + h / 2 - thickness / 2, y + h / 2 + thickness / 2, y + h / 2 + thickness / 2}, 4);

        return segments;
    }
}

/*
src@wikipedia:
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
*/