import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture p;
    private double[][] energies;
    public SeamCarver(Picture picture) {
        if (null == picture) throw new IllegalArgumentException();
        p = new Picture(picture);
        energies = new double[height()][width()];
        for (int y = 0; y < height(); y++)
            for (int x = 0; x < width(); x++) {
                double e = calc(x, y);
                energies[y][x] = e;
            }
    }

    public Picture picture() {
        return new Picture(p);
    }

    public int width() {
        return p.width();
    }

    public int height() {
        return p.height();
    }

    public double energy(int x, int y) {
        validate(x, y);
        return energies[y][x];
    }

    private int xyTo1D(int x, int y, boolean flag) {
        return flag ? height() * y + x : width() * y + x;
    }

    private void reinit(double[] distTo, int[] edgeTo, boolean flag) {
        int height = flag ? width() : height();
        int width = flag ? height() : width();
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int v = xyTo1D(x, y, flag);
                double e = flag ? energies[x][y] : energies[y][x];
                edgeTo[v] = -1;
                distTo[v] = y == 0 ? e : Double.POSITIVE_INFINITY;
            }
    }

    private void relax(double[] distTo, int[] edgeTo, boolean flag) {
        int height = flag ? width() : height();
        int width = flag ? height() : width();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int v = xyTo1D(c, r, flag), y = r+1;
                int[] tmp = {c, c-1, c+1};
                for (int x: tmp) {
                    if (!isValid(x, y, flag)) continue;
                    int w = xyTo1D(x, y, flag);
                    double e = flag ? energies[x][y] : energies[y][x];
                    if (distTo[w] > distTo[v] + e) {
                        distTo[w] = distTo[v] + e;
                        edgeTo[w] = v;
                    }
                }
            }
        }
    }

    private  double calc(int x, int y) {
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
            return 1000;
        int[] x1 = unpack(p.getRGB(x-1, y)), x2 = unpack(p.getRGB(x+1, y));
        int[] y1 = unpack(p.getRGB(x, y-1)), y2 = unpack(p.getRGB(x, y+1));

        return Math.sqrt(gradient(x1, x2) + gradient(y1, y2));
    }

    private static int[] unpack(int c) {
        int[] rgb = new int[3];
        rgb[0] = (c >> 16) & 0xFF;
        rgb[1] = (c >> 8) & 0xFF;
        rgb[2] = c & 0xFF;
        return rgb;
    }

    private static double gradient(int[] a, int[] b) {
        int dR, dG, dB;
        dR = Math.abs(a[0] - b[0]);
        dG = Math.abs(a[1] - b[1]);
        dB = Math.abs(a[2] - b[2]);
        return dR * dR + dG * dG + dB * dB;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }
    private boolean isValid(int x, int y, boolean flag) {
        return flag ? isValid(y, x) : isValid(x, y);
    }

    private void validate(int x, int y) {
        if (!isValid(x, y))
            throw new IllegalArgumentException();
    }

    private int[] seam(boolean flag) {
        int size = width() * height();
        double[] distTo = new double[size];
        int[] edgeTo = new int[size];
        reinit(distTo, edgeTo, flag);
        relax(distTo, edgeTo, flag);
        int v = shortestEnd(distTo, flag);

        return shortestPath(edgeTo, v, flag);
    }

    private int[] shortestPath(int[] edgeTo, int v, boolean flag) {
        int height = flag ? width() : height();
        int width = flag ? height() : width();
        int[] path = new int[height];
        int n = height - 1;
        for (int i = v; i >= 0; i = edgeTo[i]) {
            path[n] = i % width;
            n--;
        }
        return path;
    }
    private int shortestEnd(double[] distTo, boolean flag) {
        int height = flag ? width() : height();
        int width = flag ? height() : width();
        double min = Double.POSITIVE_INFINITY;
        int y = height - 1;
        int v = -1;
        for (int x = 0; x < width; x++) {
            int w = xyTo1D(x, y, flag);
            if (min > distTo[w]) {
                min = distTo[w];
                v = w;
            }
        }
        return v;
    }

    public int[] findHorizontalSeam() {
        return seam(true);
    }

    public int[] findVerticalSeam() {
        return seam(false);
    }

    public void removeHorizontalSeam(int[] seam) {
        Picture tmp = new Picture(width(), height()-1);

        for (int y = 0; y < height(); y++) {

            for (int x = 0; x < width(); x++) {
                int z = seam[x];
                if (y >= z - 1) {
                    energies[y][x] = calc(x, y);
                }
                if (y == z) continue;
                tmp.setRGB(x, y > z ? y - 1 : y, p.getRGB(x, y));
            }
        }
        p = tmp;
    }

    public void removeVerticalSeam(int[] seam) {
        Picture tmp = new Picture(width()-1, height());

        for (int y = 0; y < height(); y++) {
            int z = seam[y];

            for (int x = 0; x < width(); x++) {
                if (x >= z-1) {
                    energies[y][x] = calc(x, y);
                }
                if (x == z) continue;
                tmp.setRGB(x > z ? x-1 : x, y, p.getRGB(x, y));
            }
        }
        p = tmp;
    }
}