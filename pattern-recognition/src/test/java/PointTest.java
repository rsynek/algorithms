import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PointTest {

    @Test
    public void testHorizontalLine() {
        Point p = new Point(10, 10);
        Point q = new Point(20, 10);

        Assertions.assertThat(p.slopeTo(q)).isZero();
        Assertions.assertThat(q.slopeTo(p)).isZero();
    }

    @Test
    public void testVerticalLine() {
        Point p = new Point(10, 10);
        Point q = new Point(10, 20);

        Assertions.assertThat(p.slopeTo(q)).isEqualTo(Double.POSITIVE_INFINITY);
        Assertions.assertThat(q.slopeTo(p)).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testDegenerateLine() {
        Point p = new Point(10, 10);
        Point q = new Point(10, 10);

        Assertions.assertThat(p.slopeTo(q)).isEqualTo(Double.NEGATIVE_INFINITY);
        Assertions.assertThat(q.slopeTo(p)).isEqualTo(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testSlopeOrderReflexivity() {
        Point p = new Point (66, 432);
        Point q = new Point(66, 128);

        Assertions.assertThat(p.slopeOrder().compare(q, q)).isEqualTo(0);
    }

    @Test
    public void testSlopeOrderAntisymmetry() {
        Point p = new Point (24, 406);
        Point q = new Point(24, 108);
        Point r = new Point(24, 123);

        Assertions.assertThat(p.slopeOrder().compare(q, r)).isEqualTo(0);
        Assertions.assertThat(p.slopeOrder().compare(r, q)).isEqualTo(0);
    }
}
