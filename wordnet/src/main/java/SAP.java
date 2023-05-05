import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class SAP {

    private final Digraph digraph;

    private Integer cachedLength = null;

    /**
     * constructor takes a digraph (not necessarily a DAG)
     */
    public SAP(Digraph digraph) {
        if (digraph == null) {
            throw new IllegalArgumentException("Digraph cannot be null.");
        }
        this.digraph = new Digraph(digraph);
    }

    /**
     * length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {
        checkInput(v);
        checkInput(w);
        return length(Arrays.asList(v), Arrays.asList(w));
    }

    /**
     * a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {
        checkInput(v);
        checkInput(w);
        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    /**
     * length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkInput(v);
        checkInput(w);

        int ancestor = ancestor(v, w);

        if (ancestor >= 0) {
            if (cachedLength != null) {
                return cachedLength;
            }

            BreadthFirstDirectedPaths breadthFirstPathsFromV = new BreadthFirstDirectedPaths(digraph, v);
            BreadthFirstDirectedPaths breadthFirstPathsFromW = new BreadthFirstDirectedPaths(digraph, w);

            return breadthFirstPathsFromV.distTo(ancestor) + breadthFirstPathsFromW.distTo(ancestor);
        } else {
            return -1;
        }
    }

    /**
     * a common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkInput(v);
        checkInput(w);

        cachedLength = null;

        BreadthFirstDirectedPaths breadthFirstPathsFromV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths breadthFirstPathsFromW = new BreadthFirstDirectedPaths(digraph, w);

        int minLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (breadthFirstPathsFromV.hasPathTo(vertex) && breadthFirstPathsFromW.hasPathTo(vertex)) {
                int length = breadthFirstPathsFromV.distTo(vertex) + breadthFirstPathsFromW.distTo(vertex);
                if (length < minLength) {
                    minLength = length;
                    ancestor = vertex;
                }
            }
        }

        if (ancestor > -1) {
            cachedLength = minLength;
        }

        return ancestor;
    }

    private void checkInput(int vertex) {
        if (vertex < 0 || vertex >= digraph.V()) {
            throw new IllegalArgumentException("Vertex is out of boundaries.");
        }
    }

    private void checkInput(Iterable<Integer> vertexes) {
        if (vertexes == null) {
            throw new IllegalArgumentException("A groups of vertexes cannot be null.");
        }

        for (int vertex : vertexes) {
            checkInput(vertex);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
