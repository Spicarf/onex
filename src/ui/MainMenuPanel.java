package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainMenuPanel extends JPanel {
    private MainFrame mainApp;
    private JLabel welcomeLabel;
    private JComboBox<String> themeComboBox;

    public MainMenuPanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("ONEX", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));
        titleLabel.setForeground(Theme.PRIMARY_COLOR);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        welcomeLabel = new JLabel("Welcome, Player!", SwingConstants.CENTER);
        welcomeLabel.setFont(Theme.SUBHEADER_FONT);
        welcomeLabel.setForeground(Theme.TEXT_GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(welcomeLabel, gbc);

        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        themePanel.setOpaque(false);

        JLabel themeLabel = new JLabel("Theme: ");
        themeLabel.setFont(Theme.NORMAL_FONT);
        themeLabel.setForeground(Theme.TEXT_WHITE);

        String[] themes = Theme.getAvailableThemes();
        themeComboBox = new JComboBox<>(themes);
        themeComboBox.setFont(Theme.NORMAL_FONT);
        themeComboBox.setPreferredSize(new Dimension(150, 30));

        themePanel.add(themeLabel);
        themePanel.add(themeComboBox);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(themePanel, gbc);

        gbc.insets = new Insets(8, 0, 8, 0);

        JButton btnStart = createModernButton("START GAME", Theme.PRIMARY_COLOR);
        btnStart.addActionListener(e -> {
            String selectedTheme = (String) themeComboBox.getSelectedItem();
            Theme.setCurrentTheme(selectedTheme);
            mainApp.showPanel("LEVEL_SELECT");
        });
        gbc.gridy = 3;
        add(btnStart, gbc);

        JButton btnLeader = createModernButton("LEADERBOARD", Theme.PANEL_COLOR);
        btnLeader.addActionListener(e -> mainApp.showPanel("LEADERBOARD"));
        gbc.gridy = 4;
        add(btnLeader, gbc);

        JButton btnHistory = createModernButton("HISTORY", Theme.PANEL_COLOR);
        btnHistory.addActionListener(e -> mainApp.showPanel("HISTORY"));
        gbc.gridy = 5;
        add(btnHistory, gbc);

        JButton btnLogout = createModernButton("LOGOUT", Theme.ACCENT_COLOR);
        btnLogout.addActionListener(e -> mainApp.logout());
        gbc.gridy = 6;
        add(btnLogout, gbc);
    }

    public void setWelcomeMessage(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
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