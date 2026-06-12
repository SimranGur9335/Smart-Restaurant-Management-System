package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.utility.SearchUtility;

/**
 * Controller service representing the Restaurant.
 * Manages the in-memory database of menu items, customers, orders, and sales data.
 * Demonstrates OOP design, inheritance, polymorphism, and arrays.
 */
public class Restaurant {
    private FoodItem[] menu;
    private int menuCount;

    private Customer[] customers;
    private int customerCount;

    private Order[] orders;
    private int orderCount;

    private double[] dailySales;
    private int salesCount;

    // Constructor
    public Restaurant() {
        this.menu = new FoodItem[20];
        this.menuCount = 0;

        this.customers = new Customer[10];
        this.customerCount = 0;

        this.orders = new Order[10];
        this.orderCount = 0;

        this.dailySales = new double[30];
        this.salesCount = 0;

        initializeMenu();
        initializeSampleCustomers();
        initializeSampleSales();
    }

    // Initialize 16 default menu items
    private void initializeMenu() {
        // Adding Veg Items
        addFoodItem(new VegItem(101, "Paneer Tikka", 220.00));
        addFoodItem(new VegItem(102, "Garlic Bread", 120.00));
        addFoodItem(new VegItem(103, "Spring Rolls", 150.00));
        addFoodItem(new VegItem(106, "Veg Biryani", 250.00));
        addFoodItem(new VegItem(108, "Margherita Pizza", 290.00));
        addFoodItem(new VegItem(110, "Hakka Noodles", 180.00));
        addFoodItem(new VegItem(112, "Chocolate Brownie", 160.00));
        addFoodItem(new VegItem(114, "Caesar Salad", 170.00));
        addFoodItem(new VegItem(115, "Tandoori Roti", 30.00));
        addFoodItem(new VegItem(116, "Butter Naan", 45.00));

        // Adding Non-Veg Items
        addFoodItem(new NonVegItem(104, "Butter Chicken", 380.00));
        addFoodItem(new NonVegItem(105, "Chicken Tikka", 340.00));
        addFoodItem(new NonVegItem(107, "Chicken Biryani", 320.00));
        addFoodItem(new NonVegItem(109, "Pepperoni Pizza", 390.00));
        addFoodItem(new NonVegItem(111, "Chilli Chicken", 310.00));
        addFoodItem(new NonVegItem(113, "Fish and Chips", 350.00));
    }

    // Helper to add food item to the menu array
    private void addFoodItem(FoodItem item) {
        if (menuCount == menu.length) {
            FoodItem[] newMenu = new FoodItem[menu.length * 2];
            System.arraycopy(menu, 0, newMenu, 0, menu.length);
            menu = newMenu;
        }
        menu[menuCount++] = item;
    }

    // Initialize sample customer records
    private void initializeSampleCustomers() {
        addCustomer("Aarav Sharma", "9876543210");
        addCustomer("Diya Patel", "8765432109");
        addCustomer("Rohan Sen", "7654321098");
    }

    // Initialize 10 days of sample daily sales
    private void initializeSampleSales() {
        addSalesRecord(1200.50);
        addSalesRecord(950.00);
        addSalesRecord(1500.25);
        addSalesRecord(800.00);
        addSalesRecord(2100.40);
        addSalesRecord(1100.00);
        addSalesRecord(1750.60);
        addSalesRecord(1300.00);
        addSalesRecord(2200.50);
        addSalesRecord(1900.00);
    }

    // Display all items in the menu
    public void displayMenu() {
        System.out.println("=========================================================================");
        System.out.println("                              RESTAURANT MENU                            ");
        System.out.println("=========================================================================");
        for (int i = 0; i < menuCount; i++) {
            menu[i].displayItem(); // Polymorphism: displays either VegItem or NonVegItem formatting
        }
        System.out.println("=========================================================================");
    }

    // Create a copy of the menu items for sorting
    public FoodItem[] getMenuCopy() {
        FoodItem[] copy = new FoodItem[menuCount];
        System.arraycopy(menu, 0, copy, 0, menuCount);
        return copy;
    }

    public int getMenuCount() {
        return menuCount;
    }

    // Register a new customer
    public Customer addCustomer(String name, String contact) {
        if (customerCount == customers.length) {
            Customer[] newCustomers = new Customer[customers.length * 2];
            System.arraycopy(customers, 0, newCustomers, 0, customers.length);
            customers = newCustomers;
        }
        int newId = 200 + (customerCount + 1); // Customer IDs start from 201
        Customer c = new Customer(newId, name, contact);
        customers[customerCount++] = c;
        return c;
    }

    // Display customer records
    public void displayCustomers() {
        if (customerCount == 0) {
            System.out.println("No customer records found.");
            return;
        }
        System.out.println("=========================================================================");
        System.out.println("                            REGISTERED CUSTOMERS                         ");
        System.out.println("=========================================================================");
        for (int i = 0; i < customerCount; i++) {
            customers[i].displayCustomer();
        }
        System.out.println("=========================================================================");
    }

    // Search for customer by ID
    public Customer findCustomerById(int id) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getCustomerId() == id) {
                return customers[i];
            }
        }
        return null;
    }

    // Search food item using Linear Search (by Name)
    public FoodItem searchFoodItemByName(String name) {
        return SearchUtility.linearSearchByName(menu, menuCount, name);
    }

    // Search food item using Binary Search (by ID)
    public FoodItem searchFoodItemById(int id) {
        return SearchUtility.binarySearchById(menu, menuCount, id);
    }

    // Place an Order
    public Order createOrder(Customer customer) {
        if (orderCount == orders.length) {
            Order[] newOrders = new Order[orders.length * 2];
            System.arraycopy(orders, 0, newOrders, 0, orders.length);
            orders = newOrders;
        }
        int newOrderId = 5000 + (orderCount + 1); // Order IDs start from 5001
        Order order = new Order(newOrderId, customer);
        orders[orderCount++] = order;
        return order;
    }

    // Find order by ID
    public Order findOrderById(int orderId) {
        for (int i = 0; i < orderCount; i++) {
            if (orders[i].getOrderId() == orderId) {
                return orders[i];
            }
        }
        return null;
    }

    // Add record to daily sales
    public void addSalesRecord(double amount) {
        if (salesCount == dailySales.length) {
            double[] newSales = new double[dailySales.length * 2];
            System.arraycopy(dailySales, 0, newSales, 0, dailySales.length);
            dailySales = newSales;
        }
        dailySales[salesCount++] = amount;
    }

    // Get copies of daily sales array
    public double[] getDailySales() {
        double[] copy = new double[salesCount];
        System.arraycopy(dailySales, 0, copy, 0, salesCount);
        return copy;
    }

    public int getSalesCount() {
        return salesCount;
    }

    // Get copy of registered customers list
    public Customer[] getCustomersCopy() {
        Customer[] copy = new Customer[customerCount];
        System.arraycopy(customers, 0, copy, 0, customerCount);
        return copy;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    // Get copy of placed orders list
    public Order[] getOrdersCopy() {
        Order[] copy = new Order[orderCount];
        System.arraycopy(orders, 0, copy, 0, orderCount);
        return copy;
    }

    public int getOrderCount() {
        return orderCount;
    }
}
