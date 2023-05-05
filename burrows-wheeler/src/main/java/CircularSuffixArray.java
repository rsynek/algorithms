import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

    private char [] originalString;
    private Integer [] indexes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Input string cannot be null.");
        }

        this.originalString = s.toCharArray();
        this.indexes = new Integer[originalString.length];

        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        sort();
    }

    private void sort() {
        Arrays.sort(indexes, new Comparator<Integer>() {

            @Override
            public int compare(final Integer firstIndex, final Integer secondIndex) {
                int length = length();
                for (int i = 0; i < length; i++) {
                    char first = originalString[(i + firstIndex) % length];
                    char second = originalString[(i + secondIndex) % length];

                    if (first > second) {
                        return 1;
                    } else if (first < second) {
                        return -1;
                    }
                }
                return 0;
            }
        });
    }

    // length of s
    public int length() {
        return originalString.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length()-1) {
            throw new IllegalArgumentException("Index out of bounds.");
        }
        return indexes[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        if (args.length == 1) {
            String input = args[0];
            CircularSuffixArray csa = new CircularSuffixArray(input);
            for (int i = 0; i < csa.length(); i++) {
                System.out.println(String.format("%2d %c", csa.index(i), csa.originalString[csa.index(i)]));
            }
        }
    }
}
