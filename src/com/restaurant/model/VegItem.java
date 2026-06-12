package com.restaurant.model;

/**
 * Subclass representing a Vegetarian food item.
 * Demonstrates Inheritance and Polymorphism (Method Overriding).
 */
public class VegItem extends FoodItem {

    // Constructor calling super constructor (Inheritance)
    public VegItem(int itemId, String itemName, double price) {
        super(itemId, itemName, price);
    }

    /**
     * Overrides the displayItem method from FoodItem.
     * Demonstrates Polymorphism (Method Overriding).
     */
    @Override
    public void displayItem() {
        System.out.printf("[Veg] ID: %-4d | Name: %-25s | Price: ₹%.2f%n", 
            getItemId(), getItemName(), getPrice());
    }
}
