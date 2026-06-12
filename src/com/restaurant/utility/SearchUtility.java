package com.restaurant.utility;

import com.restaurant.model.FoodItem;

/**
 * Utility class for searching food items in the menu.
 * Demonstrates Linear Search, Binary Search, and Algorithm Preconditions.
 */
public class SearchUtility {

    /**
     * Linear Search to find a FoodItem by name (partial and case-insensitive matching).
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * 
     * @param items The array of FoodItem elements to search in.
     * @param size The active number of elements in the items array.
     * @param targetName The name of the food item to search.
     * @return The matching FoodItem, or null if not found.
     */
    public static FoodItem linearSearchByName(FoodItem[] items, int size, String targetName) {
        if (items == null || size == 0 || targetName == null) {
            return null;
        }
        
        String query = targetName.toLowerCase().trim();
        for (int i = 0; i < size; i++) {
            if (items[i] != null && items[i].getItemName().toLowerCase().contains(query)) {
                return items[i]; // Found item
            }
        }
        return null; // Item not found
    }

    /**
     * Binary Search to find a FoodItem by ID.
     * Time Complexity: O(log N) [search phase] + O(N^2) [sorting phase]
     * Space Complexity: O(N) [temporary array copy]
     * 
     * IMPORTANT: Binary Search requires a sorted array. We clone the menu array
     * and sort it by ID using Insertion Sort before performing the Binary Search.
     * 
     * @param items The array of FoodItem elements to search in.
     * @param size The active number of elements in the items array.
     * @param targetId The ID of the food item to search.
     * @return The matching FoodItem, or null if not found.
     */
    public static FoodItem binarySearchById(FoodItem[] items, int size, int targetId) {
        if (items == null || size == 0) {
            return null;
        }

        // 1. Create a copy of the active menu items to preserve the original menu order
        FoodItem[] copy = new FoodItem[size];
        System.arraycopy(items, 0, copy, 0, size);

        // 2. Sort the copy by itemId (using Insertion Sort by ID)
        for (int i = 1; i < size; i++) {
            FoodItem key = copy[i];
            int j = i - 1;
            while (j >= 0 && copy[j].getItemId() > key.getItemId()) {
                copy[j + 1] = copy[j];
                j--;
            }
            copy[j + 1] = key;
        }

        // 3. Perform standard Binary Search on the sorted copy
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midId = copy[mid].getItemId();

            if (midId == targetId) {
                return copy[mid]; // Found item
            } else if (midId < targetId) {
                low = mid + 1; // Search right half
            } else {
                high = mid - 1; // Search left half
            }
        }

        return null; // Item not found
    }
}
