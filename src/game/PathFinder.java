package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PathFinder {
    private Board board;

    // Arah pergerakan: Atas, Bawah, Kiri, Kanan
    private final int[] dr = { -1, 1, 0, 0 };
    private final int[] dc = { 0, 0, -1, 1 };

    public PathFinder(Board board) {
        this.board = board;
    }

    // Method utama yang dipanggil GamePanel
    public boolean canConnect(Tile t1, Tile t2) {
        return !getConnectingPath(t1, t2).isEmpty();
    }

    // Method algoritma BFS untuk mencari jalur
    public List<Point> getConnectingPath(Tile start, Tile end) {
        List<Point> path = new ArrayList<>();

        // Syarat dasar: ID harus sama, tile tidak boleh sama
        if (start.id != end.id || start == end)
            return path;

        // Queue untuk menyimpan node yang akan dicek
        Queue<Node> queue = new LinkedList<>();

        // Array untuk mencatat turn minimal agar tidak looping (visited)
        // Kita simpan 'turns' terkecil di setiap sel
        int[][] minTurns = new int[board.rows][board.cols];
        for (int[] row : minTurns)
            Arrays.fill(row, Integer.MAX_VALUE);

        // Masukkan posisi awal ke antrian
        // row, col, currentTurns, direction (-1 = awal), parent
        Node root = new Node(start.row, start.col, 0, -1, null);
        queue.add(root);
        minTurns[start.row][start.col] = 0;

        Node finalNode = null;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Jika sampai di target
            if (current.r == end.row && current.c == end.col) {
                finalNode = current;
                break; // Ketemu!
            }

            // Coba gerak ke 4 arah
            for (int i = 0; i < 4; i++) {
                int nr = current.r + dr[i];
                int nc = current.c + dc[i];

                // Cek batas board
                if (nr < 0 || nr >= board.rows || nc < 0 || nc >= board.cols)
                    continue;

                // Hitung belokan
                int newTurns = current.turns;
                if (current.dir != -1 && current.dir != i) {
                    newTurns++; // Belok
                }

                // SYARAT ONET: Maksimal 2 Belokan
                if (newTurns > 2)
                    continue;

                // Cek apakah tile bisa dilewati
                // Bisa lewat jika: Kosong (isEmpty) ATAU itu adalah Tile Target
                boolean isTarget = (nr == end.row && nc == end.col);
                if (board.isEmpty(nr, nc) || isTarget) {

                    // Optimasi: Hanya lanjut jika jalur ini lebih efisien atau sama
                    if (newTurns <= minTurns[nr][nc]) {
                        minTurns[nr][nc] = newTurns;
                        queue.add(new Node(nr, nc, newTurns, i, current));
                    }
                }
            }
        }

        // Jika ketemu, susun ulang jalur dari Target ke Start (Backtracking)
        if (finalNode != null) {
            Node p = finalNode;
            while (p != null) {
                path.add(0, new Point(p.c, p.r)); // Masukkan ke depan list
                p = p.parent;
            }
        }

        return path;
    }

    // Class Helper untuk BFS
    private class Node {
        int r, c, turns, dir;
        Node parent;

        public Node(int r, int c, int turns, int dir, Node parent) {
            this.r = r;
            this.c = c;
            this.turns = turns;
            this.dir = dir;
            this.parent = parent;
        }
    }
}