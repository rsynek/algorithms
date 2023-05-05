public class Outcast {

    private final WordNet wordNet;

    /**
     * constructor takes a WordNet object
     */
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException("WordNet reference cannot be null.");
        }

        this.wordNet = wordnet;
    }

    /**
     * given an array of WordNet nouns, return an outcast
     */
    public String outcast(String[] nouns) {
        int maxSum = 0;
        int outcastIndex = 0;
        int sum = 0;
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                if (i != j) {
                   sum += wordNet.distance(nouns[i], nouns[j]);
                }
            }

            if (sum > maxSum) {
                maxSum = sum;
                outcastIndex = i;
            }

            sum = 0;
        }

        return nouns[outcastIndex];
    }

    public static void main(String[] args) {

    }
}
