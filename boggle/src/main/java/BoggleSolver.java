import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoggleSolver {

    private final Trie dictionary;
    private final Set<String> wordsFound;
    private boolean [][] marked;
    private BoggleBoard board;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new Trie();
        for (String word : dictionary) {
            this.dictionary.add(word);
        }
        this.wordsFound = new HashSet<>();
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.wordsFound.clear();
        this.board = board;
        this.marked = new boolean[board.rows()][board.cols()];

        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                solve(row, col);
            }
        }

        return Collections.unmodifiableSet(wordsFound);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return dictionary.contains(word) ? scoreByLength(word) : 0;
    }

    private int scoreByLength(String word) {
        final int length = word.length();

        if (length < 3) {
            return 0;
        } else if (length <= 4) {
            return 1;
        } else if (length == 5) {
            return 2;
        } else if (length == 6) {
            return 3;
        } else if (length == 7) {
            return 5;
        } else {
            return 8;
        }
    }

    private void solve(int startRow, int startCol) {
        dfs(new char[0], startRow, startCol);
    }

    /**
     * when the current path corresponds to a string that is not a prefix of any word in the dictionary,
     * there is no need to expand the path further. To do this, you will need to create a data structure
     * for the dictionary that supports the prefix query operation: given a prefix, is there any word in
     * the dictionary that starts with that prefix?
     */
    private void dfs(char[] currentWordBuffer, int row, int col) {
        marked[row][col] = true;
        char currentLetter = board.getLetter(row, col);

        char[] currentWord;
        if (currentLetter == 'Q') {
            currentWord = Arrays.copyOf(currentWordBuffer, currentWordBuffer.length + 2);
            currentWord[currentWord.length - 2] = 'Q';
            currentWord[currentWord.length - 1] = 'U';
        } else {
            currentWord = Arrays.copyOf(currentWordBuffer, currentWordBuffer.length + 1);
            currentWord[currentWord.length - 1] = currentLetter;
        }

        checkWordAndExploreNeighbours(currentWord, row, col);
        marked[row][col] = false;
    }

    private void checkWordAndExploreNeighbours(char[] currentWord, int row, int col) {
        String currentWordAsString = String.valueOf(currentWord);

        if (!dictionary.containsPrefix(currentWordAsString)) {
            return;
        }

        addWordToFoundIfExists(currentWordAsString);
        exploreNeighbours(currentWord, row, col);
    }

    private void addWordToFoundIfExists(String word) {
        if (word.length() >= 3 && dictionary.contains(word)) {
            wordsFound.add(word);
        }
    }

    private void exploreNeighbours(char[] currentWord, int row, int col) {
        for (Cell cell : generateNeighbours(row, col)) {
            if (!marked[cell.row][cell.col]) {
                dfs(currentWord, cell.row, cell.col);
            }
        }
    }

    private List<Cell> generateNeighbours(int currentRow, int currentCol) {
        Cell[] neighbours = {
                new Cell(currentRow, currentCol - 1),
                new Cell(currentRow, currentCol + 1),
                new Cell(currentRow - 1, currentCol),
                new Cell(currentRow - 1, currentCol - 1),
                new Cell(currentRow - 1, currentCol + 1),
                new Cell(currentRow + 1, currentCol),
                new Cell(currentRow + 1, currentCol - 1),
                new Cell(currentRow + 1, currentCol + 1),
        };

        List<Cell> validNeighbours = new ArrayList<>();
        for (Cell cell : neighbours) {
            if (cell.row >= 0 && cell.row < board.rows() && cell.col >= 0 && cell.col < board.cols()) {
                validNeighbours.add(cell);
            }
        }

        return validNeighbours;
    }

    private static class Trie {
        private static final int R = 26;
        private static final char SHIFT = 'A';

        private Node root;

        private class Node {
            private Node[] next = new Node[R];
            private boolean isString;
        }

        Trie() {
        }

        public boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }

        public boolean containsPrefix(String prefix) {
            Node x = root;
            for (int i = 0; i < prefix.length(); i++) {
                if (x != null) {
                    char c = getIndex(prefix.charAt(i));
                    x = x.next[c];
                }
            }
            return x != null;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = getIndex(key.charAt(d));
            return get(x.next[c], key, d+1);
        }

        public void add(String key) {
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) {
                x = new Node();
            }
            if (d == key.length()) {
                x.isString = true;
            } else {
                char c = getIndex(key.charAt(d));
                x.next[c] = add(x.next[c], key, d+1);
            }
            return x;
        }

        private char getIndex(char c) {
            return (char) (c - SHIFT);
        }
    }

    private class Cell {
        final int row;
        final int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}