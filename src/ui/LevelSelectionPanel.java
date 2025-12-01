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

public class LevelSelectionPanel extends JPanel {
    private MainFrame mainApp;

    public LevelSelectionPanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);
        setupUI();
    }

    private void setupUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        JLabel title = new JLabel("SELECT DIFFICULTY", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36)); // Font Besar
        title.setForeground(Theme.TEXT_WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0); // Jarak agak jauh ke tombol pertama
        add(title, gbc);

        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 1;
        JButton btnEasy = createModernButton("EASY (4x6)", Theme.PRIMARY_COLOR);
        btnEasy.addActionListener(e -> mainApp.startGame(1));
        add(btnEasy, gbc);

        gbc.gridy = 2;
        JButton btnMedium = createModernButton("MEDIUM (6x8)", Theme.TILE_HOVER);
        btnMedium.addActionListener(e -> mainApp.startGame(2));
        add(btnMedium, gbc);

        gbc.gridy = 3;
        JButton btnHard = createModernButton("HARD (8x12)", Theme.ACCENT_COLOR);
        btnHard.addActionListener(e -> mainApp.startGame(3));
        add(btnHard, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(30, 0, 0, 0);
        JButton btnBack = createModernButton("BACK TO MENU", Theme.SECONDARY_COLOR);
        btnBack.addActionListener(e -> mainApp.showPanel("MENU"));
        add(btnBack, gbc);
    }

    private JButton createModernButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Efek Hover
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
        btn.setPreferredSize(new Dimension(280, 55));
        return btn;
    }
}