package com.restaurant.utility;

/**
 * Utility class to analyze sales data.
 * Demonstrates the Sliding Window Algorithm.
 */
public class SalesAnalyzer {

    /**
     * Finds the maximum revenue generated in consecutive 'k' days using the Sliding Window Algorithm.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * 
     * @param sales Array storing daily sales revenue.
     * @param size Active number of days recorded.
     * @param k Size of the sliding window (number of consecutive days).
     * @return The maximum revenue in consecutive k days.
     * @throws IllegalArgumentException if invalid window size or arguments are passed.
     */
    public static double findMaxRevenueConsecutiveDays(double[] sales, int size, int k) {
        if (sales == null || size == 0) {
            throw new IllegalArgumentException("Sales database is empty.");
        }
        if (k <= 0) {
            throw new IllegalArgumentException("Number of consecutive days must be positive.");
        }
        if (k > size) {
            throw new IllegalArgumentException("Consecutive days (" + k + ") cannot be greater than the total recorded days (" + size + ").");
        }

        System.out.printf("[Sliding Window] Total days recorded: %d | Window size (K): %d days%n", size, k);

        // 1. Calculate sum of the first window
        double windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += sales[i];
        }

        double maxRevenue = windowSum;
        int maxStartIndex = 0;

        System.out.printf("  Initial Window (Days 1 to %d) -> Revenue: ₹%.2f%n", k, windowSum);

        // 2. Slide the window from index k to size - 1
        for (int i = k; i < size; i++) {
            // Slide window: add current element, subtract first element of previous window
            double prevSum = windowSum;
            windowSum = windowSum + sales[i] - sales[i - k];
            
            System.out.printf("  Slide to (Days %d to %d) -> Add: ₹%.2f | Remove: ₹%.2f | New Revenue: ₹%.2f%n", 
                (i - k + 2), (i + 1), sales[i], sales[i - k], windowSum);

            if (windowSum > maxRevenue) {
                maxRevenue = windowSum;
                maxStartIndex = i - k + 1;
            }
        }

        System.out.printf("Max Revenue found starting on Day %d ending on Day %d: ₹%.2f%n", 
            (maxStartIndex + 1), (maxStartIndex + k), maxRevenue);

        return maxRevenue;
    }
}
