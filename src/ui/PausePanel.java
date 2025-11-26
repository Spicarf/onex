package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PausePanel extends JPanel {
    private MainFrame mainApp;

    public PausePanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 200));
        setBackground(Theme.BG_COLOR);

        setupUI();
    }

    private void setupUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("GAME PAUSED", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.ORANGE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        JButton btnResume = new JButton("RESUME GAME");
        btnResume.setFont(Theme.HEADER_FONT);
        btnResume.setBackground(Theme.FG_COLOR);
        btnResume.setForeground(Theme.BG_COLOR);
        btnResume.setPreferredSize(new Dimension(250, 60));
        btnResume.addActionListener(e -> mainApp.resumeGame());

        gbc.gridy = 1;
        add(btnResume, gbc);

        JButton btnQuit = new JButton("QUIT TO MENU");
        btnQuit.setFont(Theme.NORMAL_FONT);
        btnQuit.setBackground(Color.RED);
        btnQuit.setForeground(Color.WHITE);
        btnQuit.setPreferredSize(new Dimension(250, 50));
        btnQuit.addActionListener(e -> {
            mainApp.stopGameAndReturnToMenu();
        });

        gbc.gridy = 2;
        add(btnQuit, gbc);
    }
}