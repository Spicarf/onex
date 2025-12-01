package ui;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

public class Theme {
    // --- WARNA MODERN (Flat Dark UI) ---
    public static final Color BG_COLOR = new Color(15, 23, 42);
    public static final Color PANEL_COLOR = new Color(30, 41, 59);
    public static final Color PRIMARY_COLOR = new Color(56, 189, 248);
    public static final Color SECONDARY_COLOR = new Color(71, 85, 105);
    public static final Color ACCENT_COLOR = new Color(244, 63, 94);

    public static final Color TEXT_WHITE = new Color(241, 245, 249);
    public static final Color TEXT_GRAY = new Color(148, 163, 184);

    public static final Color TILE_BG = new Color(51, 65, 85);
    public static final Color TILE_HOVER = new Color(71, 85, 105);
    public static final Color PATH_COLOR = new Color(244, 63, 94, 200);

    // --- FONT ---
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font TILE_FONT = new Font("Segoe UI", Font.BOLD, 22);

    public static String currentThemeName = "Numbers";
    private static Map<String, ImageIcon> imageCache = new HashMap<>();

    public static String[] getAvailableThemes() {
        List<String> list = new ArrayList<>();
        list.add("Numbers");

        File themesDir = new File("resources/themes");
        if (themesDir.exists() && themesDir.isDirectory()) {
            File[] files = themesDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory())
                        list.add(f.getName());
                }
            }
        }
        return list.toArray(new String[0]);
    }

    public static void setCurrentTheme(String themeName) {
        currentThemeName = themeName;
        imageCache.clear();
    }

    public static ImageIcon getTileImage(int id) {
        if ("Numbers".equalsIgnoreCase(currentThemeName)) {
            return null;
        }

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
                ImageIcon original = new ImageIcon(path);

                java.awt.Image img = original.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
                ImageIcon resized = new ImageIcon(img);

                imageCache.put(key, resized);
                return resized;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}