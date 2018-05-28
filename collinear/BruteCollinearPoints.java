import java.util.ArrayList;
import java.util.Arrays;
public class BruteCollinearPoints {
    private ArrayList<LineSegment> found = new ArrayList<>();
    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null)
            throw new IllegalArgumentException();
        checkDuplicatedEntries(points);
        Point[] tmp = points.clone();
        Arrays.sort(tmp);
        int len = tmp.length;
        for (int p = 0; p < len - 3; p++) {
            for (int q = p + 1; q < len - 2; q++) {
                double pq = tmp[p].slopeTo(tmp[q]);
                for (int r = q + 1; r < len - 1; r++) {
                    if (Double.compare(pq, tmp[p].slopeTo(tmp[r])) == 0) {
                        for (int s = r + 1; s < len; s++) {
                            if (Double.compare(pq, tmp[p].slopeTo(tmp[s])) == 0) {
                                found.add(new LineSegment(tmp[p], tmp[s]));
                            }
                        }
                    }
                }
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