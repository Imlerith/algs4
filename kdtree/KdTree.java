import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size = 0;
    public boolean isEmpty() {
        return null == root;
    }
    public               int size() {
        return size;
    }

    public              void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        double[] rect = { 0, 0, 1, 1 };
        root = insert(root, p, 0, rect);
        size++;
    }
    private Node insert(final Node r, Point2D p, int i, double[] a)            // add the point to the set (if it is not already in the set)
    {
        if (r == null) return new Node(p, a);
        if (r.p.equals(p)) return r;

        if (i % 2 == 0) {
            if (p.x() - r.p.x() < 0) {
                a[2] = r.p.x();
                r.lb = insert(r.lb,  p, i+1, a);
            } else {
                a[0] = r.p.x();
                r.rt = insert(r.rt, p, i+1, a);
            }

        } else {
            if (p.y() - r.p.y() < 0) {
                a[3] = r.p.y();
                r.lb = insert(r.lb, p, i + 1, a);
            } else {
                a[1] = r.p.y();
                r.rt = insert(r.rt, p, i + 1, a);
            }
        }

        return r;
    }
    public           boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null)
            throw new IllegalArgumentException();
        return contains(root, p, 0);

    }
    private           boolean contains(final Node r, Point2D p, int i)            // does the set contain point p?
    {   if (null == r) return false;
        if (p.equals(r.p)) return true;
        if (i % 2 == 0) {
            if (p.x() - r.p.x() < 0) return contains(r.lb, p, i+1);
            else return contains(r.rt, p, i+1);
        } else {
            if (p.y() - r.p.y() < 0) return contains(r.lb, p, i+1);
            else return contains(r.rt, p, i+1);
        }
    }

    public              void draw()                         // draw all points to standard draw
    {
        draw(root, 0);
    }

    private void draw(final Node n, int i) {
        if (n == null) return;
        StdDraw.point(n.p.x(), n.p.y());
        if (i % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect[1], n.p.x(), n.rect[3]);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect[0], n.p.y(), n.rect[2], n.p.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        draw(n.lb, i+1);
        draw(n.rt, i+1);
    }
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null)
            throw new IllegalArgumentException();
        return range(rect, root);

    }
    private ArrayList<Point2D> range(RectHV rect, Node n)             // all points that are inside the rectangle (or on the boundary)
    {
        ArrayList<Point2D> res = new ArrayList<>();
        if (null == n) return res;
        if (rect.contains(n.p))
            res.add(n.p);
        if (n.lb != null && intersects(rect, n.lb.rect))
            res.addAll(range(rect, n.lb));
        if (n.rt != null && intersects(rect, n.rt.rect))
            res.addAll(range(rect, n.rt));
        return res;

    }
    private boolean intersects(RectHV rect, double[] r) {
        return rect.xmax() >= r[0] && rect.ymax() >= r[1] && r[2] >= rect.xmin() && r[3] >= rect.ymin();
    }

    public           Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null)
            return null;

        return search(p, root, Double.POSITIVE_INFINITY, null, 0).p;
    }

    private Node search(final Point2D p, final  Node cur, double m, final Node prev, int i)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        double min, cmp = i % 2 == 0 ? p.x() - cur.p.x() : p.y() - cur.p.y();
        Node tmp = prev;

        min = Math.min(cur.distanceTo(p), m);
        if (0 > Double.compare(min, m)) tmp = cur;

        Node first = cmp < 0 ? cur.lb : cur.rt;
        Node second = cmp < 0 ? cur.rt : cur.lb;

        if (first != null && first.rectDistanceTo(p) < m) {
            tmp = search(p, first, min, tmp, i+1);
            min = Math.min(tmp.distanceTo(p), min);
        }
        if (second != null && second.rectDistanceTo(p) < m)
            tmp = search(p, second, min, tmp, i+1);

        return tmp;
    }

    private static class Node {
        private final Point2D p;      // the point
        private final double[] rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private Node(final Point2D e, final double[] r) {
            p = e;
            rect = r;
        }
        private double rectDistanceTo(Point2D e) {
            double dx = 0.0D;
            double dy = 0.0D;
            if (e.x() < rect[0]) {
                dx = e.x() - rect[0];
            } else if (e.x() > rect[2]) {
                dx = e.x() - rect[2];
            }

            if (e.y() < rect[1]) {
                dy = e.y() - rect[1];
            } else if (e.y() > rect[3]) {
                dy = e.y() - rect[2];
            }

            return dx * dx + dy * dy;
        }
        private double distanceTo(Point2D e) {
            return p.distanceSquaredTo(e);
        }
    }
}
