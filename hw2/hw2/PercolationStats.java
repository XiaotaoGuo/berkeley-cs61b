package hw2;

import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] percolationThresholdList;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T mush be positive!");
        }
        percolationThresholdList = new double[T];
        List<Integer> blockedSite = new ArrayList<>();

        for (int i = 0; i < N * N; i++) {
            blockedSite.add(i);
        }

        for (int i = 0; i < T; i++) {
            Percolation currPer = pf.make(N);
            int count = 0;
            while(!currPer.percolates()) {
                int index = StdRandom.uniform(N * N);
                int row = index / N;
                int col = index % N;
                if (!currPer.isOpen(row, col)) {
                    currPer.open(row, col);
                    count++;
                }
            }
            percolationThresholdList[i] = (double) count / (N * N);
        }
    }

    public double mean() {
        return StdStats.mean(percolationThresholdList);
    }

    public double stddev() {
        return StdStats.stddev(percolationThresholdList);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev()
                / Math.pow(percolationThresholdList.length, 0.5);
    }

    public double confidenceHigh() {
        return mean() + 1.96 * stddev()
                / Math.pow(percolationThresholdList.length, 0.5);
    }




}
