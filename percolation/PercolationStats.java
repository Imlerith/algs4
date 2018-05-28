import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[] res;
    public PercolationStats(int n, int trials) {
        if (n < 0 || trials < 0) {
            throw new IllegalArgumentException();
        }
        res = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while(!perc.percolates()){
                perc.open(StdRandom.uniform(n)+1, StdRandom.uniform(n)+1);
            }
            res[i] = (double)perc.numberOfOpenSites() / (n*n);
        }
    }
    public double mean() {
        return StdStats.mean(res);
    }
    public double stddev()  {
        return StdStats.stddev(res);
    }
    public double confidenceLo() {
        return mean()-((1.96*stddev())/Math.sqrt(res.length));
    }
    public double confidenceHi() {
        return mean()+((1.96*stddev())/Math.sqrt(res.length));
    }

    public static void main(String[] args) {
        int gridSize = 10;
        int trialCount = 10;
        if (args.length >= 2) {
            gridSize = Integer.parseInt(args[0]);
            trialCount = Integer.parseInt(args[1]);
        }
        Out out  = new Out();
        PercolationStats ps = new PercolationStats(gridSize, trialCount);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        out.println("mean                    = " + ps.mean());
        out.println("stddev                  = " + ps.stddev());
        out.println("95% confidence interval = " + confidence);

    }
}