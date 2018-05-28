import java.util.Arrays;
import java.util.Stack;

public class Board {
    private int n;
    private int size;
    private int[][] tiles;
    private int hamming = 0;
    private int manhattan = 0;
    private int zCol, zRow;
    public Board(int[][] blocks) {
        if (null == blocks)
            throw new IllegalArgumentException();
        n = blocks.length;
        size = n*n;

        tiles = new int[n][n];

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int des = (row * n + col + 1) % size;
                int val = blocks[row][col];
                tiles[row][col] = val;
                if (val == 0) {
                    zCol = col;
                    zRow = row;
                    continue;
                }
                if (val != des) {
                    int r, c;
                    hamming++;

                    r = (val - 1) / n;
                    c = (val - 1) % n;
                    int steps = Math.abs(row - r) + Math.abs(col - c);
                    manhattan += steps;
                }
            }
        }
    }
    private Board(int row, int col) {
        zRow = row;
        zCol = col;
    }

    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        if (zRow > 0)
            neighbors.add(slide(zRow-1, zCol));
        if (zRow < n - 1)
            neighbors.add(slide(zRow+1, zCol));
        if (zCol > 0)
            neighbors.add(slide(zRow, zCol-1));
        if (zCol < n - 1)
            neighbors.add(slide(zRow, zCol+1));
        return neighbors;
    }

    public int dimension() {
        return n;
    }
    public int hamming() {
        return hamming;
    }
    public int manhattan() {
        return manhattan;
    }
    public boolean isGoal() {
        return hamming == 0;
    }

    public Board twin() {
        int[][] tmp = new int[n][];
        for (int i = 0; i < n; i++)
            tmp[i] = tiles[i].clone();

        int r = 0;
        int c = 0;
        while (tmp[r][c] == 0 || tmp[r][c + 1] == 0) {
            c++;
            if (c >= tmp.length - 1) {
                r++;
                c = 0;
            }
        }
        int val = tmp[r][c];
        tmp[r][c] = tmp[r][c+1];
        tmp[r][c+1] = val;

        return new Board(tmp);
    }
    private Board slide(int row, int col) {
        int[][] tmp = new int[n][];
        for (int i = 0; i < n; i++)
            tmp[i] = tiles[i].clone();

        int val = tmp[row][col];
        tmp[row][col] = 0;
        tmp[zRow][zCol] = val;

        int desired = (zRow * n + zCol + 1) % size;
        int oldDesired = (row * n + col + 1) % size;

        int r, c, hamm = hamming, manh = manhattan;
        r = (val - 1) / n;
        c = (val - 1) % n;

        if (val != oldDesired) {
            hamm--;
            manh -= Math.abs(row - r) + Math.abs(col - c);
        }

        if (val != desired) {
            hamm++;
            manh += Math.abs(zRow - r) + Math.abs(zCol - c);
        }

        Board res = new Board(row, col);
        res.n = n;
        res.size = size;
        res.tiles = tmp;
        res.hamming = hamm;
        res.manhattan = manh;
        return res;
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != getClass()) return false;
        Board that = (Board) other;
        for (int i = 0; i < n; i++) {
            if(!Arrays.equals(tiles[i], that.tiles[i]))
                return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}