package ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainMenuPanel extends JPanel {
    private MainFrame mainApp;
    private JLabel welcomeLabel;

    public MainMenuPanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new GridLayout(6, 1, 10, 10));
        setBackground(Theme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        welcomeLabel = new JLabel("Welcome, Player!", SwingConstants.CENTER);
        welcomeLabel.setFont(Theme.HEADER_FONT);
        welcomeLabel.setForeground(Theme.FG_COLOR);
        add(welcomeLabel);

        addButton("Start Game", e -> mainApp.showPanel("LEVEL_SELECT"));

        addButton("Leaderboard", e -> mainApp.showPanel("LEADERBOARD"));
        addButton("History", e -> mainApp.showPanel("HISTORY"));
        addButton("Logout", e -> mainApp.logout());
    }

    private void addButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.NORMAL_FONT);
        btn.setBackground(Theme.TILE_BG);
        btn.setForeground(Theme.TEXT_WHITE);
        btn.addActionListener(action);
        add(btn);
    }

    public void setWelcomeMessage(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }
}