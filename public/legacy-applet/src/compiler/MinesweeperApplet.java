/**
 * 
 * This is still being developed. And is very incomplete.
 * 
 */

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MinesweeperApplet extends Applet implements MouseListener, ActionListener, KeyListener {
    // Game constants
    private static final int MAX_WIDTH = 640;
    private static final int MAX_HEIGHT = 480;
    private static final int HEADER_HEIGHT = 60;
    private static final int MARGIN = 12;
    private static final int COUNTER_WIDTH = 52;
    private static final int COUNTER_HEIGHT = 28;
    private static final int MIN_CELL_SIZE = 12;
    private static final int MAX_CELL_SIZE = 24;
    
    private static final Color XP_GRAY = new Color(192, 192, 192);
    private static final Color XP_LIGHT_GRAY = new Color(223, 223, 223);
    private static final Color XP_DARK_GRAY = new Color(128, 128, 128);
    private static final Color XP_WHITE = new Color(255, 255, 255);
    private static final Color XP_BLACK = new Color(0, 0, 0);
    private static final Color XP_BUTTON_FACE = new Color(192, 192, 192);
    private static final Color XP_BUTTON_HIGHLIGHT = new Color(255, 255, 255);
    private static final Color XP_BUTTON_SHADOW = new Color(128, 128, 128);
    private static final Color XP_BUTTON_DARK_SHADOW = new Color(0, 0, 0);
    
    // LCD Display colors
    private static final Color LCD_BG = new Color(0, 0, 0);
    private static final Color LCD_ON = new Color(255, 0, 0);
    private static final Color LCD_OFF = new Color(64, 0, 0);
    
    // Cell colors
    private static final Color CELL_UNREVEALED = XP_GRAY;
    private static final Color CELL_REVEALED = XP_LIGHT_GRAY;
    private static final Color MINE_COLOR = Color.BLACK;
    private static final Color FLAG_COLOR = Color.RED;
    private static final Color MINE_EXPLOSION = Color.RED;
    
    private static final Color[] NUMBER_COLORS = {
        Color.BLUE,                    // 1
        new Color(0, 128, 0),         // 2 (green)
        Color.RED,                    // 3
        new Color(0, 0, 128),         // 4 (dark blue)
        new Color(128, 0, 0),         // 5 (maroon)
        new Color(0, 128, 128),       // 6 (teal)
        Color.BLACK,                  // 7
        new Color(128, 128, 128)      // 8 (gray)
    };
    
    // Difficulty levels
    private static final int EASY = 0;
    private static final int ADVANCED = 1;
    private static final int PROFESSIONAL = 2;
    
    // Game variables
    private int difficulty = EASY;
    private int rows, cols, numMines;
    private int cellSize;
    private int[][] board;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private boolean gameOver;
    private boolean gameWon;
    private boolean firstClick;
    private int minesLeft;
    private int cellsRevealed;
    private long startTime;
    private Font digitFont;
    private Font cellFont;
    private Font menuFont;
    
    // UI elements
    private int smileyX, smileyY;
    private int mineCounterX, mineCounterY;
    private int timeCounterX, timeCounterY;
    private int difficultyMenuX, difficultyMenuY;
    private boolean smileyPressed = false;
    private boolean showDifficultyMenu = false;
    private int explodedRow = -1, explodedCol = -1;
    
    // 7-segment display patterns (segments: a,b,c,d,e,f,g)
    // Layout:  aaa
    //         f   b
    //         f   b
    //          ggg
    //         e   c
    //         e   c
    //          ddd
    private static final boolean[][] SEVEN_SEGMENT = {
        // 0: a,b,c,d,e,f
        {true, true, true, true, true, true, false},
        // 1: b,c
        {false, true, true, false, false, false, false},
        // 2: a,b,g,e,d
        {true, true, false, true, true, false, true},
        // 3: a,b,g,c,d
        {true, true, true, true, false, false, true},
        // 4: f,g,b,c
        {false, true, true, false, false, true, true},
        // 5: a,f,g,c,d
        {true, false, true, true, false, true, true},
        // 6: a,f,g,e,d,c
        {true, false, true, true, true, true, true},
        // 7: a,b,c
        {true, true, true, false, false, false, false},
        // 8: all segments
        {true, true, true, true, true, true, true},
        // 9: a,b,c,d,f,g
        {true, true, true, true, false, true, true}
    };
    
    public void init() {
        setDifficulty(EASY);
        initializeGame();
        
        // Set up UI
        setBackground(XP_BUTTON_FACE);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        
        // Load fonts
        digitFont = new Font("Courier New", Font.BOLD, 14);
        menuFont = new Font("Arial", Font.PLAIN, 11);
        
        calculateLayout();
        setSize(MAX_WIDTH, MAX_HEIGHT);
    }
    
    private void setDifficulty(int diff) {
        difficulty = diff;
        switch (difficulty) {
            case EASY:
                rows = 9; cols = 9; numMines = 10;
                break;
            case ADVANCED:
                rows = 16; cols = 16; numMines = 40;
                break;
            case PROFESSIONAL:
                rows = 16; cols = 30; numMines = 99;
                break;
        }
        calculateLayout();
    }
    
    private void calculateLayout() {
        // Calculate maximum cell size that fits
        int availableWidth = MAX_WIDTH - 2 * MARGIN - 6;
        int availableHeight = MAX_HEIGHT - HEADER_HEIGHT - 2 * MARGIN - 6;
        
        int maxCellWidth = availableWidth / cols;
        int maxCellHeight = availableHeight / rows;
        
        cellSize = Math.max(MIN_CELL_SIZE, Math.min(MAX_CELL_SIZE, Math.min(maxCellWidth, maxCellHeight)));
        
        // Update font size based on cell size
        int fontSize = Math.max(10, Math.min(16, cellSize - 4));
        cellFont = new Font("Arial", Font.BOLD, fontSize);
        
        // Calculate positions
        int boardWidth = cols * cellSize;
        int boardStartX = (MAX_WIDTH - boardWidth) / 2;
        
        smileyX = MAX_WIDTH / 2 - 16;
        smileyY = MARGIN + 8; // Moved up more
        
        mineCounterX = boardStartX;
        mineCounterY = MARGIN + 8; // Moved up to align with smiley
        
        timeCounterX = boardStartX + boardWidth - COUNTER_WIDTH;
        timeCounterY = MARGIN + 8; // Moved up to align with smiley
        
        difficultyMenuX = MARGIN;
        difficultyMenuY = MARGIN + 45;
    }
    
    /**
     * @description: This basically initializes the whole Game.
     * @if_errors: Idk figure it out with a fucking debugger?
     */
    private void initializeGame() {
        board = new int[rows][cols];
        revealed = new boolean[rows][cols];
        flagged = new boolean[rows][cols];
        gameOver = false;
        gameWon = false;
        firstClick = true;
        minesLeft = numMines;
        cellsRevealed = 0;
        startTime = System.currentTimeMillis();
        smileyPressed = false;
        explodedRow = -1;
        explodedCol = -1;
        showDifficultyMenu = false;
        
        // Initialize board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = 0;
                revealed[r][c] = false;
                flagged[r][c] = false;
            }
        }
        
        repaint();
    }
    
    private void placeMines(int firstRow, int firstCol) {
        Random rand = new Random();
        int minesPlaced = 0;
        
        while (minesPlaced < numMines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            
            if (Math.abs(r - firstRow) <= 1 && Math.abs(c - firstCol) <= 1) {
                continue;
            }
            
            if (board[r][c] != -1) {
                board[r][c] = -1;
                minesPlaced++;
                
                // Update adjacent counts
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        
                        int nr = r + dr;
                        int nc = c + dc;
                        
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && board[nr][nc] != -1) {
                            board[nr][nc]++;
                        }
                    }
                }
            }
        }
    }
    
    public void paint(Graphics g) {
        // Draw main window border
        draw3DBorder(g, 0, 0, MAX_WIDTH, MAX_HEIGHT, true, 3);
        
        // Draw header area
        int headerY = MARGIN;
        int headerHeight = HEADER_HEIGHT - MARGIN;
        int headerWidth = MAX_WIDTH - 2 * MARGIN;
        draw3DBorder(g, MARGIN, headerY, headerWidth, headerHeight, false, 2);
        
        // Draw LCD mine counter (handle negative numbers)
        String mineText;
        if (minesLeft < 0) {
            mineText = String.format("-%02d", Math.abs(minesLeft));
        } else {
            mineText = String.format("%03d", minesLeft);
        }
        drawLCDCounter(g, mineCounterX, mineCounterY, mineText);
        
        // Draw smiley face
        drawSmiley(g, smileyX, smileyY);
        
        // Draw LCD time counter
        long elapsedTime = firstClick && !gameOver ? 0 : Math.min(999, (System.currentTimeMillis() - startTime) / 1000);
        drawLCDCounter(g, timeCounterX, timeCounterY, String.format("%03d", (int)elapsedTime));
        
        // Draw difficulty indicator
        drawDifficultyIndicator(g);
        
        // Draw board border
        int boardY = HEADER_HEIGHT + MARGIN;
        int boardWidth = cols * cellSize + 6;
        int boardHeight = rows * cellSize + 6;
        int boardX = (MAX_WIDTH - boardWidth) / 2;
        draw3DBorder(g, boardX, boardY, boardWidth, boardHeight, false, 3);
        
        // Draw game board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                drawCell(g, r, c);
            }
        }
        
        // Draw difficulty menu if visible
        if (showDifficultyMenu) {
            drawDifficultyMenu(g);
        }
    }
    
    private void draw3DBorder(Graphics g, int x, int y, int width, int height, boolean raised, int thickness) {
        Color highlight = raised ? XP_BUTTON_HIGHLIGHT : XP_BUTTON_DARK_SHADOW;
        Color shadow = raised ? XP_BUTTON_DARK_SHADOW : XP_BUTTON_HIGHLIGHT;
        Color midShadow = raised ? XP_BUTTON_SHADOW : XP_GRAY;
        
        for (int i = 0; i < thickness; i++) {
            g.setColor(i == 0 ? highlight : (raised ? XP_GRAY : XP_DARK_GRAY));
            g.drawLine(x + i, y + i, x + width - 1 - i, y + i);
            g.drawLine(x + i, y + i, x + i, y + height - 1 - i);
            
            g.setColor(i == thickness - 1 ? shadow : midShadow);
            g.drawLine(x + i, y + height - 1 - i, x + width - 1 - i, y + height - 1 - i);
            g.drawLine(x + width - 1 - i, y + i, x + width - 1 - i, y + height - 1 - i);
        }
        
        g.setColor(XP_BUTTON_FACE);
        g.fillRect(x + thickness, y + thickness, width - 2 * thickness, height - 2 * thickness);
    }
    
    private void drawLCDCounter(Graphics g, int x, int y, String text) {
        // Draw LCD background (recessed)
        g.setColor(LCD_BG);
        g.fillRect(x + 2, y + 2, COUNTER_WIDTH - 4, COUNTER_HEIGHT - 4);
        draw3DBorder(g, x, y, COUNTER_WIDTH, COUNTER_HEIGHT, false, 2);
        
        // Draw each digit using 7-segment display
        int digitWidth = 14;
        int digitSpacing = 2;
        int startX = x + 4;
        int startY = y + 4;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= '0' && c <= '9') {
                int digit = c - '0';
                drawSevenSegmentDigit(g, startX + i * (digitWidth + digitSpacing), startY, digit);
            } else if (c == '-') {
                // Draw minus sign as just the middle segment
                drawSevenSegmentMinus(g, startX + i * (digitWidth + digitSpacing), startY);
            }
        }
    }
    
    private void drawSevenSegmentDigit(Graphics g, int x, int y, int digit) {
        boolean[] segments = SEVEN_SEGMENT[digit];
        
        // First draw all segments in OFF state (dark red)
        g.setColor(LCD_OFF);
        drawAllSegments(g, x, y);
        
        // Then draw active segments in ON state (bright red)
        g.setColor(LCD_ON);
        drawActiveSegments(g, x, y, segments);
    }
    
    private void drawSevenSegmentMinus(Graphics g, int x, int y) {
        // Draw all segments off first
        g.setColor(LCD_OFF);
        drawAllSegments(g, x, y);
        
        // Draw only middle segment (g) for minus
        g.setColor(LCD_ON);
        drawSegmentG(g, x, y);
    }
    
    private void drawAllSegments(Graphics g, int x, int y) {
        drawSegmentA(g, x, y);
        drawSegmentB(g, x, y);
        drawSegmentC(g, x, y);
        drawSegmentD(g, x, y);
        drawSegmentE(g, x, y);
        drawSegmentF(g, x, y);
        drawSegmentG(g, x, y);
    }
    
    private void drawActiveSegments(Graphics g, int x, int y, boolean[] segments) {
        if (segments[0]) drawSegmentA(g, x, y); // a
        if (segments[1]) drawSegmentB(g, x, y); // b
        if (segments[2]) drawSegmentC(g, x, y); // c
        if (segments[3]) drawSegmentD(g, x, y); // d
        if (segments[4]) drawSegmentE(g, x, y); // e
        if (segments[5]) drawSegmentF(g, x, y); // f
        if (segments[6]) drawSegmentG(g, x, y); // g
    }
    
    // Individual segment drawing methods
    private void drawSegmentA(Graphics g, int x, int y) {
        // Top horizontal segment
        g.fillRect(x + 2, y, 8, 2);
    }
    
    private void drawSegmentB(Graphics g, int x, int y) {
        // Top right vertical segment
        g.fillRect(x + 10, y + 2, 2, 6);
    }
    
    private void drawSegmentC(Graphics g, int x, int y) {
        // Bottom right vertical segment
        g.fillRect(x + 10, y + 10, 2, 6);
    }
    
    private void drawSegmentD(Graphics g, int x, int y) {
        // Bottom horizontal segment
        g.fillRect(x + 2, y + 16, 8, 2);
    }
    
    private void drawSegmentE(Graphics g, int x, int y) {
        // Bottom left vertical segment
        g.fillRect(x, y + 10, 2, 6);
    }
    
    private void drawSegmentF(Graphics g, int x, int y) {
        // Top left vertical segment
        g.fillRect(x, y + 2, 2, 6);
    }
    
    private void drawSegmentG(Graphics g, int x, int y) {
        // Middle horizontal segment
        g.fillRect(x + 2, y + 8, 8, 2);
    }
    
    private void drawDifficultyIndicator(Graphics g) {
        String[] diffNames = {"Easy", "Advanced", "Professional"};
        g.setColor(XP_BLACK);
        g.setFont(menuFont);
        String text = diffNames[difficulty] + " (← → to change)";
        g.drawString(text, MARGIN + 5, MAX_HEIGHT - MARGIN - 5);
    }
    
    private void drawDifficultyMenu(Graphics g) {
        int menuWidth = 120;
        int menuHeight = 80;
        int menuX = difficultyMenuX;
        int menuY = difficultyMenuY;
        
        // Draw menu background
        draw3DBorder(g, menuX, menuY, menuWidth, menuHeight, true, 2);
        
        g.setColor(XP_BLACK);
        g.setFont(menuFont);
        
        String[] options = {"← Easy (9x9, 10)", "Advanced (16x16, 40)", "Professional (30x16, 99) →"};
        for (int i = 0; i < options.length; i++) {
            if (i == difficulty) {
                g.setColor(XP_BUTTON_SHADOW);
                g.fillRect(menuX + 4, menuY + 15 + i * 18, menuWidth - 8, 16);
                g.setColor(XP_WHITE);
            } else {
                g.setColor(XP_BLACK);
            }
            g.drawString(options[i], menuX + 6, menuY + 26 + i * 18);
        }
    }
    
    private void drawSmiley(Graphics g, int x, int y) {
        int size = 32;
        
        draw3DBorder(g, x - 3, y - 3, size + 6, size + 6, !smileyPressed, 2);
        
        g.setColor(Color.YELLOW);
        g.fillOval(x + 1, y + 1, size - 2, size - 2);
        g.setColor(XP_BLACK);
        g.drawOval(x + 1, y + 1, size - 2, size - 2);
        
        if (gameOver && !gameWon) {
            g.drawLine(x + 8, y + 9, x + 12, y + 13);
            g.drawLine(x + 12, y + 9, x + 8, y + 13);
            g.drawLine(x + 20, y + 9, x + 24, y + 13);
            g.drawLine(x + 24, y + 9, x + 20, y + 13);
            g.drawOval(x + 12, y + 20, 8, 5);
        } else if (gameWon) {
            g.fillRect(x + 7, y + 10, 6, 4);
            g.fillRect(x + 19, y + 10, 6, 4);
            g.drawRect(x + 6, y + 9, 8, 6);
            g.drawRect(x + 18, y + 9, 8, 6);
            g.drawLine(x + 14, y + 11, x + 18, y + 11);
            g.drawArc(x + 10, y + 18, 12, 8, 0, -180);
        } else {
            g.fillOval(x + 10, y + 10, 3, 3);
            g.fillOval(x + 19, y + 10, 3, 3);
            g.drawArc(x + 11, y + 18, 10, 6, 0, -180);
        }
    }
    
    private void drawCell(Graphics g, int row, int col) {
        int boardStartX = (MAX_WIDTH - cols * cellSize) / 2;
        int x = boardStartX + 3 + col * cellSize;
        int y = HEADER_HEIGHT + MARGIN + 3 + row * cellSize;
        
        if (revealed[row][col]) {
            g.setColor(CELL_REVEALED);
            g.fillRect(x, y, cellSize, cellSize);
            
            g.setColor(XP_DARK_GRAY);
            g.drawLine(x, y, x + cellSize - 1, y);
            g.drawLine(x, y, x, y + cellSize - 1);
            
            if (board[row][col] == -1) {
                if (row == explodedRow && col == explodedCol) {
                    g.setColor(MINE_EXPLOSION);
                    g.fillRect(x, y, cellSize, cellSize);
                }
                g.setColor(MINE_COLOR);
                int mineSize = Math.max(4, cellSize - 6);
                g.fillOval(x + (cellSize - mineSize) / 2, y + (cellSize - mineSize) / 2, mineSize, mineSize);
                g.setColor(XP_WHITE);
                int highlightSize = Math.max(2, mineSize / 3);
                g.fillOval(x + (cellSize - mineSize) / 2 + 2, y + (cellSize - mineSize) / 2 + 2, highlightSize, highlightSize);
            } else if (board[row][col] > 0) {
                g.setColor(NUMBER_COLORS[board[row][col] - 1]);
                g.setFont(cellFont);
                String num = Integer.toString(board[row][col]);
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(num);
                int textX = x + (cellSize - textWidth) / 2;
                int textY = y + (cellSize + fm.getAscent()) / 2 - 1;
                g.drawString(num, textX, textY);
            }
        } else {
            g.setColor(CELL_UNREVEALED);
            g.fillRect(x, y, cellSize, cellSize);
            
            g.setColor(XP_WHITE);
            g.drawLine(x, y, x + cellSize - 2, y);
            g.drawLine(x, y, x, y + cellSize - 2);
            
            g.setColor(XP_DARK_GRAY);
            g.drawLine(x + cellSize - 1, y + 1, x + cellSize - 1, y + cellSize - 1);
            g.drawLine(x + 1, y + cellSize - 1, x + cellSize - 1, y + cellSize - 1);
            
            g.setColor(XP_BUTTON_SHADOW);
            g.drawLine(x + cellSize - 2, y + 1, x + cellSize - 2, y + cellSize - 2);
            g.drawLine(x + 1, y + cellSize - 2, x + cellSize - 2, y + cellSize - 2);
            
            if (flagged[row][col]) {
                int flagSize = Math.max(8, cellSize - 4);
                int poleX = x + cellSize / 2;
                int baseY = y + cellSize - 3;
                
                g.setColor(XP_BLACK);
                g.drawLine(poleX, y + 2, poleX, baseY);
                
                g.setColor(FLAG_COLOR);
                int[] flagX = {poleX + 1, poleX + flagSize / 2 + 2, poleX + 1, poleX + 1};
                int[] flagY = {y + 2, y + flagSize / 3 + 2, y + flagSize / 2 + 2, y + 2};
                g.fillPolygon(flagX, flagY, 4);
                
                g.setColor(XP_BLACK);
                g.drawLine(poleX - 2, baseY, poleX + 2, baseY);
            }
        }
    }
    
    private void revealCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols || 
            revealed[row][col] || flagged[row][col] || gameOver) {
            return;
        }
        
        revealed[row][col] = true;
        cellsRevealed++;
        
        if (board[row][col] == -1) {
            explodedRow = row;
            explodedCol = col;
            gameOver = true;
            revealAllMines();
            repaint();
            return;
        }
        
        if (board[row][col] == 0) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    revealCell(row + dr, col + dc);
                }
            }
        }
        
        if (cellsRevealed == rows * cols - numMines) {
            gameWon = true;
            gameOver = true;
            flagAllMines();
        }
        
        repaint();
    }
    
    private void revealAllMines() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == -1) {
                    revealed[r][c] = true;
                }
            }
        }
    }
    
    private void flagAllMines() {
        minesLeft = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == -1 && !flagged[r][c]) {
                    flagged[r][c] = true;
                }
            }
        }
    }
    
    public void mouseClicked(MouseEvent e) {
        if (showDifficultyMenu) {
            handleDifficultyMenuClick(e);
            return;
        }
        
        if (e.getX() >= smileyX - 3 && e.getX() <= smileyX + 35 &&
            e.getY() >= smileyY - 3 && e.getY() <= smileyY + 35) {
            initializeGame();
            return;
        }
        
        if (gameOver) return;
        
        int boardStartX = (MAX_WIDTH - cols * cellSize) / 2;
        int boardY = HEADER_HEIGHT + MARGIN + 3;
        int x = e.getX() - boardStartX - 3;
        int y = e.getY() - boardY;
        
        if (x < 0 || x >= cols * cellSize || y < 0 || y >= rows * cellSize) {
            return;
        }
        
        int row = y / cellSize;
        int col = x / cellSize;
        
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (firstClick) {
                firstClick = false;
                placeMines(row, col);
                startTime = System.currentTimeMillis();
            }
            
            if (!flagged[row][col]) {
                revealCell(row, col);
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (!revealed[row][col]) {
                flagged[row][col] = !flagged[row][col];
                minesLeft += flagged[row][col] ? -1 : 1;
                repaint();
            }
        }
    }
    
    private void handleDifficultyMenuClick(MouseEvent e) {
        int menuX = difficultyMenuX;
        int menuY = difficultyMenuY;
        
        if (e.getX() >= menuX && e.getX() <= menuX + 120 &&
            e.getY() >= menuY && e.getY() <= menuY + 80) {
            
            int option = (e.getY() - menuY - 15) / 18;
            if (option >= 0 && option < 3) {
                setDifficulty(option);
                initializeGame();
            }
        }
        showDifficultyMenu = false;
    }
    
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= smileyX - 3 && e.getX() <= smileyX + 35 &&
            e.getY() >= smileyY - 3 && e.getY() <= smileyY + 35) {
            smileyPressed = true;
            repaint();
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        if (smileyPressed) {
            smileyPressed = false;
            repaint();
        }
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            showDifficultyMenu = !showDifficultyMenu;
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            // Previous difficulty
            int newDiff = (difficulty - 1 + 3) % 3;
            setDifficulty(newDiff);
            initializeGame();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            // Next difficulty
            int newDiff = (difficulty + 1) % 3;
            setDifficulty(newDiff);
            initializeGame();
        }
    }
    
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    public void actionPerformed(ActionEvent e) {}
    
    public void start() {
        Thread timerThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (!firstClick && !gameOver) {
                        repaint();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }
}