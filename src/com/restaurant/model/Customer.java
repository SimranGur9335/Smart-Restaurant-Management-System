package com.restaurant.model;

/**
 * Class representing a Customer.
 * Handles contact details, visits frequency, loyalty reward points, and membership status levels.
 */
public class Customer {
    private int customerId;
    private String name;
    private String contact;
    private String email;
    private int visits;
    private int rewardPoints;
    private String membershipLevel; // Bronze, Silver, Gold, Platinum

    // Constructor
    public Customer(int customerId, String name, String contact) {
        this(customerId, name, contact, "", 0, 0);
    }

    public Customer(int customerId, String name, String contact, String email, int visits, int rewardPoints) {
        this.customerId = customerId;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.visits = visits;
        this.rewardPoints = rewardPoints;
        updateMembershipLevel();
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

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public void incrementVisits() {
        this.visits++;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
        updateMembershipLevel();
    }

    public void addRewardPoints(int points) {
        this.rewardPoints += points;
        updateMembershipLevel();
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void updateMembershipLevel() {
        if (rewardPoints >= 1000) {
            membershipLevel = "Platinum";
        } else if (rewardPoints >= 500) {
            membershipLevel = "Gold";
        } else if (rewardPoints >= 100) {
            membershipLevel = "Silver";
        } else {
            membershipLevel = "Bronze";
        }
    }

    /**
     * Display details of the customer.
     */
    public void displayCustomer() {
        System.out.printf("Customer ID: %-4d | Name: %-20s | Contact: %-15s | Level: %-10s%n", 
            customerId, name, contact, membershipLevel);
    }
}

