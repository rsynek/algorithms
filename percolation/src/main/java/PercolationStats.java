import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.lang.Math;

public class PercolationStats {

    private static final double CONFIDENCE_CONST = 1.96;

    private final int size;
    private final int trials;

    private int [] results;

    /**
     * perform trials independent experiments on an n-by-n grid
     * */
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw  new IllegalArgumentException("Percolation grid size must be a positive integer.");
        }

        if (trials <=0 ) {
            throw  new IllegalArgumentException("Number of trials must be a positive integer.");
        }

        this.size = n;
        this.trials = trials;
        this.results = new int[trials];

        performExperiments();
    }

    private void performExperiments() {
        for (int i = 0; i < trials; i++) {
            results[i] = performSingleExperiment();
        }
    }

    private int performSingleExperiment() {
        Percolation percolation = new Percolation(size);

        while (!percolation.percolates()) {
            openRandomSite(percolation);
        }

        return percolation.numberOfOpenSites();
    }

    private void openRandomSite(Percolation percolation) {
        int row = StdRandom.uniform(size) + 1;
        int col = StdRandom.uniform(size) + 1;

        percolation.open(row, col);
    }

    /**
     * sample mean of percolation threshold
     * */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /**
     * low  endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - confidenceComponent();
    }

    /**
     * high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + confidenceComponent();
    }

    private double confidenceComponent() {
        return CONFIDENCE_CONST * stddev()/Math.sqrt(trials);
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(10,30);
        StdOut.println("Mean: " + ps.mean());
        StdOut.println("StdDev: " + ps.stddev());
        StdOut.println("ConfLow: " + ps.confidenceLo());
        StdOut.println("ConfHi: " + ps.confidenceHi());
    }
}