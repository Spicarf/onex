package ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public abstract class BasePanel extends JPanel {
    public BasePanel() {
        setLayout(new BorderLayout());
    }

    public abstract void setupUI();
}