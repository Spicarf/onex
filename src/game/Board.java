package game;//

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;

import ui.Theme;

public class Board {
    public int rows;
    public int cols;
    public Tile[][] tiles;

    public Board(int gameRows, int gameCols) {
        this.rows = gameRows + 2;
        this.cols = gameCols + 2;
        tiles = new Tile[rows][cols];
        initializeBoard(gameRows, gameCols);
    }

    private void initializeBoard(int gameRows, int gameCols) {
        ArrayList<Integer> ids = new ArrayList<>();
        int totalPairs = (gameRows * gameCols) / 2;

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
                    applyThemeToTile(t, currentId);
                }

                tiles[r][c] = t;
            }
        }
    }

    private void applyThemeToTile(Tile t, int id) {
        ImageIcon icon = Theme.getTileImage(id);

        if (icon != null) {
            t.setIcon(icon);
            t.setText("");
        } else {
            t.setIcon(null);
            t.setText(String.valueOf(id));
            t.setForeground(Theme.TEXT_WHITE);
        }
    }

    public void shuffleRemainingTiles() {
        ArrayList<Integer> remainingIds = new ArrayList<>();
        ArrayList<Tile> activeTiles = new ArrayList<>();

        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                Tile t = tiles[r][c];
                if (!t.isRemoved) {
                    remainingIds.add(t.id);
                    activeTiles.add(t);
                }
            }
        }

        Collections.shuffle(remainingIds);

        for (int i = 0; i < activeTiles.size(); i++) {
            int newId = remainingIds.get(i);
            Tile t = activeTiles.get(i);

            t.id = newId;
            applyThemeToTile(t, newId);
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
}