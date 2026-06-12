package com.restaurant.exception;

/**
 * Custom Exception thrown when a customer record is not found in the database.
 * Demonstrates Exception Handling and Custom Exceptions.
 */
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
