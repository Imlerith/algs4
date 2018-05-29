import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private final SET<Point2D> set;
    public PointSET()                               // construct an empty set of points
    {
        set = new SET<>();
    }
    public boolean isEmpty() {
        return set.isEmpty();
    }
    public               int size()                         // number of points in the set
    {
        return set.size();
    }
    public              void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null)
            throw new IllegalArgumentException();
        set.add(p);
    }
    public           boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null)
            throw new IllegalArgumentException();
        return set.contains(p);
    }
    public              void draw()                         // draw all points to standard draw
    {
        for (Point2D p: set)
            p.draw();
    }
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null)
            throw new IllegalArgumentException();
        Stack<Point2D> stack = new Stack<>();
        for (Point2D p: set)
            if (rect.contains(p))
                stack.push(p);
        return stack;
    }
    public           Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null)
            throw new IllegalArgumentException();
        if (set.isEmpty()) return null;
        Point2D m = null;
        double min = Double.POSITIVE_INFINITY;
        for (Point2D item: set)
            if (min > p.distanceSquaredTo(item)) {
                min = p.distanceSquaredTo(item);
                m = item;
            }
        return m;

    }
}