package com.restaurant.model;

/**
 * Abstract class representing a food item in the restaurant.
 * Demonstrates Abstraction (Abstract Class) and Encapsulation.
 */
public abstract class FoodItem {
    // Encapsulated fields
    private int itemId;
    private String itemName;
    private double price;

    // Parameterized constructor
    public FoodItem(int itemId, String itemName, double price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
    }

    // Getters and Setters (Encapsulation)
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Abstract method to display item details.
     * Must be implemented by concrete subclasses.
     * Demonstrates Abstraction and Polymorphism.
     */
    public abstract void displayItem();
}
