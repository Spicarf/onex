package ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import db.DatabaseManager;

public class HistoryPanel extends JPanel {
    private MainFrame mainApp;
    private JTable tableHistory;
    private DefaultTableModel model;

    public HistoryPanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);

        JLabel title = new JLabel("MY HISTORY", SwingConstants.CENTER);
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.FG_COLOR);
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] { "Score", "Duration", "Date" }, 0);
        tableHistory = new JTable(model);
        add(new JScrollPane(tableHistory), BorderLayout.CENTER);

        JButton backBtn = new JButton("Back to Menu");
        backBtn.addActionListener(e -> mainApp.showPanel("MENU"));
        add(backBtn, BorderLayout.SOUTH);
    }

    public void onPanelShown() {
        model.setRowCount(0);
        List<Object[]> data = DatabaseManager.getHistory(mainApp.getCurrentUserId());
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}