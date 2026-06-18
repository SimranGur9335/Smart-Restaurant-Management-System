package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.utility.SearchUtility;
import com.restaurant.utility.JsonHelper;

import java.io.*;
import java.util.*;

/**
 * Controller service representing the Restaurant.
 * Manages the JSON file-based databases of menu items, customers, orders, tables,
 * reservations, inventory, suppliers, staff, attendance, feedback, and sales data.
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

    private Table[] tables;
    private int tableCount;

    private Reservation[] reservations;
    private int reservationCount;

    private Ingredient[] inventory;
    private int inventoryCount;

    private Supplier[] suppliers;
    private int supplierCount;

    private Employee[] staff;
    private int staffCount;

    private Attendance[] attendance;
    private int attendanceCount;

    private Feedback[] feedback;
    private int feedbackCount;

    // Data folder path
    private static final String DATA_DIR = "data";

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

        this.tables = new Table[10];
        this.tableCount = 0;

        this.reservations = new Reservation[10];
        this.reservationCount = 0;

        this.inventory = new Ingredient[20];
        this.inventoryCount = 0;

        this.suppliers = new Supplier[10];
        this.supplierCount = 0;

        this.staff = new Employee[10];
        this.staffCount = 0;

        this.attendance = new Attendance[20];
        this.attendanceCount = 0;

        this.feedback = new Feedback[10];
        this.feedbackCount = 0;

        // Ensure data directory exists
        new File(DATA_DIR).mkdirs();

        // Load all data from files, or populate default samples if empty
        loadAllData();
    }

    // --- Persistence System ---

    private void loadAllData() {
        loadMenu();
        loadCustomers();
        loadOrders();
        loadDailySales();
        loadTables();
        loadReservations();
        loadInventory();
        loadSuppliers();
        loadStaff();
        loadAttendance();
        loadFeedback();
    }

    private String readFile(String filename) {
        File file = new File(DATA_DIR, filename);
        if (!file.exists()) return null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
        }
        return null;
    }

    private void writeFile(String filename, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(DATA_DIR, filename)));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing " + filename + ": " + e.getMessage());
        }
    }

    // --- Menu Storage ---
    public synchronized void saveMenu() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < menuCount; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("itemId", menu[i].getItemId());
            map.put("type", (menu[i] instanceof VegItem) ? "Veg" : "NonVeg");
            map.put("itemName", menu[i].getItemName());
            map.put("price", menu[i].getPrice());
            map.put("description", menu[i].getDescription());
            map.put("category", menu[i].getCategory());
            map.put("available", menu[i].isAvailable());
            map.put("imagePath", menu[i].getImagePath());
            list.add(map);
        }
        writeFile("menu.json", JsonHelper.toJson(list));
    }

    private void loadMenu() {
        String json = readFile("menu.json");
        if (json == null) {
            initializeMenu();
            saveMenu();
            return;
        }
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            int id = Integer.parseInt(map.get("itemId"));
            String type = map.get("type");
            String name = map.get("itemName");
            double price = Double.parseDouble(map.get("price"));
            String desc = map.get("description");
            String cat = map.get("category");
            boolean avail = Boolean.parseBoolean(map.get("available"));
            String img = map.get("imagePath");

            FoodItem item;
            if ("Veg".equalsIgnoreCase(type)) {
                item = new VegItem(id, name, price, desc, cat, avail, img);
            } else {
                item = new NonVegItem(id, name, price, desc, cat, avail, img);
            }
            addFoodItemNoSave(item);
        }
    }

    private void initializeMenu() {
        addFoodItemNoSave(new VegItem(101, "Paneer Tikka", 220.00, "Spiced cottage cheese cubes grilled in tandoor.", "Starters", true, ""));
        addFoodItemNoSave(new VegItem(102, "Garlic Bread", 120.00, "Toasted bread topped with garlic and herb butter.", "Starters", true, ""));
        addFoodItemNoSave(new VegItem(103, "Spring Rolls", 150.00, "Crispy fried rolls filled with seasoned vegetables.", "Starters", true, ""));
        addFoodItemNoSave(new VegItem(106, "Veg Biryani", 250.00, "Basmati rice cooked with fresh greens and select spices.", "Main Course", true, ""));
        addFoodItemNoSave(new VegItem(108, "Margherita Pizza", 290.00, "Classic pizza with tomato sauce, mozzarella, and fresh basil.", "Main Course", true, ""));
        addFoodItemNoSave(new VegItem(110, "Hakka Noodles", 180.00, "Stir-fried noodles with assorted vegetables and sauce.", "Main Course", true, ""));
        addFoodItemNoSave(new VegItem(112, "Chocolate Brownie", 160.00, "Rich fudge chocolate brownie served warm.", "Desserts", true, ""));
        addFoodItemNoSave(new VegItem(114, "Caesar Salad", 170.00, "Crisp romaine lettuce tossed in Caesar dressing with croutons.", "Starters", true, ""));
        addFoodItemNoSave(new VegItem(115, "Tandoori Roti", 30.00, "Indian flatbread made from wheat flour cooked in tandoor.", "Main Course", true, ""));
        addFoodItemNoSave(new VegItem(116, "Butter Naan", 45.00, "Soft oven-baked flatbread brushed with butter.", "Main Course", true, ""));

        addFoodItemNoSave(new NonVegItem(104, "Butter Chicken", 380.00, "Tender chicken cooked in rich buttery tomato sauce.", "Main Course", true, ""));
        addFoodItemNoSave(new NonVegItem(105, "Chicken Tikka", 340.00, "Boneless spiced chicken pieces skewered and grilled.", "Starters", true, ""));
        addFoodItemNoSave(new NonVegItem(107, "Chicken Biryani", 320.00, "Layered rice dish with spiced chicken meat.", "Main Course", true, ""));
        addFoodItemNoSave(new NonVegItem(109, "Pepperoni Pizza", 390.00, "Topped with spicy pepperoni slices and mozzarella.", "Main Course", true, ""));
        addFoodItemNoSave(new NonVegItem(111, "Chilli Chicken", 310.00, "Sweet, spicy, and slightly sour crispy chicken bites.", "Starters", true, ""));
        addFoodItemNoSave(new NonVegItem(113, "Fish and Chips", 350.00, "Deep-fried battered fish served with potato chips.", "Main Course", true, ""));
    }

    private void addFoodItemNoSave(FoodItem item) {
        if (menuCount == menu.length) {
            FoodItem[] newMenu = new FoodItem[menu.length * 2];
            System.arraycopy(menu, 0, newMenu, 0, menu.length);
            menu = newMenu;
        }
        menu[menuCount++] = item;
    }

    public synchronized void addFoodItem(FoodItem item) {
        addFoodItemNoSave(item);
        saveMenu();
    }

    public synchronized void updateFoodItem(FoodItem updatedItem) {
        for (int i = 0; i < menuCount; i++) {
            if (menu[i].getItemId() == updatedItem.getItemId()) {
                menu[i] = updatedItem;
                saveMenu();
                return;
            }
        }
    }

    public synchronized void deleteFoodItem(int id) {
        int index = -1;
        for (int i = 0; i < menuCount; i++) {
            if (menu[i].getItemId() == id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (int i = index; i < menuCount - 1; i++) {
                menu[i] = menu[i + 1];
            }
            menu[--menuCount] = null;
            saveMenu();
        }
    }

    // --- Customers Storage ---
    public synchronized void saveCustomers() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < customerCount; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("customerId", customers[i].getCustomerId());
            map.put("name", customers[i].getName());
            map.put("contact", customers[i].getContact());
            map.put("email", customers[i].getEmail());
            map.put("visits", customers[i].getVisits());
            map.put("rewardPoints", customers[i].getRewardPoints());
            list.add(map);
        }
        writeFile("customers.json", JsonHelper.toJson(list));
    }

    private void loadCustomers() {
        String json = readFile("customers.json");
        if (json == null) {
            initializeSampleCustomers();
            saveCustomers();
            return;
        }
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            int id = Integer.parseInt(map.get("customerId"));
            String name = map.get("name");
            String contact = map.get("contact");
            String email = map.get("email");
            int visits = Integer.parseInt(map.get("visits"));
            int reward = Integer.parseInt(map.get("rewardPoints"));
            
            Customer c = new Customer(id, name, contact, email, visits, reward);
            addCustomerNoSave(c);
        }
    }

    private void initializeSampleCustomers() {
        addCustomerNoSave(new Customer(201, "Aarav Sharma", "9876543210", "aarav@gmail.com", 3, 150));
        addCustomerNoSave(new Customer(202, "Diya Patel", "8765432109", "diya@gmail.com", 1, 40));
        addCustomerNoSave(new Customer(203, "Rohan Sen", "7654321098", "rohan@gmail.com", 8, 620));
    }

    private void addCustomerNoSave(Customer c) {
        if (customerCount == customers.length) {
            Customer[] newCustomers = new Customer[customers.length * 2];
            System.arraycopy(customers, 0, newCustomers, 0, customers.length);
            customers = newCustomers;
        }
        customers[customerCount++] = c;
    }

    public synchronized Customer addCustomer(String name, String contact) {
        return addCustomer(name, contact, "", 0, 0);
    }

    public synchronized Customer addCustomer(String name, String contact, String email, int visits, int points) {
        int newId = 200 + (customerCount + 1);
        Customer c = new Customer(newId, name, contact, email, visits, points);
        addCustomerNoSave(c);
        saveCustomers();
        return c;
    }

    public synchronized void updateCustomer(Customer c) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getCustomerId() == c.getCustomerId()) {
                customers[i] = c;
                saveCustomers();
                return;
            }
        }
    }

    public synchronized void deleteCustomer(int id) {
        int index = -1;
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getCustomerId() == id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (int i = index; i < customerCount - 1; i++) {
                customers[i] = customers[i + 1];
            }
            customers[--customerCount] = null;
            saveCustomers();
        }
    }

    // --- Orders Storage ---
    public synchronized void saveOrders() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < orderCount; i++) {
            Order o = orders[i];
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("orderId", o.getOrderId());
            map.put("customerId", o.getCustomer().getCustomerId());
            map.put("isPaid", o.isPaid());
            map.put("orderType", o.getOrderType());
            map.put("orderStatus", o.getOrderStatus());
            map.put("tableId", o.getTableId());
            map.put("couponCode", o.getCouponCode());
            map.put("couponDiscount", o.getCouponDiscount());
            map.put("orderDate", o.getOrderDate());
            
            // Build item IDs comma separated list
            StringBuilder itemsSb = new StringBuilder();
            FoodItem[] items = o.getOrderedItems();
            for (int j = 0; j < items.length; j++) {
                if (j > 0) itemsSb.append(",");
                itemsSb.append(items[j].getItemId());
            }
            map.put("itemIds", itemsSb.toString());
            list.add(map);
        }
        writeFile("orders.json", JsonHelper.toJson(list));
    }

    private void loadOrders() {
        String json = readFile("orders.json");
        if (json == null) return;
        
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            int orderId = Integer.parseInt(map.get("orderId"));
            int custId = Integer.parseInt(map.get("customerId"));
            boolean isPaid = Boolean.parseBoolean(map.get("isPaid"));
            String type = map.get("orderType");
            String status = map.get("orderStatus");
            int tableId = Integer.parseInt(map.get("tableId"));
            String coupon = map.get("couponCode");
            double couponDisc = Double.parseDouble(map.get("couponDiscount"));
            String date = map.get("orderDate");
            String itemIds = map.get("itemIds");

            Customer cust = findCustomerById(custId);
            if (cust == null) continue;

            Order order = new Order(orderId, cust);
            order.setOrderType(type);
            order.setOrderStatus(status);
            order.setTableId(tableId);
            order.setCouponCode(coupon);
            order.setCouponDiscount(couponDisc);
            order.setPaid(isPaid);
            order.setOrderDate(date);

            if (itemIds != null && !itemIds.trim().isEmpty()) {
                String[] split = itemIds.split(",");
                for (String sId : split) {
                    FoodItem item = searchFoodItemById(Integer.parseInt(sId));
                    if (item != null) {
                        order.addItem(item);
                    }
                }
            }
            order.calculateTotal(); // Update totals
            addOrderNoSave(order);
        }
    }

    private void addOrderNoSave(Order order) {
        if (orderCount == orders.length) {
            Order[] newOrders = new Order[orders.length * 2];
            System.arraycopy(orders, 0, newOrders, 0, orders.length);
            orders = newOrders;
        }
        orders[orderCount++] = order;
    }

    public synchronized Order createOrder(Customer customer) {
        if (orderCount == orders.length) {
            Order[] newOrders = new Order[orders.length * 2];
            System.arraycopy(orders, 0, newOrders, 0, orders.length);
            orders = newOrders;
        }
        int newOrderId = 5000 + (orderCount + 1);
        Order order = new Order(newOrderId, customer);
        orders[orderCount++] = order;
        saveOrders();
        return order;
    }

    public synchronized void updateOrder(Order order) {
        for (int i = 0; i < orderCount; i++) {
            if (orders[i].getOrderId() == order.getOrderId()) {
                orders[i] = order;
                saveOrders();
                return;
            }
        }
    }

    public synchronized void cancelOrder(int orderId) {
        for (int i = 0; i < orderCount; i++) {
            if (orders[i].getOrderId() == orderId) {
                orders[i].setOrderStatus("Cancelled");
                // If it had a table, release it
                if (orders[i].getTableId() != -1) {
                    Table tbl = findTableById(orders[i].getTableId());
                    if (tbl != null) {
                        tbl.setStatus("Available");
                        tbl.setAssignedCustomerId(-1);
                        saveTables();
                    }
                }
                saveOrders();
                return;
            }
        }
    }

    // --- Daily Sales Storage ---
    public synchronized void saveDailySales() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < salesCount; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("day", i + 1);
            map.put("amount", dailySales[i]);
            list.add(map);
        }
        writeFile("daily_sales.json", JsonHelper.toJson(list));
    }

    private void loadDailySales() {
        String json = readFile("daily_sales.json");
        if (json == null) {
            initializeSampleSales();
            saveDailySales();
            return;
        }
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            double amount = Double.parseDouble(map.get("amount"));
            addSalesRecordNoSave(amount);
        }
    }

    private void initializeSampleSales() {
        addSalesRecordNoSave(1200.50);
        addSalesRecordNoSave(950.00);
        addSalesRecordNoSave(1500.25);
        addSalesRecordNoSave(800.00);
        addSalesRecordNoSave(2100.40);
        addSalesRecordNoSave(1100.00);
        addSalesRecordNoSave(1750.60);
        addSalesRecordNoSave(1300.00);
        addSalesRecordNoSave(2200.50);
        addSalesRecordNoSave(1900.00);
    }

    private void addSalesRecordNoSave(double amount) {
        if (salesCount == dailySales.length) {
            double[] newSales = new double[dailySales.length * 2];
            System.arraycopy(dailySales, 0, newSales, 0, dailySales.length);
            dailySales = newSales;
        }
        dailySales[salesCount++] = amount;
    }

    public synchronized void addSalesRecord(double amount) {
        addSalesRecordNoSave(amount);
        saveDailySales();
    }

    // --- Tables Domain ---
    public synchronized void saveTables() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < tableCount; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("tableId", tables[i].getTableId());
            map.put("capacity", tables[i].getCapacity());
            map.put("status", tables[i].getStatus());
            map.put("assignedCustomerId", tables[i].getAssignedCustomerId());
            list.add(map);
        }
        writeFile("tables.json", JsonHelper.toJson(list));
    }

    private void loadTables() {
        String json = readFile("tables.json");
        if (json == null) {
            initializeTables();
            saveTables();
            return;
        }
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            int tableId = Integer.parseInt(map.get("tableId"));
            int cap = Integer.parseInt(map.get("capacity"));
            String status = map.get("status");
            int custId = Integer.parseInt(map.get("assignedCustomerId"));
            addTableNoSave(new Table(tableId, cap, status, custId));
        }
    }

    private void initializeTables() {
        addTableNoSave(new Table(1, 2));
        addTableNoSave(new Table(2, 2));
        addTableNoSave(new Table(3, 4));
        addTableNoSave(new Table(4, 4));
        addTableNoSave(new Table(5, 6));
        addTableNoSave(new Table(6, 6));
        addTableNoSave(new Table(7, 8));
        addTableNoSave(new Table(8, 10));
    }

    private void addTableNoSave(Table t) {
        if (tableCount == tables.length) {
            Table[] newTables = new Table[tables.length * 2];
            System.arraycopy(tables, 0, newTables, 0, tables.length);
            tables = newTables;
        }
        tables[tableCount++] = t;
    }

    public synchronized Table addTable(int tableId, int capacity) {
        // Prevent duplicate IDs
        if (findTableById(tableId) != null) return null;
        Table t = new Table(tableId, capacity);
        addTableNoSave(t);
        saveTables();
        return t;
    }

    public synchronized void updateTable(Table t) {
        for (int i = 0; i < tableCount; i++) {
            if (tables[i].getTableId() == t.getTableId()) {
                tables[i] = t;
                saveTables();
                return;
            }
        }
    }

    public synchronized void deleteTable(int tableId) {
        int index = -1;
        for (int i = 0; i < tableCount; i++) {
            if (tables[i].getTableId() == tableId) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (int i = index; i < tableCount - 1; i++) {
                tables[i] = tables[i + 1];
            }
            tables[--tableCount] = null;
            saveTables();
        }
    }

    public Table findTableById(int tableId) {
        for (int i = 0; i < tableCount; i++) {
            if (tables[i].getTableId() == tableId) return tables[i];
        }
        return null;
    }

    public Table[] getTablesCopy() {
        Table[] copy = new Table[tableCount];
        System.arraycopy(tables, 0, copy, 0, tableCount);
        return copy;
    }

    public int getTableCount() {
        return tableCount;
    }

    // --- Reservations Domain ---
    public synchronized void saveReservations() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("reservationId", r.getReservationId());
            map.put("customerName", r.getCustomerName());
            map.put("date", r.getDate());
            map.put("time", r.getTime());
            map.put("guests", r.getGuests());
            map.put("tableId", r.getTableId());
            map.put("status", r.getStatus());
            list.add(map);
        }
        writeFile("reservations.json", JsonHelper.toJson(list));
    }

    private void loadReservations() {
        String json = readFile("reservations.json");
        if (json == null) {
            initializeReservations();
            saveReservations();
            return;
        }
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            int rId = Integer.parseInt(map.get("reservationId"));
            String name = map.get("customerName");
            String date = map.get("date");
            String time = map.get("time");
            int guests = Integer.parseInt(map.get("guests"));
            int tableId = Integer.parseInt(map.get("tableId"));
            String status = map.get("status");
            addReservationNoSave(new Reservation(rId, name, date, time, guests, tableId, status));
        }
    }

    private void initializeReservations() {
        java.time.LocalDate today = java.time.LocalDate.now();
        addReservationNoSave(new Reservation(1, "Aarav Sharma", today.toString(), "19:30", 4, 3, "Confirmed"));
        addReservationNoSave(new Reservation(2, "Diya Patel", today.plusDays(1).toString(), "20:00", 2, 1, "Pending"));
    }

    private void addReservationNoSave(Reservation r) {
        if (reservationCount == reservations.length) {
            Reservation[] newReservations = new Reservation[reservations.length * 2];
            System.arraycopy(reservations, 0, newReservations, 0, reservations.length);
            reservations = newReservations;
        }
        reservations[reservationCount++] = r;
    }

    public synchronized Reservation createReservation(String customerName, String date, String time, int guests, int tableId) {
        int newId = 1000 + (reservationCount + 1);
        Reservation r = new Reservation(newId, customerName, date, time, guests, tableId);
        addReservationNoSave(r);
        saveReservations();
        return r;
    }

    public synchronized void updateReservation(Reservation r) {
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i].getReservationId() == r.getReservationId()) {
                reservations[i] = r;
                saveReservations();
                return;
            }
        }
    }

    public synchronized void cancelReservation(int id) {
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i].getReservationId() == id) {
                reservations[i].setStatus("Cancelled");
                saveReservations();
                return;
            }
        }
    }

    public Reservation[] getReservationsCopy() {
        Reservation[] copy = new Reservation[reservationCount];
        System.arraycopy(reservations, 0, copy, 0, reservationCount);
        return copy;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    // --- Inventory Domain ---
    public synchronized void saveInventory() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < inventoryCount; i++) {
            Ingredient ing = inventory[i];
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ingredientId", ing.getIngredientId());
            map.put("name", ing.getName());
            map.put("quantity", ing.getQuantity());
            map.put("unit", ing.getUnit());
            map.put("minimumStock", ing.getMinimumStock());
            map.put("expiryDate", ing.getExpiryDate());
            list.add(map);
        }
        writeFile("inventory.json", JsonHelper.toJson(list));
    }

    private void loadInventory() {
        String json = readFile("inventory.json");
        if (json == null) {
            initializeInventory();
            saveInventory();
            return;
        }
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            int id = Integer.parseInt(map.get("ingredientId"));
            String name = map.get("name");
            double qty = Double.parseDouble(map.get("quantity"));
            String unit = map.get("unit");
            double min = Double.parseDouble(map.get("minimumStock"));
            String exp = map.get("expiryDate");
            addIngredientNoSave(new Ingredient(id, name, qty, unit, min, exp));
        }
    }

    private void initializeInventory() {
        java.time.LocalDate today = java.time.LocalDate.now();
        addIngredientNoSave(new Ingredient(301, "Paneer (Cottage Cheese)", 15.5, "kg", 5.0, today.plusDays(4).toString()));
        addIngredientNoSave(new Ingredient(302, "Basmati Rice", 120.0, "kg", 20.0, today.plusDays(180).toString()));
        addIngredientNoSave(new Ingredient(303, "Boneless Chicken", 8.0, "kg", 15.0, today.plusDays(2).toString())); // Alerts low stock + near expiry
        addIngredientNoSave(new Ingredient(304, "Tomato Puree", 45.0, "liters", 10.0, today.plusDays(30).toString()));
        addIngredientNoSave(new Ingredient(305, "Mozzarella Cheese", 3.2, "kg", 8.0, today.plusDays(6).toString())); // Alert low stock
    }

    private void addIngredientNoSave(Ingredient ing) {
        if (inventoryCount == inventory.length) {
            Ingredient[] newInv = new Ingredient[inventory.length * 2];
            System.arraycopy(inventory, 0, newInv, 0, inventory.length);
            inventory = newInv;
        }
        inventory[inventoryCount++] = ing;
    }

    public synchronized Ingredient addIngredient(String name, double quantity, String unit, double minimumStock, String expiryDate) {
        int id = 300 + (inventoryCount + 1);
        Ingredient ing = new Ingredient(id, name, quantity, unit, minimumStock, expiryDate);
        addIngredientNoSave(ing);
        saveInventory();
        return ing;
    }

    public synchronized void updateIngredient(Ingredient ing) {
        for (int i = 0; i < inventoryCount; i++) {
            if (inventory[i].getIngredientId() == ing.getIngredientId()) {
                inventory[i] = ing;
                saveInventory();
                return;
            }
        }
    }

    public synchronized void deleteIngredient(int id) {
        int index = -1;
        for (int i = 0; i < inventoryCount; i++) {
            if (inventory[i].getIngredientId() == id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (int i = index; i < inventoryCount - 1; i++) {
                inventory[i] = inventory[i + 1];
            }
            inventory[--inventoryCount] = null;
            saveInventory();
        }
    }

    public Ingredient[] getInventoryCopy() {
        Ingredient[] copy = new Ingredient[inventoryCount];
        System.arraycopy(inventory, 0, copy, 0, inventoryCount);
        return copy;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    // --- Suppliers Domain ---
    public synchronized void saveSuppliers() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < supplierCount; i++) {
            Supplier s = suppliers[i];
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("supplierId", s.getSupplierId());
            map.put("name", s.getName());
            map.put("contact", s.getContact());
            map.put("address", s.getAddress());
            map.put("products", s.getProducts());
            list.add(map);
        }
        writeFile("suppliers.json", JsonHelper.toJson(list));
    }

    private void loadSuppliers() {
        String json = readFile("suppliers.json");
        if (json == null) {
            initializeSuppliers();
            saveSuppliers();
            return;
        }
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            int id = Integer.parseInt(map.get("supplierId"));
            String name = map.get("name");
            String contact = map.get("contact");
            String addr = map.get("address");
            String prod = map.get("products");
            addSupplierNoSave(new Supplier(id, name, contact, addr, prod));
        }
    }

    private void initializeSuppliers() {
        addSupplierNoSave(new Supplier(401, "Fresh Farms Produce", "9988776655", "45 Agri Lane, Ooty", "Vegetables, Greens, Potatoes"));
        addSupplierNoSave(new Supplier(402, "Supreme Dairy & Poultry", "8877665544", "12 Industrial Belt, Pune", "Paneer, Mozzarella, Cream, Eggs, Boneless Chicken"));
    }

    private void addSupplierNoSave(Supplier s) {
        if (supplierCount == suppliers.length) {
            Supplier[] newSupp = new Supplier[suppliers.length * 2];
            System.arraycopy(suppliers, 0, newSupp, 0, suppliers.length);
            suppliers = newSupp;
        }
        suppliers[supplierCount++] = s;
    }

    public synchronized Supplier addSupplier(String name, String contact, String address, String products) {
        int id = 400 + (supplierCount + 1);
        Supplier s = new Supplier(id, name, contact, address, products);
        addSupplierNoSave(s);
        saveSuppliers();
        return s;
    }

    public synchronized void updateSupplier(Supplier s) {
        for (int i = 0; i < supplierCount; i++) {
            if (suppliers[i].getSupplierId() == s.getSupplierId()) {
                suppliers[i] = s;
                saveSuppliers();
                return;
            }
        }
    }

    public synchronized void deleteSupplier(int id) {
        int index = -1;
        for (int i = 0; i < supplierCount; i++) {
            if (suppliers[i].getSupplierId() == id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (int i = index; i < supplierCount - 1; i++) {
                suppliers[i] = suppliers[i + 1];
            }
            suppliers[--supplierCount] = null;
            saveSuppliers();
        }
    }

    public Supplier[] getSuppliersCopy() {
        Supplier[] copy = new Supplier[supplierCount];
        System.arraycopy(suppliers, 0, copy, 0, supplierCount);
        return copy;
    }

    public int getSupplierCount() {
        return supplierCount;
    }

    // --- Staff Domain ---
    public synchronized void saveStaff() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < staffCount; i++) {
            Employee emp = staff[i];
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("employeeId", emp.getEmployeeId());
            map.put("name", emp.getName());
            map.put("role", emp.getRole());
            map.put("contact", emp.getContact());
            map.put("shift", emp.getShift());
            list.add(map);
        }
        writeFile("staff.json", JsonHelper.toJson(list));
    }

    private void loadStaff() {
        String json = readFile("staff.json");
        if (json == null) {
            initializeStaff();
            saveStaff();
            return;
        }
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            int id = Integer.parseInt(map.get("employeeId"));
            String name = map.get("name");
            String role = map.get("role");
            String contact = map.get("contact");
            String shift = map.get("shift");
            addEmployeeNoSave(new Employee(id, name, role, contact, shift));
        }
    }

    private void initializeStaff() {
        addEmployeeNoSave(new Employee(601, "Gursimran Singh", "Admin", "9999888877", "Morning"));
        addEmployeeNoSave(new Employee(602, "Amit Sharma", "Manager", "8888777766", "Morning"));
        addEmployeeNoSave(new Employee(603, "Rajesh Kumar", "Chef", "7777666655", "Evening"));
        addEmployeeNoSave(new Employee(604, "Sohan Singh", "Waiter", "6666555544", "Evening"));
        addEmployeeNoSave(new Employee(605, "Pooja Gupta", "Cashier", "5555444433", "Morning"));
    }

    private void addEmployeeNoSave(Employee e) {
        if (staffCount == staff.length) {
            Employee[] newStaff = new Employee[staff.length * 2];
            System.arraycopy(staff, 0, newStaff, 0, staff.length);
            staff = newStaff;
        }
        staff[staffCount++] = e;
    }

    public synchronized Employee addEmployee(String name, String role, String contact, String shift) {
        int id = 600 + (staffCount + 1);
        Employee e = new Employee(id, name, role, contact, shift);
        addEmployeeNoSave(e);
        saveStaff();
        return e;
    }

    public synchronized void updateEmployee(Employee e) {
        for (int i = 0; i < staffCount; i++) {
            if (staff[i].getEmployeeId() == e.getEmployeeId()) {
                staff[i] = e;
                saveStaff();
                return;
            }
        }
    }

    public synchronized void deleteEmployee(int id) {
        int index = -1;
        for (int i = 0; i < staffCount; i++) {
            if (staff[i].getEmployeeId() == id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (int i = index; i < staffCount - 1; i++) {
                staff[i] = staff[i + 1];
            }
            staff[--staffCount] = null;
            saveStaff();
        }
    }

    public Employee[] getStaffCopy() {
        Employee[] copy = new Employee[staffCount];
        System.arraycopy(staff, 0, copy, 0, staffCount);
        return copy;
    }

    public int getStaffCount() {
        return staffCount;
    }

    // --- Attendance Domain ---
    public synchronized void saveAttendance() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < attendanceCount; i++) {
            Attendance att = attendance[i];
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("date", att.getDate());
            map.put("employeeId", att.getEmployeeId());
            map.put("clockInTime", att.getClockInTime());
            map.put("clockOutTime", att.getClockOutTime());
            map.put("present", att.isPresent());
            list.add(map);
        }
        writeFile("attendance.json", JsonHelper.toJson(list));
    }

    private void loadAttendance() {
        String json = readFile("attendance.json");
        if (json == null) return;
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            String date = map.get("date");
            int id = Integer.parseInt(map.get("employeeId"));
            String cin = map.get("clockInTime");
            String cout = map.get("clockOutTime");
            boolean pres = Boolean.parseBoolean(map.get("present"));
            addAttendanceNoSave(new Attendance(date, id, cin, cout, pres));
        }
    }

    private void addAttendanceNoSave(Attendance a) {
        if (attendanceCount == attendance.length) {
            Attendance[] newAtt = new Attendance[attendance.length * 2];
            System.arraycopy(attendance, 0, newAtt, 0, attendance.length);
            attendance = newAtt;
        }
        attendance[attendanceCount++] = a;
    }

    public synchronized void recordClockIn(int employeeId, String time) {
        String date = java.time.LocalDate.now().toString();
        // Check if record exists
        for (int i = 0; i < attendanceCount; i++) {
            if (attendance[i].getEmployeeId() == employeeId && attendance[i].getDate().equals(date)) {
                attendance[i].setClockInTime(time);
                attendance[i].setPresent(true);
                saveAttendance();
                return;
            }
        }
        addAttendanceNoSave(new Attendance(date, employeeId, time, "", true));
        saveAttendance();
    }

    public synchronized void recordClockOut(int employeeId, String time) {
        String date = java.time.LocalDate.now().toString();
        for (int i = 0; i < attendanceCount; i++) {
            if (attendance[i].getEmployeeId() == employeeId && attendance[i].getDate().equals(date)) {
                attendance[i].setClockOutTime(time);
                saveAttendance();
                return;
            }
        }
        addAttendanceNoSave(new Attendance(date, employeeId, "", time, true));
        saveAttendance();
    }

    public Attendance[] getAttendanceCopy() {
        Attendance[] copy = new Attendance[attendanceCount];
        System.arraycopy(attendance, 0, copy, 0, attendanceCount);
        return copy;
    }

    public int getAttendanceCount() {
        return attendanceCount;
    }

    // --- Feedback Domain ---
    public synchronized void saveFeedback() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < feedbackCount; i++) {
            Feedback fb = feedback[i];
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("feedbackId", fb.getFeedbackId());
            map.put("customerName", fb.getCustomerName());
            map.put("rating", fb.getRating());
            map.put("comments", fb.getComments());
            map.put("date", fb.getDate());
            list.add(map);
        }
        writeFile("feedback.json", JsonHelper.toJson(list));
    }

    private void loadFeedback() {
        String json = readFile("feedback.json");
        if (json == null) {
            initializeFeedback();
            saveFeedback();
            return;
        }
        ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray(json);
        for (LinkedHashMap<String, String> map : list) {
            int id = Integer.parseInt(map.get("feedbackId"));
            String name = map.get("customerName");
            int rating = Integer.parseInt(map.get("rating"));
            String comm = map.get("comments");
            String date = map.get("date");
            addFeedbackNoSave(new Feedback(id, name, rating, comm, date));
        }
    }

    private void initializeFeedback() {
        java.time.LocalDate today = java.time.LocalDate.now();
        addFeedbackNoSave(new Feedback(1, "Aarav Sharma", 5, "Amazing food! Butter chicken was incredibly rich and creamy.", today.minusDays(1).toString()));
        addFeedbackNoSave(new Feedback(2, "Diya Patel", 4, "Good service, margerita pizza was delicious. Will come again.", today.toString()));
    }

    private void addFeedbackNoSave(Feedback fb) {
        if (feedbackCount == feedback.length) {
            Feedback[] newFb = new Feedback[feedback.length * 2];
            System.arraycopy(feedback, 0, newFb, 0, feedback.length);
            feedback = newFb;
        }
        feedback[feedbackCount++] = fb;
    }

    public synchronized Feedback addFeedback(String customerName, int rating, String comments) {
        int id = feedbackCount + 1;
        Feedback fb = new Feedback(id, customerName, rating, comments, java.time.LocalDate.now().toString());
        addFeedbackNoSave(fb);
        saveFeedback();
        return fb;
    }

    public Feedback[] getFeedbackCopy() {
        Feedback[] copy = new Feedback[feedbackCount];
        System.arraycopy(feedback, 0, copy, 0, feedbackCount);
        return copy;
    }

    public int getFeedbackCount() {
        return feedbackCount;
    }

    // --- Search & Utilities bridging ---

    public void displayMenu() {
        System.out.println("=========================================================================");
        System.out.println("                              RESTAURANT MENU                            ");
        System.out.println("=========================================================================");
        for (int i = 0; i < menuCount; i++) {
            menu[i].displayItem();
        }
        System.out.println("=========================================================================");
    }

    public FoodItem[] getMenuCopy() {
        FoodItem[] copy = new FoodItem[menuCount];
        System.arraycopy(menu, 0, copy, 0, menuCount);
        return copy;
    }

    public int getMenuCount() {
        return menuCount;
    }

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

    public Customer findCustomerById(int id) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getCustomerId() == id) {
                return customers[i];
            }
        }
        return null;
    }

    public FoodItem searchFoodItemByName(String name) {
        return SearchUtility.linearSearchByName(menu, menuCount, name);
    }

    public FoodItem searchFoodItemById(int id) {
        return SearchUtility.binarySearchById(menu, menuCount, id);
    }

    public Order findOrderById(int orderId) {
        for (int i = 0; i < orderCount; i++) {
            if (orders[i].getOrderId() == orderId) {
                return orders[i];
            }
        }
        return null;
    }

    public double[] getDailySales() {
        double[] copy = new double[salesCount];
        System.arraycopy(dailySales, 0, copy, 0, salesCount);
        return copy;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public Customer[] getCustomersCopy() {
        Customer[] copy = new Customer[customerCount];
        System.arraycopy(customers, 0, copy, 0, customerCount);
        return copy;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public Order[] getOrdersCopy() {
        Order[] copy = new Order[orderCount];
        System.arraycopy(orders, 0, copy, 0, orderCount);
        return copy;
    }

    public int getOrderCount() {
        return orderCount;
    }
}

