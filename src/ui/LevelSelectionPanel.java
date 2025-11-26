package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Judul
        JLabel title = new JLabel("SELECT DIFFICULTY", SwingConstants.CENTER);
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.FG_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        // Tombol Level
        createLevelButton("EASY (4x6)", 1, gbc, 1);
        createLevelButton("MEDIUM (6x8)", 2, gbc, 2);
        createLevelButton("HARD (8x12)", 3, gbc, 3);

        // Tombol Kembali
        JButton btnBack = new JButton("Back to Menu");
        btnBack.setFont(Theme.NORMAL_FONT);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> mainApp.showPanel("MENU"));

        gbc.gridy = 4;
        add(btnBack, gbc);
    }

    private void createLevelButton(String text, int difficulty, GridBagConstraints gbc, int y) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.NORMAL_FONT);
        btn.setBackground(Theme.TILE_BG);
        btn.setForeground(Theme.TEXT_WHITE);
        btn.setPreferredSize(new Dimension(200, 50));

        btn.addActionListener(e -> {
            mainApp.startGame(difficulty); // Mulai game dengan difficulty terpilih
        });

        gbc.gridy = y;
        add(btn, gbc);
    }
}