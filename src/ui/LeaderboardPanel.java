import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    
    private Main mainApp;
    private KoneksiDatabase db;
    
    private JTable tabelLeaderboard;
    private DefaultTableModel modelTabel; 
    private JButton backButton = new JButton("Kembali ke Menu");

    private final String[] COLUMN_NAMES = {"Username", "WPM", "Akurasi (%)", "Waktu"}; 

    public LeaderboardPanel(Main mainApp) {
        this.mainApp = mainApp;
        this.db = new KoneksiDatabase();

        setLayout(new BorderLayout(20, 20));
        setBackground(Theme.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel = new JLabel("Top 10 Leaderboard", SwingConstants.CENTER);
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.COLOR_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        modelTabel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelLeaderboard = new JTable(modelTabel);
        tabelLeaderboard.setFont(Theme.FONT_BODY);
        tabelLeaderboard.setRowHeight(25);
        tabelLeaderboard.setDragEnabled(false); 

        tabelLeaderboard.getTableHeader().setFont(Theme.FONT_HEADER);
        tabelLeaderboard.getTableHeader().setBackground(Theme.COLOR_PRIMARY.darker());
        tabelLeaderboard.getTableHeader().setForeground(Theme.COLOR_TEXT_WHITE);
        tabelLeaderboard.setBackground(Theme.COLOR_BACKGROUND);
        tabelLeaderboard.setForeground(Theme.COLOR_TEXT_WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabelLeaderboard);
        scrollPane.getViewport().setBackground(Theme.COLOR_BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);

        styleButton(backButton, Theme.COLOR_PRIMARY, Theme.COLOR_BACKGROUND);
        add(backButton, BorderLayout.SOUTH);
        
        backButton.addActionListener(e -> mainApp.showPanel(Main.MENU_CARD));
    }
    
    private void styleButton(JButton button, Color fg, Color bg) {
        button.setFont(Theme.FONT_HEADER);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setFocusPainted(false);
    }

    public void onPanelShown() {
        modelTabel.setRowCount(0);
        
        List<Object[]> data = db.getLeaderboard();
        
        for (Object[] row : data) {
            modelTabel.addRow(row);
        }
    }
}