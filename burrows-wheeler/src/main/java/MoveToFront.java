import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char [] sequence = new char[R];

        for (char i = 0; i < sequence.length; i++) {
            sequence[i] = i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char currentChar = BinaryStdIn.readChar();

            char i = 0;
            char newChar = currentChar, lastChar;
            do {
                lastChar = sequence[i];
                sequence[i] = newChar;
                newChar = lastChar;
            } while (i < R - 1 && currentChar != sequence[++i]);
            sequence[i] = newChar;

            BinaryStdOut.write(i);
        }

        BinaryStdIn.close();
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char [] sequence = new char[R];

        for (char i = 0; i < sequence.length; i++) {
            sequence[i] = i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char currentIndex = BinaryStdIn.readChar();
            char currentChar = sequence[currentIndex];

            char i = 0;
            char newChar = currentChar, lastChar;
            do {
                lastChar = sequence[i];
                sequence[i] = newChar;
                newChar = lastChar;
            } while (++i != currentIndex && i < R - 1);
            sequence[i] = newChar;

            BinaryStdOut.write(currentChar);
        }

        BinaryStdIn.close();
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Exactly one argument +/- is required.");
        }

        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException("Unknown argument.");
        }
    }
}