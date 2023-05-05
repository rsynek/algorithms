import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class BruteCollinearPoints {

    private final Point[] points;

    private final Collection<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points cannot be null.");
        }

        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            if (point == null) {
                throw new IllegalArgumentException("None of points can be null.");
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException("None of points can be null.");
                }
                if (arePointsEqual(points[i], points[j])) {
                    throw new IllegalArgumentException("Duplicate points are forbidden.");
                }
            }
        }

        this.points = points;
        this.segments = new ArrayList<>();
        solve();
    }

    private boolean arePointsEqual(Point a, Point b) {
        return a.compareTo(b) == 0;
    }

    private void solve() {
        int size = points.length;
        for (int a = 0; a < size; a++) {
            for (int b = a; b < size; b++) {
                for (int c = b; c < size; c++) {
                    for (int d = c; d < size; d++) {
                        Point [] fourTuple = new Point[] {points[a], points[b], points[c], points[d]};
                        Arrays.sort(fourTuple);
                        solveFourTuple(fourTuple);
                    }
                }
            }
        }
    }

    private void solveFourTuple(Point [] fourTuple) {
        boolean isLineVal = isLine(fourTuple);
        if (isLineVal) {
            segments.add(new LineSegment(fourTuple[0], fourTuple[3]));
        }
    }

    private boolean isLine(Point[] fourTuple) {
        double pToQ = fourTuple[0].slopeTo(fourTuple[1]);
        double qToR = fourTuple[1].slopeTo(fourTuple[2]);
        double rToS = fourTuple[2].slopeTo(fourTuple[3]);

        boolean slopesEqual = (pToQ == qToR) && (qToR == rToS);

        return slopesEqual && pToQ != Double.NEGATIVE_INFINITY;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

}