package com.restaurant.utility;

import com.restaurant.model.FoodItem;

/**
 * Utility class for sorting food items by price.
 * Demonstrates Bubble Sort, Selection Sort, and Insertion Sort.
 */
public class SortUtility {

    /**
     * Bubble Sort to sort items by price.
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     * 
     * @param items The FoodItem array to sort.
     * @param size The active number of elements in the items array.
     * @param ascending True for low-to-high, false for high-to-low.
     */
    public static void bubbleSortByPrice(FoodItem[] items, int size, boolean ascending) {
        if (items == null || size <= 1) return;

        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - i - 1; j++) {
                boolean shouldSwap = false;
                if (ascending) {
                    shouldSwap = items[j].getPrice() > items[j + 1].getPrice();
                } else {
                    shouldSwap = items[j].getPrice() < items[j + 1].getPrice();
                }

                if (shouldSwap) {
                    FoodItem temp = items[j];
                    items[j] = items[j + 1];
                    items[j + 1] = temp;
                    swapped = true;
                }
            }
            // Optimization: If no elements were swapped in the inner loop, array is sorted.
            if (!swapped) break;
        }
    }

    /**
     * Selection Sort to sort items by price.
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     * 
     * @param items The FoodItem array to sort.
     * @param size The active number of elements in the items array.
     * @param ascending True for low-to-high, false for high-to-low.
     */
    public static void selectionSortByPrice(FoodItem[] items, int size, boolean ascending) {
        if (items == null || size <= 1) return;

        for (int i = 0; i < size - 1; i++) {
            int extremeIndex = i;
            for (int j = i + 1; j < size; j++) {
                boolean isExtreme = false;
                if (ascending) {
                    isExtreme = items[j].getPrice() < items[extremeIndex].getPrice();
                } else {
                    isExtreme = items[j].getPrice() > items[extremeIndex].getPrice();
                }

                if (isExtreme) {
                    extremeIndex = j;
                }
            }
            // Swap elements
            FoodItem temp = items[extremeIndex];
            items[extremeIndex] = items[i];
            items[i] = temp;
        }
    }

    /**
     * Insertion Sort to sort items by price.
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     * 
     * @param items The FoodItem array to sort.
     * @param size The active number of elements in the items array.
     * @param ascending True for low-to-high, false for high-to-low.
     */
    public static void insertionSortByPrice(FoodItem[] items, int size, boolean ascending) {
        if (items == null || size <= 1) return;

        for (int i = 1; i < size; i++) {
            FoodItem key = items[i];
            int j = i - 1;

            while (j >= 0) {
                boolean shouldShift = false;
                if (ascending) {
                    shouldShift = items[j].getPrice() > key.getPrice();
                } else {
                    shouldShift = items[j].getPrice() < key.getPrice();
                }

                if (shouldShift) {
                    items[j + 1] = items[j];
                    j--;
                } else {
                    break;
                }
            }
            items[j + 1] = key;
        }
    }
}
