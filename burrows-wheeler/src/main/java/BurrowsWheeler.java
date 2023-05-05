import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        while (!BinaryStdIn.isEmpty()) {
            String originalString = BinaryStdIn.readString();
            char[] originalChars = originalString.toCharArray();
            CircularSuffixArray csa = new CircularSuffixArray(originalString);

            char[] buffer = new char[csa.length()];
            int startIndex = 0;
            int length = csa.length();
            for (int i = 0; i < length; i++) {
                if (csa.index(i) == 0) {
                    startIndex = i;
                }
                buffer[i] = originalChars[(csa.index(i) + length - 1) % length];
            }
            BinaryStdOut.write(startIndex);
            BinaryStdOut.write(new String(buffer));
        }

        BinaryStdIn.close();
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int firstIndex = BinaryStdIn.readInt();
        char [] t = BinaryStdIn.readString().toCharArray();

        int [] next = constructNext(t);

        printOriginalMessage(t, firstIndex, next);
    }


    private static int [] constructNext(char [] t) {
        int [] count = new int [R + 1];
        int [] next = new int [t.length];

        for (int i = 0; i < t.length; i++) {
            count[t[i] + 1]++;
        }

        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }

        for (int i = 0; i < t.length; i++) {
            next[count[t[i]]++] = i;
        }

        return next;
    }

    private static void printOriginalMessage(char[] t, int first, int[] next) {
        for (int i = 0, index = next[first]; i < next.length; i++, index = next[index]) {
            BinaryStdOut.write(t[index]);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Exactly one argument +/- is required.");
        }

        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException("Unknown argument.");
        }
    }
}
