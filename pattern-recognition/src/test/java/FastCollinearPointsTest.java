import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;

public class FastCollinearPointsTest extends AbstractTestBase {

    @Test
    public void testInputWithNulls() {
        Point [] points = readInputFile("nulls.txt");
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new FastCollinearPoints(points));
    }

    @Test
    public void testDuplicatePoints() {
        Point[] points = new Point[]{new Point(10, 10), new Point(5, 5), new Point(10, 10)};
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new FastCollinearPoints(points));
    }

    @Test
    public void testSinglePoint() {
        Point [] points = readInputFile("input1.txt");
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

        Assertions.assertThat(fastCollinearPoints.numberOfSegments()).isZero();
    }

    @Test
    public void testTwoPoints() {
        Point [] points = readInputFile("input2.txt");
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

        Assertions.assertThat(fastCollinearPoints.numberOfSegments()).isZero();
    }

    @Test
    public void testSixPoints() {
        Point [] points = readInputFile("input6.txt");
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

        Assertions.assertThat(fastCollinearPoints.numberOfSegments()).isEqualTo(1);
    }

    @Test
    public void testEightPoints() {
        Point [] points = readInputFile("input8.txt");
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

        Assertions.assertThat(fastCollinearPoints.numberOfSegments()).isEqualTo(2);
        Arrays.stream(fastCollinearPoints.segments()).forEach(
                segment -> System.out.println(segment)
        );
    }

}
