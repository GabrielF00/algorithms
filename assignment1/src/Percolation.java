/**
 * Created by Gabriel Fishman on 2/13/15.
 */
public class Percolation {

    private final int mN;
    private boolean[][] mGrid;
    private WeightedQuickUnionUF mUnionFind;
    private int mTopId = 0; // one point connected to every open site in the top row
    private int mBottomId;  // one point connected to every open site in the bottom row

    /**
     * create an N-by-N grid, with all sites blocked
     * @param N
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Cannot initialize a grid of size <= 0. N = " + N);
        }
        mN = N;
        mGrid = new boolean[N+1][N+1];  // by convention, grid coordinates go from (1,1) to (N,N)
        mUnionFind = new WeightedQuickUnionUF(N * N + 2);
        mBottomId = N * N + 1;
    }

    /**
     * open site (row i, column j) if it is not open already
     * @param i row
     * @param j col
     */
    public void open(int i, int j) {
        if (i < 1 || i > mN) {
            throw new IndexOutOfBoundsException("Array index i out of bounds. i = " + i);
        }
        if (j < 1 || j > mN) {
            throw new IndexOutOfBoundsException("Array index j out of bounds. j = " + j);
        }
        mGrid[i][j] = true;

        // Connect any sites in the top row to the top id and the bottom row to the bottom id
        if (i == 1) {
            mUnionFind.union(get1DIndex(i, j), mTopId);
        }
        if (i == mN) {
            mUnionFind.union(get1DIndex(i, j), mBottomId);
        }

        connectIfOpen(i, j, i - 1, j); // above
        connectIfOpen(i, j, i + 1, j); // below
        connectIfOpen(i, j, i, j - 1); // left
        connectIfOpen(i, j, i, j + 1); // right
    }

    /**
     * If site q is in the grid and open, connect it to site p
     * @param pI
     * @param pJ
     * @param qI
     * @param qJ
     */
    private void connectIfOpen(int pI, int pJ, int qI, int qJ) {
        if (qI <= 0 || qJ <= 0 || qI > mN || qJ > mN) {
            return;
        }

        if (isOpen(qI, qJ)) {
            int pId = get1DIndex(pI, pJ);
            int qId = get1DIndex(qI, qJ);
            mUnionFind.union(pId, qId);
        }
    }
    /**
     * is site (row i, column j) open?
     * @param i row
     * @param j col
     * @return true if open
     */
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > mN) {
            throw new IndexOutOfBoundsException("Array index i out of bounds. i = " + i);
        }
        if (j < 1 || j > mN) {
            throw new IndexOutOfBoundsException("Array index j out of bounds. j = " + j);
        }
        return mGrid[i][j];
    }

    /**
     * is site (row i, column j) full?
     * A full site is an open site that can be connected to an open site in the top row via a chain of neighboring
     * (left, right, up, down) open sites.
     * @param i
     * @param j
     * @return
     */
    public boolean isFull(int i, int j) {
        if (i < 1 || i > mN) {
            throw new IndexOutOfBoundsException("Array index i out of bounds. i = " + i);
        }
        if (j < 1 || j > mN) {
            throw new IndexOutOfBoundsException("Array index j out of bounds. j = " + j);
        }
        return mUnionFind.connected(mTopId, get1DIndex(i, j));
    }

    /**
     * Return the index in a 1-dimensional array of grid[i][j]
     * @param i
     * @param j
     * @return
     */
    private int get1DIndex(int i, int j) {
        return (mN * (i - 1)) + j;
    }

    /**
     * does the system percolate?
     * the system percolates if there is a full site in the bottom row
     * @return
     */
    public boolean percolates() {
        return mUnionFind.connected(mTopId, mBottomId);
    }

    /**
     * For debugging
     */
    private void printGrid() {
        for (int i = 1; i <= mN; i++) {
            for (int j = 1; j <= mN; j++) {
                if (isFull(i, j)) {
                    System.out.print("F ");
                } else if (mGrid[i][j]) {
                    System.out.print("O ");
                } else {
                    System.out.print("B ");
                }
            }
            System.out.println();
        }
    }
}
