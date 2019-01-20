/******************************************************************************
 *  Name:    MaGuilong
 *
 *  Description:
 *
 *  Written:       12/01/2019
 *  Last updated:  12/01/2019
 *
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final double confidenceHigh;
    private final double confidenceLow;
    public PercolationStats(int n, int trials) {                        // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        double[] results = new double[trials];
        for (int i = 0; i < trials; ++i) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row, col;
                int index = StdRandom.uniform(n * n) + 1;
                row = (index - 1) / n + 1;
                col = index - (row - 1) * n;
                percolation.open(row, col);
            }
            results[i] = percolation.numberOfOpenSites() / Double.valueOf(n) / Double.valueOf(n);
        }
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        double sqrtT = Math.sqrt(trials);
        confidenceLow = mean - CONFIDENCE_95 * stddev / sqrtT;
        confidenceHigh = mean + CONFIDENCE_95 * stddev / sqrtT;
    }
    public double mean() {                                              // sample mean of percolation threshold
        return mean;
    }
    public double stddev() {                                            // sample standard deviation of percolation threshold
        return stddev;
    }
    public double confidenceLo() {                                      // low  endpoint of 95% confidence interval
        return confidenceLow;
    }
    public double confidenceHi() {                                      // high endpoint of 95% confidence interval
        return confidenceHigh;
    }
    public static void main(String[] args) {                            // test client (described below)
        int n = Integer.parseInt(args[0]);                              // n-by-n grid
        int t = Integer.parseInt(args[1]);                              // performs t independent computational experiments
        PercolationStats percolationStats = new PercolationStats(n, t);
        System.out.println("mean\t=" + percolationStats.mean());
        System.out.println("stddev\t=" + percolationStats.stddev());
        System.out.println("95% confidence interval\t=[" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}
