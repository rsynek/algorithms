import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PercolationTest {

    @Test
    public void testSingleRowPercolates() {
        final int size = 1;
        Percolation percolation = new Percolation(size);
        percolation.open(1,1);

        Assertions.assertThat(percolation.percolates()).isTrue();
        Assertions.assertThat(percolation.numberOfOpenSites()).isEqualTo(1);
    }

    @Test
    public void testTwoRowsPercolate() {
        final int size = 2;
        Percolation percolation = new Percolation(size);
        percolation.open(1,1);
        percolation.open(2,1);

        Assertions.assertThat(percolation.percolates()).isTrue();
        Assertions.assertThat(percolation.numberOfOpenSites()).isEqualTo(2);
    }
}
