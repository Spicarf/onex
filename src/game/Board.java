package game;

import java.util.ArrayList;
import java.util.Collections;

public class Board {
    public int rows;
    public int cols;
    public Tile[][] tiles;

    public Board(int gameRows, int gameCols) {
        // Padding +2 untuk jalur luar
        this.rows = gameRows + 2;
        this.cols = gameCols + 2;
        tiles = new Tile[rows][cols];
        initializeBoard(gameRows, gameCols);
    }

    private void initializeBoard(int gameRows, int gameCols) {
        ArrayList<Integer> ids = new ArrayList<>();
        int totalPairs = (gameRows * gameCols) / 2;

        // Gunakan angka 1 sampai 15 (atau lebih jika hard)
        int maxVariations = 15;

        for (int i = 0; i < totalPairs; i++) {
            int id = (i % maxVariations) + 1;
            ids.add(id);
            ids.add(id);
        }
        Collections.shuffle(ids);

        int idx = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int currentId = 0;
                boolean isBorder = (r == 0 || r == rows - 1 || c == 0 || c == cols - 1);

                if (!isBorder) {
                    currentId = ids.get(idx++);
                }

                Tile t = new Tile(currentId, r, c);

                if (isBorder) {
                    t.setAsBorder();
                } else {
                    // MODE ANGKA: Cukup set text saja
                    // (Logika ini sudah ada di constructor Tile, tapi kita pertegas)
                    t.setText(String.valueOf(currentId));
                }

                tiles[r][c] = t;
            }
        }
    }

    public Tile getTile(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols)
            return null;
        return tiles[r][c];
    }

    public boolean isEmpty(int r, int c) {
        Tile t = getTile(r, c);
        return t == null || t.isRemoved;
    }

    // Method untuk mengacak ulang sisa tile
    public void shuffleRemainingTiles() {
        ArrayList<Integer> remainingIds = new ArrayList<>();
        ArrayList<Tile> activeTiles = new ArrayList<>();

        // 1. Kumpulkan semua ID dari tile yang belum hilang
        for (int r = 1; r < rows - 1; r++) { // Skip border
            for (int c = 1; c < cols - 1; c++) { // Skip border
                Tile t = tiles[r][c];
                if (!t.isRemoved) {
                    remainingIds.add(t.id);
                    activeTiles.add(t);
                }
            }
        }

        // 2. Acak ID-nya
        Collections.shuffle(remainingIds);

        // 3. Pasang kembali ID yang sudah diacak ke tile yang aktif
        for (int i = 0; i < activeTiles.size(); i++) {
            activeTiles.get(i).setID(remainingIds.get(i));
        }
    }
}