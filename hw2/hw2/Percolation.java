package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import org.junit.Test;

import static org.junit.Assert.*;

public class Percolation {

    private WeightedQuickUnionUF sites;
    private WeightedQuickUnionUF sitesToTop;
    private int top;
    private int bottom;
    private int length;
    private boolean[][] openSites;
    private int openCount;
    private boolean isPercolate;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("N should be positive");
        }
        isPercolate = false;
        length = N;
        top = N * N;
        bottom = N * N + 1;
        sites = new WeightedQuickUnionUF(N * N + 2);
        sitesToTop = new WeightedQuickUnionUF(N * N + 1);
        openSites = new boolean[N][N];
        openCount = 0;
        for (int i = 0; i < N; i++) {
            sitesToTop.union(top, posToIndex(0, i));
            for (int j = 0; j < N; j++) {
                openSites[i][j] = false;
            }
        }
    }

    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= length || col >= length) {
            throw new java.lang.IndexOutOfBoundsException("Index out of bound!");
        }
        if (!openSites[row][col]) {
            openSites[row][col] = true;
            openCount++;

            if (row == 0) {
                sites.union(top, posToIndex(row, col));
            }

            if (row > 0) {
                unionHelper(row, col, row - 1, col);
            }
            if (row < length - 1) {
                unionHelper(row, col, row + 1, col);
            }
            if (col > 0) {
                unionHelper(row, col, row, col - 1);
            }
            if (col < length - 1) {
                unionHelper(row, col, row, col + 1);
            }
            /* in the last row */
            if (row == length - 1) {
                sites.union(bottom, posToIndex(row, col));
            }
            if (row == 0) {
                sites.union(top, posToIndex(row, col));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        return openSites[row][col];
    }

    public boolean isFull(int row, int col) {
        return openSites[row][col] && sitesToTop.connected(top, posToIndex(row, col));
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        return sites.connected(top, bottom);
    }

    @Test
    public static void main(String[] args) {


        Percolation per = new Percolation(6);
        per.open(0, 5);
        assertEquals(true, per.isOpen(0, 5));
        assertEquals(true, per.isFull(0, 5));
        assertEquals(1, per.numberOfOpenSites());
        assertEquals(false, per.percolates());


    }

    private void unionHelper(int row1, int col1, int row2, int col2) {
        if (openSites[row2][col2]) {
            sites.union(posToIndex(row1, col1), posToIndex(row2, col2));
            sitesToTop.union(posToIndex(row1, col1), posToIndex(row2, col2));
        }
    }

    private int posToIndex(int row, int col) {
        return row * length + col;
    }
}
