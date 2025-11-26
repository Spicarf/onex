package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/onex_db";
    private static final String USER = "root"; // Ganti dengan user db Anda
    private static final String PASS = ""; // Ganti dengan pass db Anda

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static int loginUser(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Gagal
    }

    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- BAGIAN INI YANG DIPERBARUI ---
    public static boolean saveScore(int userId, int score, int duration, String difficulty) {
        // Mencetak log ke terminal untuk debugging
        System.out.println("Menyimpan ke DB: ID=" + userId + ", Score=" + score + ", Diff=" + difficulty);

        String sql = "INSERT INTO history (user_id, score, duration, difficulty) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, score);
            pstmt.setInt(3, duration);
            pstmt.setString(4, difficulty); // Simpan tingkat kesulitan

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("BERHASIL: Data tersimpan.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("GAGAL SAVE SCORE:");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static List<Object[]> getLeaderboard() {
        List<Object[]> list = new ArrayList<>();
        // Kita ambil difficulty juga untuk ditampilkan (opsional)
        String sql = "SELECT u.username, h.score, h.duration, h.difficulty FROM history h " +
                "JOIN users u ON h.user_id = u.id ORDER BY h.score DESC LIMIT 10";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[] {
                        rs.getString("username"),
                        rs.getInt("score"),
                        // Menampilkan durasi + difficulty (misal: 45s (Hard))
                        rs.getInt("duration") + "s (" + rs.getString("difficulty") + ")"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Object[]> getHistory(int userId) {
        List<Object[]> list = new ArrayList<>();
        // Ambil difficulty juga
        String sql = "SELECT score, duration, difficulty, played_at FROM history WHERE user_id = ? ORDER BY played_at DESC";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Object[] {
                        rs.getInt("score"),
                        // Tampilkan difficulty di kolom durasi agar hemat tempat
                        rs.getInt("duration") + "s (" + rs.getString("difficulty") + ")",
                        rs.getTimestamp("played_at")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}