package com.restaurant.model;

/**
 * Class representing a Customer Order.
 * Demonstrates encapsulation, arrays, loops, and conditional statements.
 */
public class Order {
    private int orderId;
    private Customer customer;
    private FoodItem[] orderedItems;
    private int itemCount;
    private double totalAmount;
    private double discountAmount;
    private double finalAmount;
    private boolean isPaid;

    // Constructor
    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderedItems = new FoodItem[5]; // Initial capacity of 5
        this.itemCount = 0;
        this.totalAmount = 0.0;
        this.discountAmount = 0.0;
        this.finalAmount = 0.0;
        this.isPaid = false;
    }

    // Add an item to the order, handling array resizing manually
    public void addItem(FoodItem item) {
        if (itemCount == orderedItems.length) {
            // Resize array: double the capacity (Demonstrates Manual Array Manipulation)
            FoodItem[] newItems = new FoodItem[orderedItems.length * 2];
            System.arraycopy(orderedItems, 0, newItems, 0, orderedItems.length);
            orderedItems = newItems;
        }
        orderedItems[itemCount++] = item;
        calculateTotal();
    }

    // Calculate total amount of items in the order
    public void calculateTotal() {
        totalAmount = 0.0;
        for (int i = 0; i < itemCount; i++) {
            totalAmount += orderedItems[i].getPrice();
        }
        applyDiscounts();
    }

    // Apply discounts using Conditional Statements
    public void applyDiscounts() {
        // Hierarchical discounts:
        // Total > ₹1000: 15% discount
        // Total > ₹500: 10% discount
        // Total > ₹200: 5% discount
        if (totalAmount > 1000.0) {
            discountAmount = totalAmount * 0.15;
        } else if (totalAmount > 500.0) {
            discountAmount = totalAmount * 0.10;
        } else if (totalAmount > 200.0) {
            discountAmount = totalAmount * 0.05;
        } else {
            discountAmount = 0.0;
        }
        finalAmount = totalAmount - discountAmount;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public FoodItem[] getOrderedItems() {
        // Return a copy of only the filled part of the array
        FoodItem[] filledItems = new FoodItem[itemCount];
        System.arraycopy(orderedItems, 0, filledItems, 0, itemCount);
        return filledItems;
    }

    public int getItemCount() {
        return itemCount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    /**
     * Displays complete details of the order.
     */
    public void displayOrderDetails() {
        System.out.println("==============================================");
        System.out.println("                 ORDER DETAILS                ");
        System.out.println("==============================================");
        System.out.println("Order ID: " + orderId);
        if (customer != null) {
            System.out.println("Customer Name: " + customer.getName());
            System.out.println("Customer Contact: " + customer.getContact());
        }
        System.out.println("----------------------------------------------");
        System.out.println("Items Ordered:");
        for (int i = 0; i < itemCount; i++) {
            System.out.print("  " + (i + 1) + ". ");
            orderedItems[i].displayItem(); // Polymorphic call to displayItem()
        }
        System.out.println("----------------------------------------------");
        System.out.printf("Subtotal:       ₹%.2f%n", totalAmount);
        System.out.printf("Discount Applied: ₹%.2f%n", discountAmount);
        System.out.printf("Net Amount:      ₹%.2f%n", finalAmount);
        System.out.println("Payment Status: " + (isPaid ? "PAID" : "UNPAID"));
        System.out.println("==============================================");
    }
}
