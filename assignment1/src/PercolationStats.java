/**
 * Created by Gabriel Fishman on 2/13/15.
 */
public class PercolationStats {

    private final int mN;
    private final int mT;
    private double[] mResults;

    /**
     * @param N size of grid (N * N)
     * @param T number of experiments
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T must be >0. N = " + N + " T = " + T);
        }
        mN = N;
        mT = T;
        mResults = new double[T];
        for (int i = 0; i < T; i++) {
            mResults[i] = performExperiment();
        }
    }

    /**
     * randomly select a site, if that site is blocked, open it and increment the counter. Stop when percolates() == true
     * @return number of sites opened before percolation
     */
    private double performExperiment() {
        Percolation percolation = new Percolation(mN);
        int count = 0;
        while (!percolation.percolates()) {
            int randI = StdRandom.uniform(1, mN + 1);
            int randJ = StdRandom.uniform(1, mN + 1);
            if (!percolation.isOpen(randI, randJ)) {
                percolation.open(randI, randJ);
                count++;
            }
        }
        return count / ((double) mN * (double) mN);
    }

    /**
     * sample mean of percolation threshold
     * @return
     */
    public double mean() {
        double sum = 0;
        for (int i = 0; i < mT; i++) {
            sum = sum + mResults[i];
        }
        return sum / mT;
    }

    /**
     * sample standard deviation of percolation threshold
     * @return
     */
    public double stddev() {
        double sum = 0;
        for (int i = 0; i < mT; i++) {
            sum = sum + Math.pow((mResults[i] - mean()), 2);
        }
        double sigma2 = sum / (mT - 1);
        return Math.sqrt(sigma2);
    }

    /**
     * low  endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(mT);
    }

    /**
     * high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(mT);
    }

    /**
     * test client (described below)
     * @param args
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        System.out.println("mean =                  " + stats.mean());
        System.out.println("stddev =                " + stats.stddev());
        System.out.println("95% confidence interval " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
