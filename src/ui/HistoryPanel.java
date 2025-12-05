package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

public class HistoryPanel extends BasePanel {
    private MainFrame mainApp;
    private JTable tableHistory;
    private DefaultTableModel model;

    public HistoryPanel(MainFrame mainApp) {
        super();
        this.mainApp = mainApp;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(new EmptyBorder(40, 60, 40, 60));
        setupUI();
    }

    @Override
    public void setupUI() {
        JLabel title = new JLabel("MY HISTORY", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setForeground(Theme.PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] { "SCORE", "DURATION", "DATE" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHistory = new JTable(model);
        styleTable(tableHistory);

        JScrollPane scrollPane = new JScrollPane(tableHistory) {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                RoundRectangle2D.Float round = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setClip(round);
                super.paint(g2);

                g2.setClip(null);
                g2.setColor(Theme.SECONDARY_COLOR);
                g2.setStroke(new BasicStroke(2));
                g2.draw(round);

                g2.dispose();
            }
        };

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        scrollPane.getViewport().setBackground(Theme.PANEL_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Theme.BG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        JButton backBtn = createModernButton("BACK TO MENU", Theme.SECONDARY_COLOR);
        backBtn.addActionListener(e -> mainApp.showPanel("MENU"));

        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setBackground(Theme.PANEL_COLOR);
        table.setForeground(Theme.TEXT_WHITE);
        table.setFont(Theme.NORMAL_FONT);
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                lbl.setBackground(Theme.TILE_BG);
                lbl.setForeground(Theme.PRIMARY_COLOR);
                lbl.setFont(Theme.SUBHEADER_FONT);
                lbl.setHorizontalAlignment(CENTER);
                lbl.setBorder(new EmptyBorder(15, 10, 15, 10));
                return lbl;
            }
        });
        header.setPreferredSize(new Dimension(0, 60));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(CENTER);
                setBorder(new EmptyBorder(0, 10, 0, 10));

                if (row % 2 == 0) {
                    c.setBackground(Theme.PANEL_COLOR);
                } else {
                    c.setBackground(new Color(35, 48, 68));
                }
                c.setForeground(Theme.TEXT_WHITE);
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
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