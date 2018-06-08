import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class WordNet {
    private Map<String, Set<Integer>> nounsIds;
    private ArrayList<String> synsets;
    private Digraph graph;
    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernyms) {
        if (null == synsetsFile || null == hypernyms)
            throw new IllegalArgumentException();

        parseSynsets(synsetsFile);
        parseHypernyms(hypernyms);

        DirectedCycle dc = new DirectedCycle(graph);
        if (dc.hasCycle())
            throw new IllegalArgumentException();
        int count = 0;
        for (int v = 0; v < graph.V(); v++) {
            if (graph.outdegree(v) == 0) {
                count++;
            }
        }
        if (count > 1)
            throw new IllegalArgumentException();
    }

    private void parseSynsets(String synsetsFile) {
        In in = new In(synsetsFile);
        nounsIds = new HashMap<String, Set<Integer>>();
        synsets = new ArrayList<String>();

        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            synsets.add(fields[1]);
            String[] synset = fields[1].split(" ");
            for (String noun : synset) {
                if (!nounsIds.containsKey(noun))
                    nounsIds.put(noun, new HashSet<Integer>());
                nounsIds.get(noun).add(id);
            }
        }
    }

    private void parseHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        graph = new Digraph(synsets.size());
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int hid = Integer.parseInt(fields[i]);
                graph.addEdge(id, hid);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (null == word) throw new IllegalArgumentException();
        return nounsIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        Iterable<Integer> ai = nounsIds.get(nounA);
        Iterable<Integer> bi = nounsIds.get(nounB);
        SAP sap = new SAP(graph);
        return sap.length(ai, bi);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        Iterable<Integer> ai = nounsIds.get(nounA);
        Iterable<Integer> bi = nounsIds.get(nounB);
        SAP sap = new SAP(graph);
        int found = sap.ancestor(ai, bi);
        return synsets.get(found);
    }
}