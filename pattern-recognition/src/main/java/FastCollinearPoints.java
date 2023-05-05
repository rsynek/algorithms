import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastCollinearPoints {

    private final Point[] points;

    private final Collection<LineSegment> resultSegments;

    private final Map<Double, List<Point>> segmentSlopes;

    // finds all line resultSegments containing 4 points
    public FastCollinearPoints(Point[] points) {
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
        this.resultSegments = new ArrayList<>();
        this.segmentSlopes = new HashMap<>();
        solve();
    }

    private boolean arePointsEqual(Point a, Point b) {
        return a.compareTo(b) == 0;
    }

    private void solve() {
        Point[] originalPoints = Arrays.copyOf(points, points.length);
        for (int i = 0; i < originalPoints.length; i++) { // for each point determine it's slope with each other
            Point currentPoint = originalPoints[i];
            Arrays.sort(points, 0, points.length, currentPoint.slopeOrder());
            addSegmentsForSinglePoint(currentPoint, 0);
        }
    }

    private void addSegmentsForSinglePoint(Point point, int startingIndex) {
        int equalValuesFromIndex = -1;

        double lastSlope = point.slopeTo(points[startingIndex]);
        for (int i = startingIndex + 1; i < points.length; i++) {
            double currentSlope = point.slopeTo(points[i]);
            if (lastSlope == currentSlope) {
                if (equalValuesFromIndex == -1) { // first 2 points with equal slopes in sequence found
                    equalValuesFromIndex = i - 1;
                }
            } else {
                if (equalValuesFromIndex >= 0) { // some points with equal slope found
                    if (i - equalValuesFromIndex >= 3) { // line segment detected
                        List<Point> existingSegment = segmentSlopes.get(lastSlope);
                        List<Point> newSegment = createSegmentPoints(point, equalValuesFromIndex, i);
                        if (existingSegment == null || !isSegmentContainedIn(newSegment, existingSegment)) {
                            segmentSlopes.put(lastSlope, newSegment);
                            addLineSegment(newSegment);
                        }
                    }
                    equalValuesFromIndex = -1;
                }
                lastSlope = currentSlope;
            }
        }
    }

    private boolean isSegmentContainedIn(List<Point> source, List<Point> target) {
        boolean found = false;
        for (Point sourcePoint : source) {
            found = false;
            for (Point targetPoint : target) {
                if (targetPoint.compareTo(sourcePoint) == 0) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                break;
            }
        }
        return found;
    }

    private List<Point> createSegmentPoints(Point currentPoint, int startingIndex, int lastIndex) {
        int size = lastIndex - startingIndex + 1;
        List<Point> segmentPoints = new ArrayList<>();
        segmentPoints.add(currentPoint);

        for (int i = 1; i < size; i++) {
            segmentPoints.add(points[startingIndex + i - 1]);
        }
        Collections.sort(segmentPoints);
        return segmentPoints;
    }

    private void addLineSegment(List<Point> segmentPoints) {
        LineSegment lineSegment = new LineSegment(segmentPoints.get(0), segmentPoints.get(segmentPoints.size() - 1));
        resultSegments.add(lineSegment);
    }

    // the number of line resultSegments
    public int numberOfSegments() {
        return resultSegments.size();
    }

    // the line resultSegments
    public LineSegment[] segments() {
        return resultSegments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        String file = "/home/rsynek/projects/personal/algorithms/pattern-recognition/data/input8.txt";

        // read the n points from a file
        In in = new In(file);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line resultSegments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}