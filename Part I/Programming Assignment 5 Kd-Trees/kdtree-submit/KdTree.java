/* *****************************************************************************
 *  Name:
 *  Date: 2019-04-15
 *  Description: 2-d tree solution
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private Point root;
    // nearest point of the query point
    private Point2D champion;
    // nearest distance from query point to the current champion
    private double distance;
    // size of the current tree
    private int cnt;
    // VIP! use variables outside of the deep recursion method to save memory
    private double x;
    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;
    private double y;
    private RectHV rect;
    private ArrayList<Point2D> set;

    // construct an empty set of points
    public KdTree() {
        root = null;
        cnt = 0;
    }

    public boolean isEmpty() {                  // is the set empty?
        return root == null;
    }

    // number of points in the set
    public int size() {
        return cnt;
    }

    // add the point to the tree if it is not already in the tree
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        Point rootTmp = new Point(p);
        if (root == null) {
            rootTmp.rectHV = new RectHV(0, 0, 1, 1);
        }
        root = insert(root, rootTmp);
    }

    private Point insert(Point r, Point node) {
        if (r == null) {
            ++cnt;
            return node;
        }
        // ignore duplicate point
        if (r.point.equals(node.point)) return r;
        // split by verticle line
        if (!r.isOdd) {
            node.isOdd = true;
            if (r.point.x() > node.point.x()) {
                // add to the left of the splitter
                r.lb = insert(r.lb, node);
            }
            else {
                // add to the right of the splitter
                r.rt = insert(r.rt, node);
            }
        }
        else {
            // split by horizontal line
            node.isOdd = false;
            if (r.point.y() > node.point.y()) {
                // add to the bottom of the splliter
                r.lb = insert(r.lb, node);
            }
            else {
                r.rt = insert(r.rt, node);
            }
        }
        return r;
    }

    // does the set contains point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Point r, Point2D node) {
        // not find
        if (r == null) return false;
        // find the point
        if (r.point.equals(node)) return true;
        if (!r.isOdd) {
            if (r.point.x() > node.x()) {
                return contains(r.lb, node);
            }
            else {
                return contains(r.rt, node);
            }
        }
        else {
            if (r.point.y() > node.y()) {
                return contains(r.lb, node);
            }
            else {
                return contains(r.rt, node);
            }
        }

    }

    // draw all points to standard draw
    public void draw() {
        draw(root, 0, 0, 1, 1);
    }

    // draw all the lines and points of current root
    private void draw(Point root, double xmin, double ymin, double xmax, double ymax) {
        if (root == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        root.point.draw();

        if (!root.isOdd) {
            // for verticle splitting line
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(root.point.x(), ymin, root.point.x(), ymax);
            draw(root.lb, xmin, ymin, root.point.x(), ymax);
            draw(root.rt, root.point.x(), ymin, xmax, ymax);
        }
        else {
            // for horizontal splitting line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(xmin, root.point.y(), xmax, root.point.y());
            draw(root.lb, xmin, ymin, xmax, root.point.y());
            draw(root.rt, xmin, root.point.y(), xmax, ymax);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();
        xmin = rect.xmin();
        xmax = rect.xmax();
        this.rect = rect;
        this.set = new ArrayList<>();
        range(root);
        return set;
    }

    private void range(Point node) {
        if (node == null) return;
        // search by verticle line
        if (!node.isOdd) {
            // if splitter hits the rectangle, search both sub-tree
            x = node.point.x();
            if (xmin <= x && x <= xmax) {
                // add the point if it is inside the rectangle
                y = node.point.y();
                if ((rect.ymin() <= y) && (y <= rect.ymax())) {
                    set.add(node.point);
                }
                range(node.lb);
                range(node.rt);
            }

            else if (x < xmin) {
                // rectangle is on the right of the splitter
                range(node.rt);
            }
            else {
                // rectangle is on the left of the splitter
                range(node.lb);
            }
        }
        else {
            y = node.point.y();
            // search by horizontal line
            ymin = rect.ymin();
            ymax = rect.ymax();
            if (ymin <= y && y <= ymax) {
                x = node.point.x();
                // if splitter hit the rectangle, search both sub-tree
                if (rect.xmin() <= x && x <= rect.xmax()) {
                    // add the point if it is inside the rectangle
                    set.add(node.point);
                }
                range(node.lb);
                range(node.rt);
            }
            else if (y < ymin) {
                // rectangle is on the bottom of the splitter
                range(node.rt);
            }
            else {
                // rectangle is on the top of the splitter
                range(node.lb);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        distance = 100;
        champion = null;
        if (root != null) {
            nearest(root, p);
        }
        return champion;
    }

    private void nearest(Point node, Point2D p) {
        if (node == null) return;
        // update distance and nearest neighbor
        if (node.point.distanceSquaredTo(p) < distance) {
            champion = node.point;
            distance = champion.distanceSquaredTo(p);
        }
        if (!node.isOdd) {
            // update rectangle of the sub-tree
            if (node.lb != null && node.lb.rectHV == null) {
                node.lb.rectHV = new RectHV(node.rectHV.xmin(), node.rectHV.ymin(), node.point.x(),
                                            node.rectHV.ymax());
            }
            if (node.rt != null && node.rt.rectHV == null) {
                node.rt.rectHV = new RectHV(node.point.x(), node.rectHV.ymin(), node.rectHV.xmax(),
                                            node.rectHV.ymax());
            }

            if (node.point.x() > p.x()) {
                // query point on the left side
                nearest(node.lb, p);

                if (node.rt != null && node.rt.rectHV.distanceSquaredTo(p) < distance) {
                    nearest(node.rt, p);
                }
            }
            else {
                // query point on the right side
                nearest(node.rt, p);
                if (node.lb != null && node.lb.rectHV.distanceSquaredTo(p) < distance) {
                    nearest(node.lb, p);
                }
            }
        }
        else {
            // update rectangle of the sub-tree
            if (node.lb != null && node.lb.rectHV == null) {
                node.lb.rectHV = new RectHV(node.rectHV.xmin(), node.rectHV.ymin(),
                                            node.rectHV.xmax(), node.point.y());
            }
            if (node.rt != null && node.rt.rectHV == null) {
                node.rt.rectHV = new RectHV(node.rectHV.xmin(), node.point.y(), node.rectHV.xmax(),
                                            node.rectHV.ymax());
            }
            if (node.point.y() > p.y()) {
                nearest(node.lb, p);
                if (node.rt != null && node.rt.rectHV.distanceSquaredTo(p) < distance) {
                    nearest(node.rt, p);
                }
            }
            else {
                nearest(node.rt, p);
                if (node.lb != null && node.lb.rectHV.distanceSquaredTo(p) < distance) {
                    nearest(node.lb, p);
                }
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        System.out.println(kdtree.contains(new Point2D(0.125, 0.1875)));

        System.out.println(new RectHV(0, 0, 0.7, 0.4).distanceSquaredTo(new Point2D(0.151, 0.408)));
        kdtree.draw();
        RectHV rect = new RectHV(0.01, 0.15, 0.34, 0.48);
        rect.draw();
        kdtree.range(rect);
    }

    private static class Point {
        boolean isOdd;
        Point2D point;
        // left or bottom, right or top
        Point lb, rt;
        RectHV rectHV;

        Point(Point2D point) {
            this.point = point;
            lb = null;
            rt = null;
            rectHV = null;
        }
    }
}
