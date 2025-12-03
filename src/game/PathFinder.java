package game;//

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PathFinder {
    private Board board;

    private final int[] dr = { -1, 1, 0, 0 };
    private final int[] dc = { 0, 0, -1, 1 };

    public PathFinder(Board board) {
        this.board = board;
    }

    public boolean canConnect(Tile t1, Tile t2) {
        return !getConnectingPath(t1, t2).isEmpty();
    }

    public List<Point> getConnectingPath(Tile start, Tile end) {
        List<Point> path = new ArrayList<>();

        if (start.id != end.id || start == end)
            return path;

        Queue<Node> queue = new LinkedList<>();

        int[][] minTurns = new int[board.rows][board.cols];
        for (int[] row : minTurns)
            Arrays.fill(row, Integer.MAX_VALUE);

        Node root = new Node(start.row, start.col, 0, -1, null);
        queue.add(root);
        minTurns[start.row][start.col] = 0;

        Node finalNode = null;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.r == end.row && current.c == end.col) {
                finalNode = current;
                break;
            }

            for (int i = 0; i < 4; i++) {
                int nr = current.r + dr[i];
                int nc = current.c + dc[i];

                if (nr < 0 || nr >= board.rows || nc < 0 || nc >= board.cols)
                    continue;

                int newTurns = current.turns;
                if (current.dir != -1 && current.dir != i) {
                    newTurns++;
                }

                if (newTurns > 2)
                    continue;

                boolean isTarget = (nr == end.row && nc == end.col);
                if (board.isEmpty(nr, nc) || isTarget) {

                    if (newTurns <= minTurns[nr][nc]) {
                        minTurns[nr][nc] = newTurns;
                        queue.add(new Node(nr, nc, newTurns, i, current));
                    }
                }
            }
        }

        if (finalNode != null) {
            Node p = finalNode;
            while (p != null) {
                path.add(0, new Point(p.c, p.r));
                p = p.parent;
            }
        }

        return path;
    }

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