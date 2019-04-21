/* *****************************************************************************
 *  Name:
 *  Date: 2019-04-15
 *  Description: brute force solution
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> tree;

    public PointSET() {                               // construct an empty tree of points
        tree = new TreeSet<>();
    }

    public boolean isEmpty() {                      // is the tree empty?
        return tree.isEmpty();
    }

    public int size() {                         // number of points in the tree
        return tree.size();
    }

    public void insert(
            Point2D p) {              // add the point to the tree (if it is not already in the tree)
        if (p == null) throw new java.lang.IllegalArgumentException();
        tree.add(p);
    }

    public boolean contains(Point2D p) {            // does the tree contain point p?
        if (p == null) throw new java.lang.IllegalArgumentException();
        return tree.contains(p);
    }

    public void draw() {                         // draw all points to standard draw
        for (Point2D point : tree) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect) {             // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new java.lang.IllegalArgumentException();
        TreeSet<Point2D> inRect = new TreeSet<>();

        for (Point2D point : tree.subSet(new Point2D(rect.xmin(), rect.ymin()), true, new Point2D(rect.xmax(), rect.ymax()), true)) {
            if (rect.contains(point)) {
                inRect.add(point);
            }
        }
        return inRect;
    }

    public Point2D nearest(
            Point2D p) {             // a nearest neighbor in the tree to point p; null if the tree is empty
        if (p == null) throw new java.lang.IllegalArgumentException();
        double min = 100;
        Point2D nnPoint = null;
        for (Point2D point : tree) {
            if (min > point.distanceSquaredTo(p)) {
                nnPoint = point;
                min = point.distanceSquaredTo(p);
            }
        }
        return nnPoint;
    }

    public static void main(
            String[] args) {                  // unit testing of the methods (optional)
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();

    }
}
