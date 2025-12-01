package ui;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel container;

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private MainMenuPanel mainMenuPanel;
    private LevelSelectionPanel levelSelectionPanel;
    private GamePanel gamePanel;
    private PausePanel pausePanel;
    private LeaderboardPanel leaderboardPanel;
    private HistoryPanel historyPanel;
    private GameOverPanel gameOverPanel;

    private int currentUserId;
    private String currentUsername;

    public MainFrame() {
        setTitle("ONEX - Tile Matching Game");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        levelSelectionPanel = new LevelSelectionPanel(this);
        gamePanel = new GamePanel(this);
        pausePanel = new PausePanel(this);
        leaderboardPanel = new LeaderboardPanel(this);
        historyPanel = new HistoryPanel(this);
        gameOverPanel = new GameOverPanel(this);

        container.add(loginPanel, "LOGIN");
        container.add(registerPanel, "REGISTER");
        container.add(mainMenuPanel, "MENU");
        container.add(levelSelectionPanel, "LEVEL_SELECT");
        container.add(gamePanel, "GAME");
        container.add(pausePanel, "PAUSE");
        container.add(leaderboardPanel, "LEADERBOARD");
        container.add(historyPanel, "HISTORY");
        container.add(gameOverPanel, "GAMEOVER");

        add(container);
        showPanel("LOGIN");
    }

    public void showPanel(String name) {
        if (name.equals("LEADERBOARD"))
            leaderboardPanel.onPanelShown();
        if (name.equals("HISTORY"))
            historyPanel.onPanelShown();
        cardLayout.show(container, name);
    }

    public void startGame(int difficulty) {
        gamePanel.startGame(difficulty);
        cardLayout.show(container, "GAME");
    }

    public void showPauseScreen() {
        cardLayout.show(container, "PAUSE");
    }

    public void resumeGame() {
        gamePanel.resumeGame();
        cardLayout.show(container, "GAME");
    }

    public void stopGameAndReturnToMenu() {
        gamePanel.stopGame(game.GameEndType.USER_QUIT);
    }

    public void showGameOverScreen(boolean isWin, int score, int duration, String difficulty) {
        gameOverPanel.setResults(isWin, score, duration, difficulty);
        cardLayout.show(container, "GAMEOVER");
    }

    public void onLoginSuccess(int id, String username) {
        this.currentUserId = id;
        this.currentUsername = username;
        mainMenuPanel.setWelcomeMessage(username);
        showPanel("MENU");
    }

    public void logout() {
        this.currentUserId = -1;
        this.currentUsername = null;
        loginPanel.resetField();
        showPanel("LOGIN");
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}