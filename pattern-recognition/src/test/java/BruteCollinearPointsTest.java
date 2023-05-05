import org.assertj.core.api.Assertions;
import org.junit.Test;

public class BruteCollinearPointsTest extends AbstractTestBase {

    @Test
    public void testInputWithNulls() {
        Point [] points = readInputFile("nulls.txt");
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new BruteCollinearPoints(points));
    }

    @Test
    public void testDuplicatePoints() {
        Point[] points = new Point[]{new Point(10, 10), new Point(5, 5), new Point(10, 10)};
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new BruteCollinearPoints(points));
    }

    @Test
    public void testSinglePoint() {
        Point [] points = readInputFile("input1.txt");
        Assertions.assertThat(points).hasSize(1);

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        Assertions.assertThat(bruteCollinearPoints.numberOfSegments()).isZero();
    }

    @Test
    public void testTwoPoints() {
        Point [] points = readInputFile("input2.txt");
        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);

        Assertions.assertThat(bruteCollinearPoints.numberOfSegments()).isZero();
    }

    @Test
    public void testEightPoints() {
        Point [] points = readInputFile("input8.txt");
        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);

        Assertions.assertThat(bruteCollinearPoints.numberOfSegments()).isEqualTo(2);
    }

}
