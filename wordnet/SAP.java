import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.Arrays;

public class SAP {
    private final Digraph graph;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(Arrays.asList(v), Arrays.asList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (null == v || null == w)
            throw new IllegalArgumentException();
        validate(v);
        validate(w);
        BreadthFirstDirectedPaths vs = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths ws = new BreadthFirstDirectedPaths(graph, w);

        int m = Integer.MAX_VALUE;

        for (int i = 0; i < graph.V(); i++)
            if (vs.hasPathTo(i) && ws.hasPathTo(i)) {
                int d = vs.distTo(i) + ws.distTo(i);
                if (m > d) m = d;
            }

        if (m == Integer.MAX_VALUE) return -1;

        return m;
    }

    private void validate(Iterable<Integer> in) {
        for (int v : in)
            if (v < 0 || v >= graph.V())
                throw new IllegalArgumentException();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (null == v || null == w)
            throw new IllegalArgumentException();
        validate(v);
        validate(w);
        BreadthFirstDirectedPaths vs = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths ws = new BreadthFirstDirectedPaths(graph, w);

        int m = Integer.MAX_VALUE;
        int a = -1;

        for (int i = 0; i < graph.V(); i++)
            if (vs.hasPathTo(i) && ws.hasPathTo(i)) {
                int d = vs.distTo(i) + ws.distTo(i);
                if (m > d) {
                    m = d;
                    a = i;
                }
            }

        return a;
    }
}