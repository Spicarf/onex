package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainMenuPanel extends BasePanel {
    private MainFrame mainApp;
    private JLabel welcomeLabel;

    private List<JButton> themeButtons = new ArrayList<>();
    private String selectedThemeName = "Numbers";

    public MainMenuPanel(MainFrame mainApp) {
        super();
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);
        setupUI();
    }

    @Override
    public void setupUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("ONEX", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 64));
        titleLabel.setForeground(Theme.PRIMARY_COLOR);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        welcomeLabel = new JLabel("Welcome, Player!", SwingConstants.CENTER);
        welcomeLabel.setFont(Theme.SUBHEADER_FONT);
        welcomeLabel.setForeground(Theme.TEXT_GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(welcomeLabel, gbc);

        JLabel lblChoose = new JLabel("CHOOSE THEME", SwingConstants.CENTER);
        lblChoose.setFont(Theme.SUBHEADER_FONT);
        lblChoose.setForeground(Theme.TEXT_WHITE);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        add(lblChoose, gbc);

        JPanel themeContainer = new JPanel(new GridLayout(1, 3, 15, 0));
        themeContainer.setOpaque(false);

        String[] themes = Theme.getAvailableThemes();

        for (String themeName : themes) {
            JButton btnTheme = createThemePreviewCard(themeName);
            themeButtons.add(btnTheme);
            themeContainer.add(btnTheme);
        }

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(themeContainer);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(wrapperPanel, gbc);

        updateThemeSelection(themes.length > 0 ? themes[0] : "Numbers");

        gbc.insets = new Insets(8, 0, 8, 0);

        JButton btnStart = createModernButton("START GAME", Theme.PRIMARY_COLOR);
        btnStart.addActionListener(e -> {
            Theme.setCurrentTheme(selectedThemeName);
            this.mainApp.showPanel("LEVEL_SELECT");
        });
        gbc.gridy = 4;
        add(btnStart, gbc);

        JButton btnLeader = createModernButton("LEADERBOARD", Theme.PANEL_COLOR);
        btnLeader.addActionListener(e -> this.mainApp.showPanel("LEADERBOARD"));
        gbc.gridy = 5;
        add(btnLeader, gbc);

        JButton btnHistory = createModernButton("HISTORY", Theme.PANEL_COLOR);
        btnHistory.addActionListener(e -> this.mainApp.showPanel("HISTORY"));
        gbc.gridy = 6;
        add(btnHistory, gbc);

        JButton btnLogout = createModernButton("LOGOUT", Theme.ACCENT_COLOR);
        btnLogout.addActionListener(e -> this.mainApp.logout());
        gbc.gridy = 7;
        add(btnLogout, gbc);
    }

    public void setWelcomeMessage(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    private void updateThemeSelection(String newTheme) {
        this.selectedThemeName = newTheme;
        Theme.setCurrentTheme(newTheme);

        for (JButton btn : themeButtons) {
            btn.repaint();
        }
    }

    private JButton createThemePreviewCard(String themeName) {
        ImageIcon sampleIcon = null;
        if (!themeName.equalsIgnoreCase("Numbers")) {
            try {
                String path = "resources/themes/" + themeName + "/1.png";
                File f = new File(path);
                if (f.exists()) {
                    ImageIcon original = new ImageIcon(path);
                    Image img = original.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    sampleIcon = new ImageIcon(img);
                }
            } catch (Exception e) {
            }
        }

        final ImageIcon finalIcon = sampleIcon;

        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isSelected = themeName.equals(selectedThemeName);
                boolean isHover = getModel().isRollover();

                if (isSelected) {
                    g2.setColor(Theme.PRIMARY_COLOR);
                } else if (isHover) {
                    g2.setColor(Theme.TILE_HOVER);
                } else {
                    g2.setColor(Theme.TILE_BG);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));

                if (finalIcon != null) {
                    int x = (getWidth() - finalIcon.getIconWidth()) / 2;
                    int y = (getHeight() - finalIcon.getIconHeight()) / 2 - 10;
                    finalIcon.paintIcon(this, g2, x, y);
                } else {
                    g2.setColor(isSelected ? Color.WHITE : Theme.TEXT_GRAY);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
                    String sampleText = "7";
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(sampleText)) / 2;
                    int y = (getHeight() / 2);
                    g2.drawString(sampleText, x, y);
                }

                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor(isSelected ? Color.WHITE : Theme.TEXT_GRAY);
                String label = themeName.toUpperCase();
                FontMetrics fm2 = g2.getFontMetrics();
                int x2 = (getWidth() - fm2.stringWidth(label)) / 2;
                int y2 = getHeight() - 10;
                g2.drawString(label, x2, y2);

                if (isSelected) {
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(3));
                    g2.draw(new RoundRectangle2D.Float(2, 2, getWidth() - 4, getHeight() - 4, 15, 15));
                }

                g2.dispose();
            }
        };

        btn.setActionCommand(themeName);
        btn.setPreferredSize(new Dimension(100, 100));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);

        btn.addActionListener(e -> updateThemeSelection(themeName));

        return btn;
    }

    private JButton createModernButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover())
                    g2.setColor(baseColor.brighter());
                else
                    g2.setColor(baseColor);
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
        btn.setPreferredSize(new Dimension(280, 50));
        return btn;
    }
}