package game;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import ui.Theme;

public class Tile extends JButton {
    public int id;
    public int row, col;
    public boolean isRemoved = false;
    private boolean isBorder = false;

    public Tile(int id, int row, int col) {
        this.id = id;
        this.row = row;
        this.col = col;
        setupVisual();
    }

    private void setupVisual() {
        // AKTIFKAN LAGI TEKS ANGKA
        setText(String.valueOf(id));
        setFont(Theme.TILE_FONT); // Pastikan font besar dan jelas

        setBackground(Theme.TILE_BG);
        setForeground(Theme.TEXT_WHITE); // Warna angka putih
        setFocusPainted(false);

        // Border standar
        setBorder(BorderFactory.createLineBorder(Theme.BG_COLOR, 2));
    }

    // Method ini tidak dipakai dulu di mode angka
    // Tapi kita biarkan kosong agar tidak error jika dipanggil Board lama
    public void setTileImage(javax.swing.ImageIcon icon) {
        // Do nothing for text mode
    }

    // Hapus Override paintComponent agar kembali ke tombol standar Java Swing
    // (Button akan digambar secara default dengan background dan text)

    public void markAsRemoved() {
        this.isRemoved = true;
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setText(""); // Hapus angkanya saat hilang
        setEnabled(false);
    }

    public void setAsBorder() {
        this.isBorder = true;
        this.isRemoved = true;
        markAsRemoved();
    }

    public void reset() {
        if (isBorder)
            return;
        this.isRemoved = false;
        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(true);

        setText(String.valueOf(id)); // Tampilkan angka lagi saat reset
        setBackground(Theme.TILE_BG);
        setEnabled(true);
        repaint();
    }

    // Method baru untuk mengubah ID saat Shuffle
    public void setID(int newID) {
        this.id = newID;
        setText(String.valueOf(newID)); // Update tampilan angka
        // Jika nanti pakai gambar, update gambar di sini juga
    }
}