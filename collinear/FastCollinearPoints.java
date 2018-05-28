import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> found = new ArrayList<>();
    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null)
            throw new IllegalArgumentException();
        checkDuplicatedEntries(points);

        Point[] orig = points.clone();
        Arrays.sort(orig);
        Point[] tmp = orig.clone();

        int len = orig.length;
        for (int i = 0; i < len - 3; i++) {
            Point point = orig[i];
            Arrays.sort(tmp, i+1, len, point.slopeOrder());

            for (int j = i+1, k = j+2; k < len;) {
                Point first = tmp[j];
                double slope = point.slopeTo(first);
                Point last = tmp[k];
                if (Double.compare(slope, point.slopeTo(last)) != 0) {
                    j++;
                    k++;
                    continue;
                }
                while (k < len && Double.compare(slope, point.slopeTo(tmp[k])) == 0) {
                    last = tmp[k++];
                }
                j = k;
                k = j + 2;
                found.add(new LineSegment(point, last));
            }
        }

    }
    public int numberOfSegments()        // the number of line segments
    {
        return found.size();
    }
    public LineSegment[] segments()                // the line segments
    {
        return found.toArray(new LineSegment[found.size()]);
    }

    private void checkDuplicatedEntries(Point[] points) {
        if (points[0] == null)
            throw new IllegalArgumentException();
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null || points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Duplicated entries in given points.");
                }
            }
        }
    }
}