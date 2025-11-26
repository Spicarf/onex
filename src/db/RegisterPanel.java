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

public class RegisterPanel extends JPanel {
    private MainFrame mainApp;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;

    public RegisterPanel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);
        setupUI();
    }

    private void setupUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("REGISTER ONEX");
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.FG_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        confirmField = new JPasswordField(15);

        addComp(new JLabel("Username:"), 0, 1, gbc);
        addComp(usernameField, 1, 1, gbc);
        addComp(new JLabel("Password:"), 0, 2, gbc);
        addComp(passwordField, 1, 2, gbc);
        addComp(new JLabel("Confirm:"), 0, 3, gbc);
        addComp(confirmField, 1, 3, gbc);

        JButton btnReg = new JButton("Daftar");
        JButton btnBack = new JButton("Kembali");

        btnReg.addActionListener(e -> processRegister());
        btnBack.addActionListener(e -> gotoLogin());

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(btnReg, gbc);
        gbc.gridy = 5;
        add(btnBack, gbc);
    }

    private void addComp(JComponent comp, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        if (comp instanceof JLabel)
            ((JLabel) comp).setForeground(Theme.TEXT_WHITE);
        add(comp, gbc);
    }

    private void processRegister() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        String conf = new String(confirmField.getPassword());

        if (!pass.equals(conf)) {
            JOptionPane.showMessageDialog(this, "Password tidak cocok!");
            return;
        }

        if (DatabaseManager.registerUser(user, pass)) {
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil!");
            gotoLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal. Username mungkin sudah dipakai.");
        }
    }

    private void gotoLogin() {
        resetField();
        mainApp.showPanel("LOGIN");
    }

    public void resetField() {
        usernameField.setText("");
        passwordField.setText("");
        confirmField.setText("");
    }
}