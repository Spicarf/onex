package ui;//

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import db.DatabaseManager;

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
        JPanel cardPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PANEL_COLOR); // Warna Abu-abu Card
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel title = new JLabel("LOGIN", SwingConstants.CENTER);
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Color.WHITE);
        gbc.gridy = 0;
        cardPanel.add(title, gbc);

        gbc.gridy = 1;
        cardPanel.add(createLabel("Username"), gbc);

        usernameField = createModernTextField();
        gbc.gridy = 2;
        cardPanel.add(usernameField, gbc);

        gbc.gridy = 3;
        cardPanel.add(createLabel("Password"), gbc);

        passwordField = createModernPasswordField();
        gbc.gridy = 4;
        cardPanel.add(passwordField, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(30, 0, 10, 0);

        JButton btnLogin = createModernButton("LOGIN", Theme.PRIMARY_COLOR);
        btnLogin.addActionListener(e -> processLogin());
        cardPanel.add(btnLogin, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton btnReg = createModernButton("CREATE ACCOUNT", Theme.SECONDARY_COLOR);
        btnReg.addActionListener(e -> gotoRegister());
        cardPanel.add(btnReg, gbc);

        add(cardPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.NORMAL_FONT);
        lbl.setForeground(Theme.TEXT_GRAY);
        return lbl;
    }

    private JTextField createModernTextField() {
        JTextField field = new JTextField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_COLOR);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(Theme.NORMAL_FONT);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        return field;
    }

    private JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_COLOR);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(Theme.NORMAL_FONT);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        return field;
    }

    private JButton createModernButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? baseColor.brighter() : baseColor);
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
        btn.setPreferredSize(new Dimension(200, 45));
        return btn;
    }

    private void processLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        int id = DatabaseManager.loginUser(user, pass);
        if (id != -1)
            mainApp.onLoginSuccess(id, user);
        else
            JOptionPane.showMessageDialog(this, "Login Gagal!");
    }

    private void gotoRegister() {
        mainApp.showPanel("REGISTER");
    }

    public void resetField() {
        usernameField.setText("");
        passwordField.setText("");
    }
}