package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

import ui.Theme;

public class Tile extends JButton {
    public int id;
    public int row, col;
    public boolean isRemoved = false;
    private boolean isBorder = false;

    private boolean isSelected = false;

    public Tile(int id, int row, int col) {
        this.id = id;
        this.row = row;
        this.col = col;

        setText(String.valueOf(id));
        setFont(Theme.TILE_FONT);
        setForeground(Theme.TEXT_WHITE);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isRemoved)
            return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isSelected) {
            g2.setColor(Color.ORANGE);
        } else if (getModel().isRollover()) {
            g2.setColor(Theme.TILE_HOVER);
        } else {
            g2.setColor(Theme.TILE_BG);
        }

        g2.fill(new RoundRectangle2D.Float(2, 2, getWidth() - 4, getHeight() - 4, 15, 15));

        if (isSelected) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.draw(new RoundRectangle2D.Float(2, 2, getWidth() - 4, getHeight() - 4, 15, 15));
        }

        g2.dispose();
        super.paintComponent(g);
    }

    // METHOD BARU: Untuk mengatur status seleksi dari GamePanel
    public void setSelected(boolean selected) {
        this.isSelected = selected;

        // Ubah warna text agar kontras jika background orange
        if (selected) {
            setForeground(Color.BLACK);
        } else {
            setForeground(Theme.TEXT_WHITE);
        }

        repaint(); // Gambar ulang tile
    }

    public void markAsRemoved() {
        this.isRemoved = true;
        setText("");
        setEnabled(false);
        repaint();
    }

    public void setAsBorder() {
        this.isBorder = true;
        this.isRemoved = true;
        markAsRemoved();
    }

    public void setID(int newID) {
        this.id = newID;
        setText(String.valueOf(newID));
    }

    public void reset() {
        if (isBorder)
            return;
        this.isRemoved = false;
        this.isSelected = false; // Reset seleksi juga
        setText(String.valueOf(id));
        setForeground(Theme.TEXT_WHITE);
        setEnabled(true);
        repaint();
    }
}