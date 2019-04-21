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

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int number = 0;
    private final int size;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final WeightedQuickUnionUF weightedQuickUnionUFAff;
    public Percolation(int n) {                                     // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("the n-by-n grid must greater than 0");
        }
        size = n;
        grid = new boolean[n][n];                               // 0 represents close, 1 represents open
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2); // virtual top = 0, 1 to n*n, virtual bottom = n*n+1
        weightedQuickUnionUFAff = new WeightedQuickUnionUF(n * n + 1); // virtual top = 0 to n*n
    }
    public void open(int row, int col) {                            // open site (row, col) if it is not open already.start from 1, 1
        if (rangeFault(row, col)) {
            throw new java.lang.IllegalArgumentException("the col, row must greater than 0, less or equal than n");
        }
        row -= 1;
        col -= 1;
        if (size == 1) {
            grid[row][col] = true;
            weightedQuickUnionUF.union(0, 1);
            weightedQuickUnionUF.union(1, 2);
            weightedQuickUnionUFAff.union(0, 1);
            ++number;
            return;
        }
        if (!grid[row][col]) {
            grid[row][col] = true;
            if (row == 0) {
                weightedQuickUnionUF.union(0, row * size + col + 1);
                weightedQuickUnionUFAff.union(0, row * size + col + 1);
                if (grid[row + 1][col]) {
                    weightedQuickUnionUF.union((row + 1) * size + col + 1, row * size + col + 1);
                    weightedQuickUnionUFAff.union((row + 1) * size + col + 1, row * size + col + 1);
                }
            } else if (row == size - 1) {
                weightedQuickUnionUF.union(size * size + 1, row * size + col + 1);
                if (grid[row - 1][col]) {
                    weightedQuickUnionUF.union((row - 1) * size + col + 1, row * size + col + 1);
                    weightedQuickUnionUFAff.union((row - 1) * size + col + 1, row * size + col + 1);
                }
            } else {
                if (grid[row - 1][col]) {
                    weightedQuickUnionUF.union((row - 1) * size + col + 1, row * size + col + 1);
                    weightedQuickUnionUFAff.union((row - 1) * size + col + 1, row * size + col + 1);
                }
                if (grid[row + 1][col]) {
                    weightedQuickUnionUF.union((row + 1) * size + col + 1, row * size + col + 1);
                    weightedQuickUnionUFAff.union((row + 1) * size + col + 1, row * size + col + 1);
                }
            }
            if (col != 0) {
                if (grid[row][col - 1]) {
                    weightedQuickUnionUF.union(row * size + col, row * size + col + 1);
                    weightedQuickUnionUFAff.union(row * size + col, row * size + col + 1);
                }
            }
            if (col != size - 1) {
                if (grid[row][col + 1]) {
                    weightedQuickUnionUF.union(row * size + col + 2, row * size + col  + 1);
                    weightedQuickUnionUFAff.union(row * size + col + 2, row * size + col  + 1);
                }
            }
            ++number;
        }
    }
    public boolean isOpen(int row, int col) {                       // is site (row, col) open?
        if (rangeFault(row, col)) {
            throw new java.lang.IllegalArgumentException("the col, row must greater than 0, less or equal than n");
        }
        return grid[row - 1][col - 1];
    }
    public boolean isFull(int row, int col) {                       // is site (row, col) full?
        if (rangeFault(row, col)) {
            throw new java.lang.IllegalArgumentException("the col, row must greater than 0, less or equal than n");
        }
        return weightedQuickUnionUFAff.connected(0, (row - 1) * size + col);
    }
    public int numberOfOpenSites() {                                // number of open sites
        return number;
    }
    public boolean percolates() {                                   // does the system percolate?
        return weightedQuickUnionUF.connected(0, size * size + 1);
    }
    private boolean rangeFault(int row, int col) {
        if (row <= 0 || col <= 0 || row > size || col > size) {
            return true;
        } else {
            return false;
        }
    }
    public static void main(String[] args) {                        // test client (optional)

    }
}