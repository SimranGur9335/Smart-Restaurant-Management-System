package com.restaurant.payment;

/**
 * Interface representing a payment method service.
 * Demonstrates Abstraction (Interfaces).
 */
public interface PaymentService {
    /**
     * Abstract method to process payment of a specific amount.
     * @param amount The transaction value to process.
     */
    void pay(double amount);
}
