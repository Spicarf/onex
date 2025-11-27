package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import db.DatabaseManager;

public class HistoryPanel extends JPanel {
    private MainFrame mainApp;
    private JTable tableHistory;
    private DefaultTableModel model;

    public HistoryPanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(new EmptyBorder(20, 40, 20, 40)); // Padding pinggir

        // --- JUDUL ---
        JLabel title = new JLabel("MY HISTORY", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Theme.PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 20, 0)); // Jarak ke tabel
        add(title, BorderLayout.NORTH);

        // --- TABEL MODERN ---
        // Kolom: Score, Duration, Date
        model = new DefaultTableModel(new String[] { "Score", "Duration", "Date" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tidak bisa diedit
            }
        };

        tableHistory = new JTable(model);
        styleTable(tableHistory); // Styling modern

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(tableHistory);
        scrollPane.getViewport().setBackground(Theme.BG_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
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
        // Body Tabel
        table.setBackground(Theme.PANEL_COLOR);
        table.setForeground(Theme.TEXT_WHITE);
        table.setFont(Theme.NORMAL_FONT);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        // Header Tabel
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.TILE_BG);
        header.setForeground(Color.WHITE);
        header.setFont(Theme.SUBHEADER_FONT);
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(0, 40));

        // Rata Tengah
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(Theme.PANEL_COLOR);
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
        model.setRowCount(0);
        List<Object[]> data = DatabaseManager.getHistory(mainApp.getCurrentUserId());
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}