package db;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ui.MainFrame;
import ui.Theme;

public class LoginPanel extends JPanel {
    private MainFrame mainApp;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);

        setupUI();
    }

    private void setupUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("ONEX LOGIN");
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.FG_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        // Input
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        addComp(new JLabel("Username:"), 0, 1, gbc);
        addComp(usernameField, 1, 1, gbc);
        addComp(new JLabel("Password:"), 0, 2, gbc);
        addComp(passwordField, 1, 2, gbc);

        // Buttons
        JButton btnLogin = new JButton("Login");
        JButton btnReg = new JButton("Register");

        btnLogin.addActionListener(e -> processLogin());
        btnReg.addActionListener(e -> gotoRegister());

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(btnLogin, gbc);
        gbc.gridy = 4;
        add(btnReg, gbc);
    }

    private void addComp(JComponent comp, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        if (comp instanceof JLabel)
            ((JLabel) comp).setForeground(Theme.TEXT_WHITE);
        add(comp, gbc);
    }

    private void processLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        int id = DatabaseManager.loginUser(user, pass);

        if (id != -1) {
            mainApp.onLoginSuccess(id, user);
        } else {
            JOptionPane.showMessageDialog(this, "Login Gagal!");
        }
    }

    private void gotoRegister() {
        mainApp.showPanel("REGISTER");
    }

    public void resetField() {
        usernameField.setText("");
        passwordField.setText("");
    }
}