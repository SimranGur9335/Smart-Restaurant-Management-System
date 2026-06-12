package com.restaurant.exception;

/**
 * Custom Exception thrown when a food item is not found in the restaurant menu.
 * Demonstrates Exception Handling and Custom Exceptions.
 */
public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
