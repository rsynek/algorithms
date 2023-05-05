import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {

    private final Digraph wordNetGraph;
    private final Map<Integer, String> synsets;
    private final Map<String, Set<Integer>> nounsToSynsets;
    private final SAP sap;

    /**
     * Constructor takes the name of the two input files
     * */
    public WordNet(String synsetsFile, String hypernymsFile) {
        if (synsetsFile == null) {
            throw new IllegalArgumentException("Cannot accept null synsets.");
        }

        if (hypernymsFile == null) {
            throw new IllegalArgumentException("Cannot accept null hypernyms");
        }

        synsets = new HashMap<>();
        nounsToSynsets = new HashMap<>();

        readSynsets(synsetsFile);
        wordNetGraph = readHypernyms(hypernymsFile, synsets.size());

        checkProperties();

        sap = new SAP(wordNetGraph);
    }

    private void readSynsets(final String synsetsFile) {
        In input = new In(synsetsFile);

        while (input.hasNextLine()) {
            String line = input.readLine();
            String [] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];

            synsets.put(id, synset);
            for (String noun : synset.split(" ")) {
                Set<Integer> synsetIdsForNoun = nounsToSynsets.get(noun);
                if (synsetIdsForNoun == null) {
                    synsetIdsForNoun = new HashSet<>();
                    nounsToSynsets.put(noun, synsetIdsForNoun);
                }
                synsetIdsForNoun.add(id);
            }
        }

        input.close();
    }

    /**
     * @return Digraph modelling the relationship between synsets (hypernyms -> hyponym) via their IDs.
     * */
    private Digraph readHypernyms(final String hypernymsFile, final int synsetSize) {
        In input = new In(hypernymsFile);

        Digraph wordNet = new Digraph(synsetSize);

        while (input.hasNextLine()) {
            String line = input.readLine();
            String [] fields = line.split(",", 2);

            if (fields.length == 2) {
                int hyponym = Integer.parseInt(fields[0]);
                String[] hypernyms = fields[1].split(",");

                for (String hypernym : hypernyms) {
                    wordNet.addEdge(hyponym, Integer.parseInt(hypernym));
                }
            }
        }
        input.close();

        return wordNet;
    }

    /**
     * @return all WordNet nouns
     * */
    public Iterable<String> nouns() {
        return Collections.unmodifiableSet(nounsToSynsets.keySet());
    }

    /**
     * is the word a WordNet noun?
     * */
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Word cannot be null.");
        }

        return nounsToSynsets.containsKey(word);
    }

    /**
     * distance between nounA and nounB (defined below)
     * */
    public int distance(String nounA, String nounB) {
        Set<Integer> synsetsForNounA = nounsToSynsets.get(nounA);
        Set<Integer> synsetsForNounB = nounsToSynsets.get(nounB);

        return sap.length(synsetsForNounA, synsetsForNounB);
    }

    /**
     * a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
     * in a shortest ancestral path (defined below)
     **/
    public String sap(String nounA, String nounB) {
        Set<Integer> synsetsForNounA = nounsToSynsets.get(nounA);
        Set<Integer> synsetsForNounB = nounsToSynsets.get(nounB);

        int ancestorId = sap.ancestor(synsetsForNounA, synsetsForNounB);
        return synsets.get(ancestorId);
    }

    private void checkProperties() {

        DirectedCycle dc = new DirectedCycle(wordNetGraph);

        if (dc.hasCycle()) {
            throw new IllegalArgumentException("The graph contains at least 1 cycle.");
        }

        int roots = 0;
        for (int vertex = 0; vertex < wordNetGraph.V(); vertex++) {
            if (wordNetGraph.outdegree(vertex) == 0) {
                roots++;
            }
        }

        if (roots != 1) {
            throw new IllegalArgumentException("The graph contains " + roots + " roots.");
        }

    }
}
