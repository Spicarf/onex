package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
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

    private JButton btnShuffle;
    private boolean isShuffling = false;

    private Clip bgmClip;

    public GamePanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.BG_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        infoLabel = new JLabel("Level: Easy | Time: 0 | Score: 0");
        infoLabel.setFont(Theme.HEADER_FONT);
        infoLabel.setForeground(Theme.TEXT_WHITE);
        topPanel.add(infoLabel, BorderLayout.CENTER);

        JPanel rightBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightBtnPanel.setBackground(Theme.BG_COLOR);

        btnShuffle = new JButton("SHUFFLE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.TILE_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                super.paintComponent(g);
            }
        };
        styleButton(btnShuffle);
        btnShuffle.addActionListener(e -> performManualShuffle());
        rightBtnPanel.add(btnShuffle);

        JButton btnPause = new JButton("PAUSE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT_COLOR);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                super.paintComponent(g);
            }
        };
        styleButton(btnPause);
        btnPause.addActionListener(e -> pauseGame());
        rightBtnPanel.add(btnPause);

        topPanel.add(rightBtnPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        boardPanel = new JPanel() {
            @Override
            protected void paintChildren(Graphics g) {
                super.paintChildren(g);
                if (!currentPath.isEmpty() && board != null) {
                    drawPath((Graphics2D) g);
                }
                if (isShuffling) {
                    drawShuffleOverlay((Graphics2D) g);
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

    private void styleButton(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setFont(Theme.NORMAL_FONT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(100, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void startGame(int difficulty) {
        this.currentDifficulty = difficulty;
        score = 0;
        selectedTile1 = null;
        selectedTile2 = null;
        currentPath.clear();
        boardPanel.removeAll();

        playSound("gamestart.wav");

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

        playBGM("bgm.wav");

        Timer initCheck = new Timer(500, e -> {
            checkDeadlock();
            ((Timer) e.getSource()).stop();
        });
        initCheck.setRepeats(false);
        initCheck.start();
    }

    private void handleTileClick(Tile t) {
        if (!gameRunning || t.isRemoved || pathClearTimer.isRunning() || isShuffling)
            return;
        if (t == selectedTile1)
            return;

        playSound("click.wav");
        t.setSelected(true);

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

            playSound("match.wav");
            pathClearTimer.start();
        } else {
            playSound("error.wav");
            final Tile t1 = selectedTile1;
            final Tile t2 = selectedTile2;

            Timer resetTimer = new Timer(300, e -> {
                if (t1 != null)
                    t1.setSelected(false);
                if (t2 != null)
                    t2.setSelected(false);
                ((Timer) e.getSource()).stop();
            });
            resetTimer.setRepeats(false);
            resetTimer.start();

            selectedTile1 = null;
            selectedTile2 = null;
        }
    }

    private void clearPathAndRemoveTiles() {
        currentPath.clear();
        boardPanel.repaint();

        if (selectedTile1 != null && selectedTile2 != null) {
            selectedTile1.markAsRemoved();
            selectedTile2.markAsRemoved();
            selectedTile1.setSelected(false);
            selectedTile2.setSelected(false);
        }

        selectedTile1 = null;
        selectedTile2 = null;

        checkWin();
        if (gameRunning)
            checkDeadlock();
    }

    private void performManualShuffle() {
        if (!gameRunning || isShuffling)
            return;
        startShuffleAnimation();
    }

    private void checkDeadlock() {
        if (!hasPossibleMove()) {
            if (isBoardEmpty())
                return;
            Timer autoShuffle = new Timer(800, e -> {
                startShuffleAnimation();
                ((Timer) e.getSource()).stop();
            });
            autoShuffle.setRepeats(false);
            autoShuffle.start();
        }
    }

    private void startShuffleAnimation() {
        isShuffling = true;
        playSound("shuffle.wav");
        boardPanel.repaint();

        Timer delay = new Timer(1000, e -> {
            board.shuffleRemainingTiles();
            if (selectedTile1 != null)
                selectedTile1.setSelected(false);
            if (selectedTile2 != null)
                selectedTile2.setSelected(false);
            selectedTile1 = null;
            selectedTile2 = null;

            isShuffling = false;
            boardPanel.repaint();
            checkDeadlock();
            ((Timer) e.getSource()).stop();
        });
        delay.setRepeats(false);
        delay.start();
    }

    private boolean hasPossibleMove() {
        java.util.List<Tile> activeTiles = new ArrayList<>();
        for (int r = 1; r < board.rows - 1; r++) {
            for (int c = 1; c < board.cols - 1; c++) {
                Tile t = board.getTile(r, c);
                if (!t.isRemoved)
                    activeTiles.add(t);
            }
        }
        for (int i = 0; i < activeTiles.size(); i++) {
            for (int j = i + 1; j < activeTiles.size(); j++) {
                Tile t1 = activeTiles.get(i);
                Tile t2 = activeTiles.get(j);
                if (t1.id == t2.id) {
                    if (pathFinder.canConnect(t1, t2))
                        return true;
                }
            }
        }
        return false;
    }

    private void checkWin() {
        if (isBoardEmpty()) {
            stopGame(GameEndType.BOARD_CLEAR);
        }
    }

    private void pauseGame() {
        if (!gameRunning)
            return;
        gameRunning = false;
        gameTimer.stop();
        if (pathClearTimer.isRunning())
            pathClearTimer.stop();

        if (bgmClip != null && bgmClip.isRunning())
            bgmClip.stop();

        mainApp.showPauseScreen();
    }

    public void resumeGame() {
        gameRunning = true;
        gameTimer.start();
        if (!currentPath.isEmpty())
            pathClearTimer.start();

        if (bgmClip != null) {
            bgmClip.start();
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        updateInfo();
    }

    public void stopGame(GameEndType type) {
        gameRunning = false;
        gameTimer.stop();
        if (pathClearTimer.isRunning())
            pathClearTimer.stop();

        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close();
        }

        if (type == GameEndType.USER_QUIT) {
            mainApp.showPanel("MENU");
            return;
        }

        boolean isWin = (type == GameEndType.BOARD_CLEAR);

        if (isWin)
            playSound("win.wav");
        else
            playSound("gameover.wav");

        int maxTime = (currentDifficulty == 1 ? 60 : currentDifficulty == 2 ? 120 : 180);
        int durationPlayed = maxTime - timeLeft;
        String diffName = (currentDifficulty == 1) ? "Easy" : (currentDifficulty == 2) ? "Medium" : "Hard";

        DatabaseManager.saveScore(mainApp.getCurrentUserId(), score, durationPlayed, diffName);

        mainApp.showGameOverScreen(isWin, score, durationPlayed, diffName);
    }

    private void playSound(String filename) {
        new Thread(() -> {
            try {
                AudioInputStream audioIn = loadAudioStream(filename);
                if (audioIn == null)
                    return;
                Clip clip = AudioSystem.getClip(null);
                clip.open(audioIn);
                setVolume(clip, 0.0f);
                clip.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP)
                        clip.close();
                });
                clip.start();
            } catch (Exception e) {
            }
        }).start();
    }

    private void playBGM(String filename) {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
        new Thread(() -> {
            try {
                AudioInputStream audioIn = loadAudioStream(filename);
                if (audioIn == null)
                    return;
                bgmClip = AudioSystem.getClip(null);
                bgmClip.open(audioIn);
                setVolume(bgmClip, -10.0f);
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip.start();
            } catch (Exception e) {
            }
        }).start();
    }

    private AudioInputStream loadAudioStream(String filename) {
        try {
            URL url = getClass().getClassLoader().getResource("resources/sounds/" + filename);
            if (url == null) {
                File f = new File("resources/sounds/" + filename);
                if (f.exists())
                    url = f.toURI().toURL();
            }
            if (url != null) {
                InputStream audioSrc = url.openStream();
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                return AudioSystem.getAudioInputStream(bufferedIn);
            }
        } catch (Exception e) {
        }
        return null;
    }

    private void setVolume(Clip clip, float val) {
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gain.setValue(val);
        } catch (Exception e) {
        }
    }

    // --- UTILS ---
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

    private void drawShuffleOverlay(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Theme.PRIMARY_COLOR);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
        String text = "SHUFFLING...";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, getHeight() / 2);
    }

    private void updateInfo() {
        String levelName = (currentDifficulty == 1) ? "EASY" : (currentDifficulty == 2) ? "MEDIUM" : "HARD";
        String player = (mainApp.getCurrentUsername() != null) ? mainApp.getCurrentUsername() : "Guest";
        infoLabel.setText(
                "Player: " + player + " | Level: " + levelName + " | Time: " + timeLeft + " | Score: " + score);
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
}