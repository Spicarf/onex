package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import db.DatabaseManager;
import game.Board;
import game.GameEndType;
import game.PathFinder;
import game.Tile;

public class GamePanel extends JPanel {
    private MainFrame mainApp;
    private JPanel boardPanel;
    private Board board;
    private PathFinder pathFinder;
    private Tile selectedTile1, selectedTile2;
    private Timer gameTimer;
    private int timeLeft;
    private int score = 0;
    private int currentDifficulty = 1;
    private JLabel infoLabel;
    private boolean gameRunning = false;

    private List<Point> currentPath = new ArrayList<>();
    private Timer pathClearTimer;

    // Button Shuffle
    private JButton btnShuffle;

    public GamePanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.BG_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        infoLabel = new JLabel("Level: Easy | Time: 0 | Score: 0");
        infoLabel.setFont(Theme.HEADER_FONT);
        infoLabel.setForeground(Theme.TEXT_WHITE);
        topPanel.add(infoLabel, BorderLayout.CENTER);

        // Panel tombol Kanan (Shuffle & Pause)
        JPanel rightBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightBtnPanel.setBackground(Theme.BG_COLOR);

        // TOMBOL SHUFFLE
        btnShuffle = new JButton("SHUFFLE");
        btnShuffle.setFont(Theme.NORMAL_FONT);
        btnShuffle.setBackground(Theme.TILE_BG);
        btnShuffle.setForeground(Theme.TEXT_WHITE);
        btnShuffle.setFocusPainted(false);
        btnShuffle.addActionListener(e -> performShuffle()); // Aksi Manual
        rightBtnPanel.add(btnShuffle);

        JButton btnPause = new JButton("PAUSE");
        btnPause.setFont(Theme.NORMAL_FONT);
        btnPause.setBackground(Color.ORANGE);
        btnPause.setForeground(Color.BLACK);
        btnPause.setFocusPainted(false);
        btnPause.addActionListener(e -> pauseGame());
        rightBtnPanel.add(btnPause);

        topPanel.add(rightBtnPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- BOARD PANEL ---
        boardPanel = new JPanel() {
            @Override
            protected void paintChildren(Graphics g) {
                super.paintChildren(g);
                if (!currentPath.isEmpty() && board != null) {
                    drawPath((Graphics2D) g);
                }
            }
        };
        boardPanel.setBackground(Theme.BG_COLOR);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(boardPanel, BorderLayout.CENTER);

        gameTimer = new Timer(1000, (ActionEvent e) -> {
            if (timeLeft > 0) {
                timeLeft--;
                updateInfo();
            } else {
                stopGame(GameEndType.TIME_UP);
            }
        });

        pathClearTimer = new Timer(300, e -> clearPathAndRemoveTiles());
        pathClearTimer.setRepeats(false);
    }

    // --- LOGIKA SHUFFLE ---

    private void performShuffle() {
        if (!gameRunning)
            return;

        // Lakukan pengacakan
        board.shuffleRemainingTiles();

        // Reset seleksi jika ada
        selectedTile1 = null;
        selectedTile2 = null;

        // Visual feedback
        boardPanel.repaint();
        JOptionPane.showMessageDialog(this, "Board Diacak Ulang!");

        // Cek lagi apakah setelah diacak masih deadlock? (Jarang terjadi tapi mungkin)
        checkDeadlock();
    }

    // --- LOGIKA DETEKSI DEADLOCK (JALAN BUNTU) ---

    private void checkDeadlock() {
        if (!hasPossibleMove()) {
            // Cek apakah board sudah kosong (Menang) atau masih ada sisa tapi buntu
            if (isBoardEmpty()) {
                // Sudah ditangani checkWin, jadi abaikan
                return;
            }

            // Jika masih ada tile tapi tidak ada jalan -> SHUFFLE OTOMATIS
            Timer delayShuffle = new Timer(500, e -> {
                JOptionPane.showMessageDialog(this, "Tidak ada langkah tersedia! Mengacak ulang...");
                board.shuffleRemainingTiles();
                boardPanel.repaint();
                ((Timer) e.getSource()).stop();

                // Rekursif cek, siapa tau abis shuffle masih buntu
                checkDeadlock();
            });
            delayShuffle.setRepeats(false);
            delayShuffle.start();
        }
    }

    // Algoritma Brute Force: Cek semua kemungkinan pasangan
    private boolean hasPossibleMove() {
        java.util.List<Tile> activeTiles = new ArrayList<>();

        // 1. Kumpulkan tile aktif
        for (int r = 1; r < board.rows - 1; r++) {
            for (int c = 1; c < board.cols - 1; c++) {
                Tile t = board.getTile(r, c);
                if (!t.isRemoved)
                    activeTiles.add(t);
            }
        }

        // 2. Cek setiap pasangan
        for (int i = 0; i < activeTiles.size(); i++) {
            for (int j = i + 1; j < activeTiles.size(); j++) {
                Tile t1 = activeTiles.get(i);
                Tile t2 = activeTiles.get(j);

                // Jika ID sama, cek apakah bisa terhubung
                if (t1.id == t2.id) {
                    if (pathFinder.canConnect(t1, t2)) {
                        System.out.println("Hint: Ada jalan di " + t1.id); // Debug
                        return true; // Ada minimal 1 jalan
                    }
                }
            }
        }

        return false; // Tidak ada jalan sama sekali
    }

    private boolean isBoardEmpty() {
        for (int r = 1; r < board.rows - 1; r++) {
            for (int c = 1; c < board.cols - 1; c++) {
                if (!board.tiles[r][c].isRemoved)
                    return false;
            }
        }
        return true;
    }

    // --- UPDATE METHOD clearPathAndRemoveTiles ---
    // Panggil checkDeadlock setelah tile hilang

    private void clearPathAndRemoveTiles() {
        currentPath.clear();
        boardPanel.repaint();

        if (selectedTile1 != null && selectedTile2 != null) {
            selectedTile1.markAsRemoved();
            selectedTile2.markAsRemoved();

            // Reset border
            if (selectedTile1 != null)
                selectedTile1.setBorder(BorderFactory.createLineBorder(Theme.BG_COLOR, 2));
            if (selectedTile2 != null)
                selectedTile2.setBorder(BorderFactory.createLineBorder(Theme.BG_COLOR, 2));
        }

        selectedTile1 = null;
        selectedTile2 = null;

        // Cek Kemenangan dulu
        checkWin();

        // Jika belum menang, Cek Deadlock
        if (gameRunning) {
            checkDeadlock();
        }
    }

    // --- SISA CODE (DrawPath, HandleClick, dll) TETAP SAMA ---

    private void drawPath(Graphics2D g2d) {
        g2d.setColor(Theme.PATH_COLOR);
        g2d.setStroke(new BasicStroke(5));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (boardPanel.getComponentCount() == 0)
            return;

        Component c = boardPanel.getComponent(0);
        int tileW = c.getWidth();
        int tileH = c.getHeight();

        for (int i = 0; i < currentPath.size() - 1; i++) {
            Point p1 = currentPath.get(i);
            Point p2 = currentPath.get(i + 1);
            int x1 = p1.x * (tileW + 2) + tileW / 2;
            int y1 = p1.y * (tileH + 2) + tileH / 2;
            int x2 = p2.x * (tileW + 2) + tileW / 2;
            int y2 = p2.y * (tileH + 2) + tileH / 2;
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    private void handleTileClick(Tile t) {
        if (!gameRunning || t.isRemoved || pathClearTimer.isRunning())
            return;
        if (t == selectedTile1)
            return;

        t.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));

        if (selectedTile1 == null) {
            selectedTile1 = t;
        } else {
            selectedTile2 = t;
            checkMatch();
        }
    }

    private void checkMatch() {
        List<Point> path = pathFinder.getConnectingPath(selectedTile1, selectedTile2);

        if (!path.isEmpty()) {
            score += 10;
            updateInfo();
            currentPath = path;
            boardPanel.repaint();
            pathClearTimer.start();
        } else {
            if (selectedTile1 != null)
                selectedTile1.setBorder(BorderFactory.createLineBorder(Theme.BG_COLOR, 2));
            if (selectedTile2 != null)
                selectedTile2.setBorder(BorderFactory.createLineBorder(Theme.BG_COLOR, 2));
            selectedTile1 = null;
            selectedTile2 = null;
        }
    }

    private void checkWin() {
        if (isBoardEmpty())
            stopGame(GameEndType.BOARD_CLEAR);
    }

    private void pauseGame() {
        if (!gameRunning)
            return;
        gameRunning = false;
        gameTimer.stop();
        if (pathClearTimer.isRunning())
            pathClearTimer.stop();
        mainApp.showPauseScreen();
    }

    public void resumeGame() {
        gameRunning = true;
        gameTimer.start();
        if (!currentPath.isEmpty())
            pathClearTimer.start();
        updateInfo();
    }

    public void startGame(int difficulty) {
        this.currentDifficulty = difficulty;
        score = 0;
        selectedTile1 = null;
        selectedTile2 = null;
        currentPath.clear();
        boardPanel.removeAll();

        int rows = 0, cols = 0;
        switch (difficulty) {
            case 1:
                rows = 4;
                cols = 6;
                timeLeft = 60;
                break;
            case 2:
                rows = 6;
                cols = 8;
                timeLeft = 120;
                break;
            case 3:
                rows = 8;
                cols = 12;
                timeLeft = 180;
                break;
        }

        board = new Board(rows, cols);
        pathFinder = new PathFinder(board);

        boardPanel.setLayout(new GridLayout(board.rows, board.cols, 2, 2));

        for (int r = 0; r < board.rows; r++) {
            for (int c = 0; c < board.cols; c++) {
                Tile t = board.getTile(r, c);
                t.addActionListener(e -> handleTileClick(t));
                boardPanel.add(t);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();

        gameRunning = true;
        gameTimer.start();
        updateInfo();

        // Cek deadlock di awal game (siapa tau generate sial banget)
        checkDeadlock();
    }

    private void updateInfo() {
        String levelName = (currentDifficulty == 1) ? "EASY" : (currentDifficulty == 2) ? "MEDIUM" : "HARD";
        infoLabel.setText("Level: " + levelName + " | Time: " + timeLeft + " | Score: " + score);
    }

    public void stopGame(GameEndType type) {
        gameRunning = false;
        gameTimer.stop();
        pathClearTimer.stop();

        if (type == GameEndType.USER_QUIT) {
            mainApp.showPanel("MENU");
            return;
        }

        String msg = (type == GameEndType.BOARD_CLEAR) ? "YOU WIN!" : "GAME OVER!";
        JOptionPane.showMessageDialog(this, msg + "\nFinal Score: " + score);

        int maxTime = (currentDifficulty == 1 ? 60 : currentDifficulty == 2 ? 120 : 180);
        int durationPlayed = maxTime - timeLeft;
        String diffName = (currentDifficulty == 1) ? "Easy" : (currentDifficulty == 2) ? "Medium" : "Hard";

        DatabaseManager.saveScore(mainApp.getCurrentUserId(), score, durationPlayed, diffName);
        mainApp.showPanel("MENU");
    }
}