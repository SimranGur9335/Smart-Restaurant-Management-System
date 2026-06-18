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
    private String description;
    private String category; // Starters, Main Course, Beverages, Desserts
    private boolean available;
    private String imagePath;

    // Parameterized constructor
    public FoodItem(int itemId, String itemName, double price) {
        this(itemId, itemName, price, "", "Main Course", true, "");
    }

    public FoodItem(int itemId, String itemName, double price, String description, String category, boolean available, String imagePath) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.description = description;
        this.category = category;
        this.available = available;
        this.imagePath = imagePath;
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

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category != null ? category : "Main Course";
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getImagePath() {
        return imagePath != null ? imagePath : "";
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Abstract method to display item details.
     * Must be implemented by concrete subclasses.
     * Demonstrates Abstraction and Polymorphism.
     */
    public abstract void displayItem();
}

