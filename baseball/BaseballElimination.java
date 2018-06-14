import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class BaseballElimination {
    private int n;
    private String[] teamNames;
    private int[] wins;
    private int[] losses;
    private int[] rest;
    private int[][] sched;
    private Map<String, Integer> teamIds;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        teamNames = new String[n];

        wins = new int[n];
        losses = new int[n];
        rest = new int[n];
        sched = new int[n][n];
        teamIds = new HashMap<String, Integer>();

        for (int i = 0; i < n; i++) {
            String teamName = in.readString();
            teamNames[i] = teamName;
            teamIds.put(teamName, i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            rest[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                sched[i][j] = in.readInt();
            }
        }
    }
    public int numberOfTeams() {
        return n;
    }
    public Iterable<String> teams() {
        return Arrays.asList(teamNames);
    }
    public int wins(String team) {
        int t = teamIds.get(team);
        return wins[t];
    }
    public int losses(String team) {
        int t = teamIds.get(team);
        return losses[t];
    }
    public int remaining(String team) {
        int t = teamIds.get(team);
        return rest[t];
    }
    public int against(String a, String b) {
        int i = teamIds.get(a);
        int j = teamIds.get(b);

        return sched[i][j];
    }
    public boolean isEliminated(String team) {
        if (!teamIds.containsKey(team))
            throw new java.lang.IllegalArgumentException();

        int c = teamIds.get(team); // current team
        // trivial
        for (int i = 0; i < n; i++)
            if (c != i && wins[c] + rest[c] < wins[i])
                return true;

        int m = n * (n - 1) / 2;

        int s = m + n; // sourceIndex
        int t = s + 1; // targetIndex
        
        FlowNetwork G = new FlowNetwork(m + n + 2);

        int g = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // s -> game -> team -> t
                G.addEdge(new FlowEdge(s, g, sched[i][j])); // s -> game
                G.addEdge(new FlowEdge(g, m + i, Double.POSITIVE_INFINITY)); // game -> team1
                G.addEdge(new FlowEdge(g, m + j, Double.POSITIVE_INFINITY)); // game -> team2
                g++;
            }
            G.addEdge(new FlowEdge(m + i, t, wins[c] + rest[c] - wins[i])); // team -> t
        }
        FordFulkerson ff = new FordFulkerson(G, s, t);
        for (FlowEdge edge : G.adj(s))
            if (edge.flow() != edge.capacity())
                return true;

        return false;
    }
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamIds.containsKey(team)) throw new java.lang.IllegalArgumentException();

        int c = teamIds.get(team);
        ArrayList<String> res = new ArrayList<String>();
        for (int i = 0; i < n; i++)
            if (c != i && wins[c] + rest[c] < wins[i])
                res.add(teamNames[i]);

        if (res.size() > 0) return res;

        int m = n * (n - 1) / 2;

        int s = m + n; // sourceIndex
        int t = s + 1; // targetIndex

        FlowNetwork G = new FlowNetwork(m + n + 2);

        int g = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                G.addEdge(new FlowEdge(s, g, sched[i][j]));
                G.addEdge(new FlowEdge(g, m + i, Double.POSITIVE_INFINITY));
                G.addEdge(new FlowEdge(g, m + j, Double.POSITIVE_INFINITY));
                g++;
            }
            G.addEdge(new FlowEdge(m + i, t, wins[c] + rest[c] - wins[i]));
        }
        FordFulkerson ff = new FordFulkerson(G, s, t);

        for (int i = m, j = 0; i < (m + n); i++, j++)
            if (ff.inCut(i))
                res.add(teamNames[j]);

        return res.size() > 0 ? res : null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
