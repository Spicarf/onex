package ui;

import db.DatabaseManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private MainFrame mainApp;
    private JTable tableLeaderboard;
    private DefaultTableModel model;

    public LeaderboardPanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(new EmptyBorder(20, 40, 20, 40)); // Padding pinggir agar tidak mepet

        // --- JUDUL ---
        JLabel title = new JLabel("TOP 10 PLAYERS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Theme.PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 20, 0)); // Jarak ke tabel
        add(title, BorderLayout.NORTH);

        // --- TABEL MODERN ---
        // Kolom: Username, Score, Duration
        model = new DefaultTableModel(new String[] { "Username", "Score", "Duration" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Agar tidak bisa diedit user
            }
        };

        tableLeaderboard = new JTable(model);
        styleTable(tableLeaderboard); // Panggil helper styling

        // ScrollPane (Wadah Tabel)
        JScrollPane scrollPane = new JScrollPane(tableLeaderboard);
        scrollPane.getViewport().setBackground(Theme.BG_COLOR); // Samakan background
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Hilangkan border garis
        scrollPane.setBackground(Theme.BG_COLOR);
        add(scrollPane, BorderLayout.CENTER);

        // --- TOMBOL KEMBALI ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Theme.BG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton backBtn = createModernButton("BACK TO MENU", Theme.SECONDARY_COLOR);
        backBtn.addActionListener(e -> mainApp.showPanel("MENU"));

        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // --- HELPER: STYLING TABEL ---
    private void styleTable(JTable table) {
        // Warna & Font Body
        table.setBackground(Theme.PANEL_COLOR);
        table.setForeground(Theme.TEXT_WHITE);
        table.setFont(Theme.NORMAL_FONT);
        table.setRowHeight(35); // Baris lebih tinggi biar lega
        table.setShowGrid(false); // Hilangkan garis grid kotak-kotak
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        // Warna & Font Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.TILE_BG); // Warna Header
        header.setForeground(Color.WHITE);
        header.setFont(Theme.SUBHEADER_FONT);
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(0, 40)); // Tinggi Header

        // Rata Tengah (Center Alignment) untuk isi tabel
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(Theme.PANEL_COLOR); // Pastikan background renderer pas
        centerRenderer.setForeground(Theme.TEXT_WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // --- HELPER: TOMBOL MODERN ---
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
        btn.setPreferredSize(new Dimension(250, 50));
        return btn;
    }

    public void onPanelShown() {
        model.setRowCount(0); // Reset tabel
        List<Object[]> data = DatabaseManager.getLeaderboard();
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}