import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.*;

/**
 * Brute-force implementation. Write a mutable data type PointSET.java that represents a set of
 * points in the unit square.
 * Implement the following API by using a red–black BST (using either SET from algs4.jar or java.util.TreeSet).
 * */
public class PointSET {

    private TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        Comparator<Point2D> comparator =
                (pointA, pointB) -> (pointA.compareTo(pointB));
        points = new TreeSet<>(comparator);
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkArgument(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkArgument(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        points.forEach(p -> p.draw());
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        checkArgument(rect);
        List<Point2D> inRect = new LinkedList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                inRect.add(point);
            }
        }

        return inRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkArgument(p);

        Comparator<Point2D> comparator =
                (pointA, pointB) -> (Double.compare(p.distanceSquaredTo(pointA), p.distanceSquaredTo(pointB)));


        TreeSet<Point2D> nearest = new TreeSet<>(comparator);
        nearest.addAll(points);

        return nearest.first();
    }

    private void checkArgument(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }
    }

    public static void main(String[] args) {

    }
}
