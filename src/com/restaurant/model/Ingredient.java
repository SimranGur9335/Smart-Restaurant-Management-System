package com.restaurant.model;

/**
 * Model representing kitchen pantry ingredients.
 */
public class Ingredient {
    private int ingredientId;
    private String name;
    private double quantity;
    private String unit; // e.g. "kg", "liters", "units"
    private double minimumStock;
    private String expiryDate; // "YYYY-MM-DD"

    public Ingredient(int ingredientId, String name, double quantity, String unit, double minimumStock, String expiryDate) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.minimumStock = minimumStock;
        this.expiryDate = expiryDate;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(double minimumStock) {
        this.minimumStock = minimumStock;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isLowStock() {
        return quantity <= minimumStock;
    }

    public boolean isNearExpiry() {
        if (expiryDate == null || expiryDate.isEmpty()) return false;
        try {
            java.time.LocalDate exp = java.time.LocalDate.parse(expiryDate);
            java.time.LocalDate today = java.time.LocalDate.now();
            return exp.isBefore(today.plusDays(7)); // Alert if expiring in 7 days
        } catch (Exception e) {
            return false;
        }
    }
}
