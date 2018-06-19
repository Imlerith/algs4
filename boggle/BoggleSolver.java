import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.ArrayList;

public class BoggleSolver
{
    private TS ts;
    private boolean[][] marked;
    private int height;
    private int width;
    private HashSet<String> result;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        ts = new TS();
        for (String word: dictionary)
            ts.add(word);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        height = board.rows();
        width = board.cols();
        result = new HashSet<>();
        marked = new boolean[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                dfs(board, new int[] {i, j}, "");
        return result;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int len = word.length();
        if (len < 3) return 0;
        else if (len < 5) return 1;
        else if (len == 5) return 2;
        else if (len == 6) return 3;
        else if (len == 7) return 5;
        else return 11;
    }

    private void dfs(BoggleBoard b, int[] rc, String res) {
        int r = rc[0], c = rc[1];
        char s = b.getLetter(r, c);
        if ('Q' == s) res += "QU";
        else res += s;
        if (ts.contains(res) && res.length() > 2) result.add(res);
        if (ts.containsPrefix(res)) return;

        marked[r][c] = true;
        for (int[] item : getNeighbors(rc))
            if (!isMarked(item))
                dfs(b, item, res);
        marked[r][c] = false;
    }

    private ArrayList<int[]> getNeighbors(int[] rc) {
        ArrayList<int[]> res = new ArrayList<>();
        int r = rc[0], c = rc[1];

        if (c-1 >= 0) {
            res.add(new int[]{r, c-1}); // left
            if (r-1 >= 0) res.add(new int[]{r-1, c-1}); // upperleft
            if (r+1 < height) res.add(new int[]{r+1, c-1}); // lowerleft
        }

        if (c+1 < width) {
            res.add(new int[]{r, c+1}); // right
            if (r-1 >= 0) res.add(new int[]{r-1, c+1}); // upperright
            if (r+1 < height) res.add(new int[]{r+1, c+1}); // lowerright
        }

        if (r-1 >= 0) res.add(new int[]{r-1, c}); // top

        if (r+1 < height) res.add(new int[]{r+1, c}); // bottom

        return res;
    }

    private boolean isMarked(int[] rc) { return marked[rc[0]][rc[1]]; }

    private class TS {
        private static final int R = 26;

        private Node root;

        private class Node {
            private Node[] next = new Node[R];
            private boolean isString;
        }

        public boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c-'A'], key, d+1);
        }

        public void add(String key) {
            if (key == null) throw new IllegalArgumentException("argument to add() is null");
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                x.isString = true;
            }
            else {
                char c = key.charAt(d);
                x.next[c-'A'] = add(x.next[c-'A'], key, d+1);
            }
            return x;
        }


        public boolean containsPrefix(String key) {
            Node x = get(root, key, 0);
            return x == null;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}