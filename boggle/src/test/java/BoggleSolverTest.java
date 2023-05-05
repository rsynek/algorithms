import edu.princeton.cs.algs4.In;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class BoggleSolverTest {

    private static final String DATA_FOLDER = "data";
    private static final String YAWL_DICTIONARY = "dictionary-yawl.txt";

    @Test
    public void testBoardWithZeroPoints() {
        BoggleSolver bs = createBoggleSolverUsingYawlDictionary();

        BoggleBoard board = createBoggleBoard("board-points0.txt");
        Iterable<String> foundWords = bs.getAllValidWords(board);
        Assertions.assertThat(foundWords).isEmpty();
        Assertions.assertThat(computeScore(bs, foundWords)).isEqualTo(0);
    }

    @Test
    public void testBoardDiagonal() {
        BoggleSolver bs = createBoggleSolverUsingYawlDictionary();

        BoggleBoard board = createBoggleBoard("board-diagonal.txt");
        Iterable<String> foundWords = bs.getAllValidWords(board);
        Assertions.assertThat(foundWords).isNotEmpty().containsOnly("THE", "THEN", "HEN", "HEX");
        Assertions.assertThat(computeScore(bs, foundWords)).isEqualTo(4);
    }

    @Test
    public void testBoardWith5Points() {
        BoggleSolver bs = createBoggleSolverUsingYawlDictionary();

        BoggleBoard board = createBoggleBoard("board-points5.txt");
        Iterable<String> foundWords = bs.getAllValidWords(board);
        Assertions.assertThat(foundWords).isNotEmpty();
        Assertions.assertThat(computeScore(bs, foundWords)).isEqualTo(5);
    }

    private String [] readDictionary(String filename) {
        In in = null;
        try {
            in  = new In(DATA_FOLDER + "/" + filename);
            return in.readAllStrings();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private BoggleSolver createBoggleSolverUsingYawlDictionary() {
        return new BoggleSolver(readDictionary(YAWL_DICTIONARY));
    }

    private BoggleBoard createBoggleBoard(String filename) {
        return new BoggleBoard(DATA_FOLDER + "/" + filename);
    }

    private int computeScore(BoggleSolver bs, Iterable<String> words) {
        int score = 0;
        for (String word : words) {
            score += bs.scoreOf(word);
        }

        return  score;
    }
}
