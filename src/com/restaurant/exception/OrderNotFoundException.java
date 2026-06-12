package com.restaurant.exception;

/**
 * Custom Exception thrown when an order record is not found in the database.
 * Demonstrates Exception Handling and Custom Exceptions.
 */
public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
