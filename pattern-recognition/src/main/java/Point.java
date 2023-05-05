import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws this point
    public void draw() {
        StdDraw.point(x, y);

        StdDraw.circle(x, y, 100);
        StdDraw.textLeft(x + 500, y + 500, toString());
        StdDraw.setPenColor();
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between the invoking point (x0, y0) and the argument point (x1, y1), which is given by
     * the formula (y1 − y0) / (x1 − x0).
     * Treat the slope of a horizontal line segment as positive zero; treat the slope of a vertical line segment
     * as positive infinity; treat the slope of a degenerate line segment (between a point and itself)
     * as negative infinity.
     * */
    public double slopeTo(Point that) {
        if (that.y - y == 0 && that.x - x == 0) {
            return Double.NEGATIVE_INFINITY;
        } else if (that.y - y == 0) {
            return 0.0;
        } else if (that.x - x == 0) {
            return Double.POSITIVE_INFINITY;
        } else {
            return ((double) (that.y - y)) / (that.x - x);
        }
    }

    /**
     * Return a comparator that compares its two argument points by the slopes they make with the invoking
     * point (x0, y0). Formally, the point (x1, y1) is less than the point (x2, y2) if and only if
     * the slope (y1 − y0) / (x1 − x0) is less than the slope (y2 − y0) / (x2 − x0).
     * Treat horizontal, vertical, and degenerate line segments as in the slopeTo() method.
     * */
    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }

    /**
     * Compare two points by y-coordinates, breaking ties by x-coordinates. Formally, invoking point (x0, y0)
     * is less than the argument point (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     */
    public int compareTo(Point that) {
        int yDiff = y - that.y;

        if (yDiff == 0) {
            return x - that.x;
        } else {
            return yDiff;
        }
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    private class SlopeComparator implements Comparator<Point> {

        /**
         * Formally, the point (x1, y1) is less than the point (x2, y2) if and only if
         * the slope (y1 − y0) / (x1 − x0) is less than the slope (y2 − y0) / (x2 − x0).
         * Treat horizontal, vertical, and degenerate line segments as in the slopeTo() method.
         * */
        @Override
        public int compare(Point pointA, Point pointB) {
            if (pointA == null || pointB == null) {
                throw new NullPointerException("At least one of the compared points is null.");
            }

            Double slopeA = slopeTo(pointA);
            Double slopeB = slopeTo(pointB);

            return slopeA.compareTo(slopeB);
        }
    }
}