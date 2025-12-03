package game;//

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import ui.Theme;

public class Tile extends JButton {
    public int id;
    public int row, col;
    public boolean isRemoved = false;
    public boolean isSelected = false;
    private boolean isBorder = false;

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
        setMargin(new Insets(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isRemoved || isBorder)
            return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        if (getIcon() != null && getIcon() instanceof ImageIcon) {
            Image img = ((ImageIcon) getIcon()).getImage();

            g2.drawImage(img, 0, 0, w, h, this);

            if (isSelected) {
                g2.setColor(new Color(255, 255, 255, 100));
                g2.fillRect(0, 0, w, h);
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(5));
                g2.drawRect(0, 0, w, h);
            }

        } else {
            Shape shape = new RoundRectangle2D.Float(2, 2, w - 4, h - 4, 15, 15);

            if (isSelected)
                g2.setColor(Color.ORANGE);
            else if (getModel().isRollover())
                g2.setColor(Theme.TILE_HOVER);
            else
                g2.setColor(Theme.TILE_BG);

            g2.fill(shape);

            if (isSelected) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.draw(shape);
            }

            super.paintComponent(g);
        }

        g2.dispose();
    }

    public void setAsBorder() {
        this.isBorder = true;
        this.isRemoved = true;
        markAsRemoved();
    }

    public void markAsRemoved() {
        this.isRemoved = true;
        setText("");
        setIcon(null);
        setEnabled(false);
        repaint();
    }

    public void setID(int newID) {
        this.id = newID;
        setText(String.valueOf(newID));
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (selected)
            setForeground(Color.BLACK);
        else
            setForeground(Theme.TEXT_WHITE);
        repaint();
    }

    public void reset() {
        if (isBorder)
            return;
        this.isRemoved = false;
        this.isSelected = false;

        setText(String.valueOf(id));
        setForeground(Theme.TEXT_WHITE);
        setIcon(null);

        setEnabled(true);
        repaint();
    }
}