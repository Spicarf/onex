package ui;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class Theme {
    public static final Color BG_COLOR = new Color(10, 26, 47);
    public static final Color FG_COLOR = new Color(77, 184, 255);
    public static final Color TILE_BG = new Color(60, 110, 145);
    public static final Color TEXT_WHITE = new Color(255, 255, 255);

    public static final Color PATH_COLOR = new Color(255, 50, 50, 180);

    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font TILE_FONT = new Font("Segoe UI", Font.BOLD, 20); // Font untuk angka

    public static String currentThemeName = "pokemon";
    private static Map<String, ImageIcon> imageCache = new HashMap<>();

    public static String[] getAvailableThemes() {
        File themesDir = new File("resources/themes");
        if (!themesDir.exists() || !themesDir.isDirectory()) {
            return new String[] { "pokemon", "zodiac" };
        }
        return themesDir.list((current, name) -> new File(current, name).isDirectory());
    }

    public static void setCurrentTheme(String themeName) {
        currentThemeName = themeName;
        imageCache.clear();
    }

    public static ImageIcon getTileImage(int id) {
        String key = currentThemeName + "_" + id;
        if (imageCache.containsKey(key)) {
            return imageCache.get(key);
        }
        try {
            String path = "resources/themes/" + currentThemeName + "/" + id + ".png";
            File imgFile = new File(path);
            if (!imgFile.exists()) {
                path = "resources/themes/" + currentThemeName + "/" + id + ".jpg";
                imgFile = new File(path);
            }
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(path);
                imageCache.put(key, icon);
                return icon;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}