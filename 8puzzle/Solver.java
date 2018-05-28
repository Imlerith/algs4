import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;
import java.util.Comparator;

public class Solver {
    private SolverStep solution;
    private boolean isNotSolvable;
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (null == initial)
            throw new IllegalArgumentException();
        SolverStep min, tmin;
        MinPQ<SolverStep> pq = new MinPQ<>(new SolverComparator());
        MinPQ<SolverStep> tpq = new MinPQ<>(new SolverComparator());

        pq.insert(new SolverStep(initial));
        tpq.insert(new SolverStep(initial.twin()));

        min = step(pq);
        tmin = step(tpq);

        while (!min.board.isGoal() && !tmin.board.isGoal()) {
            min = step(pq);
            tmin = step(tpq);
        }

        if (tmin.board.isGoal())
            isNotSolvable = true;
        else
            solution = min;
    }
    private SolverStep step(MinPQ<SolverStep> pq) {
        SolverStep min = pq.delMin();
        Iterable<Board> neighbors = min.board.neighbors();

        for (Board b: neighbors) {
            if (null == min.previous || !b.equals(min.previous.board))
                pq.insert(new SolverStep(b, min));
        }
        return min;
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return !isNotSolvable;
    }
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return isNotSolvable ? -1 : solution.moves;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if (isNotSolvable) return null;
        ArrayDeque<Board> s = new ArrayDeque<>();
        SolverStep tmp = solution;
        while (null != tmp) {
            s.push(tmp.board);
            tmp = tmp.previous;
        }
        return s;
    }

    private static class SolverStep {

        private int moves;
        private Board board;
        private SolverStep previous;

        private SolverStep(Board b) {
            board = b;
            moves = 0;
            previous = null;
        }

        private SolverStep(Board b, SolverStep prev) {
            board = b;
            moves = prev.moves + 1;
            previous = prev;
        }

        public int priority() {
            return board.manhattan() + moves;
        }

    }

    private static class SolverComparator implements Comparator<SolverStep> {
        public int compare(SolverStep a, SolverStep b) {
            return a.priority() - b.priority();
        }
    }
}