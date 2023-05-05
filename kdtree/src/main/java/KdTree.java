import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private static class KdTreeNode {
        private final Point2D point;
        private KdTreeNode leftChild = null;
        private KdTreeNode rightChild = null;
        private final int depth;
        private final RectHV rectangle;

        KdTreeNode(Point2D point, RectHV rectangle, int depth) {
            this.point = point;
            this.rectangle = rectangle;
            this.depth = depth;
        }

        private boolean isVertical() {
            return depth % DIMENSION == 1;
        }

        private boolean isLeft(Point2D queryPoint) {
            return isVertical() ? Double.compare(queryPoint.x(), point.x()) < 0
                    : Double.compare(queryPoint.y(), point.y()) < 0;
        }

        private void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            point.draw();

            double x1, x2, y1, y2;
            if (isVertical()) {
                x1 = x2 = point.x();
                y1 = rectangle.ymin();
                y2 = rectangle.ymax();
                StdDraw.setPenColor(StdDraw.RED);
            } else {
                x1 = rectangle.xmin();
                x2 = rectangle.xmax();
                y1 = y2 = point.y();
                StdDraw.setPenColor(StdDraw.BLUE);
            }

            StdDraw.line(x1, y1, x2, y2);

            if (leftChild != null) {
                leftChild.draw();
            }

            if (rightChild != null) {
                rightChild.draw();
            }
        }
    }

    private static final byte DIMENSION = 2;

    private static final byte BORDER_MIN = 0;
    private static final byte BORDER_MAX = 1;

    private KdTreeNode root;

    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (root == null) {
            root = new KdTreeNode(p, new RectHV(BORDER_MIN, BORDER_MIN, BORDER_MAX,BORDER_MAX), 1);
            size = 1;
        } else {
            root = insert(root, p);
        }
    }

    private KdTreeNode insert(KdTreeNode node, Point2D point) {
        if (node.point.equals(point)) {
            return node;
        }

        if (node.isLeft(point)) {
            if (node.leftChild == null) {
                node.leftChild = createNewNode(node, point, true);
            } else {
                node.leftChild = insert(node.leftChild, point);
            }
        } else {
            if (node.rightChild == null) {
                node.rightChild = createNewNode(node, point, false);
            } else {
                node.rightChild = insert(node.rightChild, point);
            }
        }

        return node;
    }

    private KdTreeNode createNewNode(KdTreeNode parent, Point2D point, boolean insertLeft) {
        this.size++;
        RectHV subRectangle = getSubRectangle(parent, parent.point, insertLeft);
        return new KdTreeNode(point, subRectangle, parent.depth + 1);
    }

    private RectHV getSubRectangle(KdTreeNode parentNode, Point2D delimiterPoint, boolean left) {
        RectHV rectangle = parentNode.rectangle;
        boolean vertical = parentNode.isVertical();
        if (vertical) {
            if (left) {
                return new RectHV(rectangle.xmin(), rectangle.ymin(), delimiterPoint.x(), rectangle.ymax());
            } else {
                return new RectHV(delimiterPoint.x(), rectangle.ymin(), rectangle.xmax(), rectangle.ymax());
            }
        } else {
            if (left) {
                return new RectHV(rectangle.xmin(), rectangle.ymin(), rectangle.xmax(), delimiterPoint.y());
            } else {
                return new RectHV(rectangle.xmin(), delimiterPoint.y(), rectangle.xmax(), rectangle.ymax());
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p);
    }

    private boolean contains(KdTreeNode node, Point2D point) {
        if (node == null) {
            return false;
        }

        if (node.point.equals(point)) {
            return true;
        }

        if (node.isLeft(point)) {
            return contains(node.leftChild, point);
        } else {
            return contains(node.rightChild, point);
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(KdTreeNode node) {
        if (node != null) {
            node.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> pointsInRectangle = new LinkedList<>();
        range(root, rect, pointsInRectangle);

        return pointsInRectangle;
    }

    private void range(KdTreeNode node, RectHV queryRect, List<Point2D> pointsInRectangle) {
        if (queryRect.contains(node.point)) {
            pointsInRectangle.add(node.point);
        }

        if (node.leftChild != null && queryRect.intersects(node.leftChild.rectangle)) {
            range(node.leftChild, queryRect, pointsInRectangle);
        }

        if (node.rightChild != null && queryRect.intersects(node.rightChild.rectangle)) {
            range(node.rightChild, queryRect, pointsInRectangle);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return nearest(root, p, root.point);
    }

    private Point2D nearest(KdTreeNode node, Point2D p, Point2D currentBest) {
        if (node == null) {
            return currentBest;
        }

        double currentBestToQueryPointDistance = currentBest.distanceSquaredTo(p);

        /*
           if the closest point discovered so far is closer than the distance between the query point
           and the rectangle corresponding to a node, there is no need to explore that node (or its subtrees)
         */
        double rectToQueryPointDistance = node.rectangle.distanceSquaredTo(p);
        if (currentBestToQueryPointDistance < rectToQueryPointDistance) {
            return currentBest;
        }

        if (node.point.distanceSquaredTo(p) < currentBestToQueryPointDistance) {
            currentBest = node.point;
        }

        KdTreeNode firstChild, secondChild;
        if (node.isLeft(p)) {
            firstChild = node.leftChild;
            secondChild = node.rightChild;
        } else {
            firstChild = node.rightChild;
            secondChild = node.leftChild;
        }

        Point2D firstChildBest = nearest(firstChild, p, currentBest);
        Point2D secondChildBest = nearest(secondChild, p, currentBest);

        Point2D bestChild = closestTo(firstChildBest, secondChildBest, p);

        return closestTo(bestChild, currentBest, p);
    }

    private Point2D closestTo(Point2D a, Point2D b, Point2D to) {
         return a.distanceSquaredTo(to) < b.distanceSquaredTo(to) ? a : b;
    }
}
