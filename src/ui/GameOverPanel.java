package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GameOverPanel extends JPanel {
    private MainFrame mainApp;
    private JLabel titleLabel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel infoLabel;

    public GameOverPanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);
        setupUI();
    }

    private void setupUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. JUDUL (GAME OVER / YOU WIN)
        titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Theme.ACCENT_COLOR);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(titleLabel, gbc);

        // 2. SKOR
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(Theme.HEADER_FONT);
        scoreLabel.setForeground(Theme.TEXT_WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        add(scoreLabel, gbc);

        // 3. WAKTU
        timeLabel = new JLabel("Time: 0s", SwingConstants.CENTER);
        timeLabel.setFont(Theme.SUBHEADER_FONT);
        timeLabel.setForeground(Theme.TEXT_GRAY);
        gbc.gridy = 2;
        add(timeLabel, gbc);

        // 4. INFO TAMBAHAN (Difficulty)
        infoLabel = new JLabel("Difficulty: Easy", SwingConstants.CENTER);
        infoLabel.setFont(Theme.NORMAL_FONT);
        infoLabel.setForeground(Theme.TEXT_GRAY);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(infoLabel, gbc);

        // 5. TOMBOL MENU
        JButton btnMenu = createModernButton("BACK TO MENU", Theme.SECONDARY_COLOR);
        btnMenu.addActionListener(e -> mainApp.showPanel("MENU"));
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(btnMenu, gbc);
    }

    // Method untuk menerima data hasil permainan dari GamePanel
    public void setResults(boolean isWin, int score, int duration, String difficulty) {
        if (isWin) {
            titleLabel.setText("YOU WIN!");
            titleLabel.setForeground(Theme.PRIMARY_COLOR); // Biru jika menang
        } else {
            titleLabel.setText("GAME OVER");
            titleLabel.setForeground(Theme.ACCENT_COLOR); // Merah jika kalah
        }

        scoreLabel.setText("Final Score: " + score);
        timeLabel.setText("Duration: " + duration + "s");
        infoLabel.setText("Difficulty: " + difficulty);
    }

    // Helper Tombol Modern
    private JButton createModernButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? baseColor.brighter() : baseColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Theme.SUBHEADER_FONT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(250, 50));
        return btn;
    }
}