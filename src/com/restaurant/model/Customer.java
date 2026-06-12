package com.restaurant.model;

/**
 * Class representing a Customer.
 * Demonstrates Encapsulation.
 */
public class Customer {
    private int customerId;
    private String name;
    private String contact;

    // Constructor
    public Customer(int customerId, String name, String contact) {
        this.customerId = customerId;
        this.name = name;
        this.contact = contact;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Display details of the customer.
     */
    public void displayCustomer() {
        System.out.printf("Customer ID: %-4d | Name: %-20s | Contact: %-15s%n", 
            customerId, name, contact);
    }
}
