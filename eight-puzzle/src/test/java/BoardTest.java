import org.assertj.core.api.Assertions;
import org.junit.Test;

public class BoardTest {

    @Test
    public void testDimension() {
        int [][] blocks = new int[5][5];
        Board board = new Board(blocks);

        Assertions.assertThat(board.dimension()).isEqualTo(5);
    }
}
