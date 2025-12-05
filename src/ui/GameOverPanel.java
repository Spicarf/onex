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
import javax.swing.border.EmptyBorder;

public class GameOverPanel extends BasePanel {
    private MainFrame mainApp;
    private JLabel titleLabel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel infoLabel;

    public GameOverPanel(MainFrame mainApp) {
        super();
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);
        setupUI();
    }

    @Override
    public void setupUI() {
        JPanel cardPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PANEL_COLOR);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Theme.ACCENT_COLOR);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        cardPanel.add(titleLabel, gbc);

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(Theme.HEADER_FONT);
        scoreLabel.setForeground(Theme.TEXT_WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        cardPanel.add(scoreLabel, gbc);

        timeLabel = new JLabel("Time: 0s", SwingConstants.CENTER);
        timeLabel.setFont(Theme.SUBHEADER_FONT);
        timeLabel.setForeground(Theme.TEXT_GRAY);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        cardPanel.add(timeLabel, gbc);

        infoLabel = new JLabel("Difficulty: Easy", SwingConstants.CENTER);
        infoLabel.setFont(Theme.NORMAL_FONT);
        infoLabel.setForeground(Theme.TEXT_GRAY);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 40, 0);
        cardPanel.add(infoLabel, gbc);

        JButton btnMenu = createModernButton("BACK TO MENU", Theme.SECONDARY_COLOR);
        btnMenu.addActionListener(e -> mainApp.showPanel("MENU"));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        cardPanel.add(btnMenu, gbc);

        add(cardPanel);
    }

    public void setResults(boolean isWin, int score, int duration, String difficulty) {
        if (isWin) {
            titleLabel.setText("YOU WIN!");
            titleLabel.setForeground(Theme.PRIMARY_COLOR);
        } else {
            titleLabel.setText("GAME OVER");
            titleLabel.setForeground(Theme.ACCENT_COLOR);
        }

        scoreLabel.setText("Final Score: " + score);
        timeLabel.setText("Time Spent: " + duration + "s");
        infoLabel.setText("Difficulty: " + difficulty);
    }

    private JButton createModernButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2.setColor(baseColor.brighter());
                } else {
                    g2.setColor(baseColor);
                }

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