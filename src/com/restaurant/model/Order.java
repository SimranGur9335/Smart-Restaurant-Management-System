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
    private double couponDiscount;
    private String couponCode;
    private double gst;
    private double finalAmount; // Net amount (subtotal - discounts)
    private double grandTotal;   // Final amount including GST
    private boolean isPaid;
    private String orderType;    // "Dine-In", "Takeaway", "Delivery"
    private String orderStatus;  // "Placed", "Accepted", "Preparing", "Ready", "Served"
    private int tableId;         // Associated Table ID (-1 if not Dine-In)
    private String orderDate;    // Format: "YYYY-MM-DD"

    // Constructor
    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderedItems = new FoodItem[5]; // Initial capacity of 5
        this.itemCount = 0;
        this.totalAmount = 0.0;
        this.discountAmount = 0.0;
        this.couponDiscount = 0.0;
        this.couponCode = "";
        this.gst = 0.0;
        this.finalAmount = 0.0;
        this.grandTotal = 0.0;
        this.isPaid = false;
        this.orderType = "Dine-In";
        this.orderStatus = "Placed";
        this.tableId = -1;
        
        // Default to current date
        java.time.LocalDate today = java.time.LocalDate.now();
        this.orderDate = today.toString();
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

    // Remove an item at specific index
    public void removeItemAt(int index) {
        if (index >= 0 && index < itemCount) {
            for (int i = index; i < itemCount - 1; i++) {
                orderedItems[i] = orderedItems[i + 1];
            }
            orderedItems[--itemCount] = null;
            calculateTotal();
        }
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
        
        // Subtotal after tier discount
        finalAmount = totalAmount - discountAmount - couponDiscount;
        if (finalAmount < 0) {
            finalAmount = 0.0;
        }
        
        // Calculate 5% GST
        gst = finalAmount * 0.05;
        grandTotal = finalAmount + gst;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public FoodItem[] getOrderedItems() {
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

    public double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(double couponDiscount) {
        this.couponDiscount = couponDiscount;
        calculateTotal();
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public double getGst() {
        return gst;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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
        System.out.println("Order Type: " + orderType + (tableId != -1 ? " (Table #" + tableId + ")" : ""));
        System.out.println("Order Status: " + orderStatus);
        System.out.println("----------------------------------------------");
        System.out.println("Items Ordered:");
        for (int i = 0; i < itemCount; i++) {
            System.out.print("  " + (i + 1) + ". ");
            orderedItems[i].displayItem(); // Polymorphic call to displayItem()
        }
        System.out.println("----------------------------------------------");
        System.out.printf("Subtotal:         ₹%.2f%n", totalAmount);
        System.out.printf("Tier Discount:    ₹%.2f%n", discountAmount);
        System.out.printf("Coupon Discount:  ₹%.2f%n", couponDiscount);
        System.out.printf("GST (5%%):         ₹%.2f%n", gst);
        System.out.printf("Grand Total:      ₹%.2f%n", grandTotal);
        System.out.println("Payment Status: " + (isPaid ? "PAID" : "UNPAID"));
        System.out.println("==============================================");
    }
}

