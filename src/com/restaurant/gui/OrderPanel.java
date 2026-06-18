package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Customer;
import com.restaurant.model.FoodItem;
import com.restaurant.model.Order;
import com.restaurant.model.Table;
import com.restaurant.model.VegItem;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Place Order panel.
 * Contains tabs to Place New Order and Track/Cancel existing ones.
 */
public class OrderPanel extends JPanel {
    private Restaurant restaurant;
    private DashboardFrame mainFrame;

    // Tab 1: New Order elements
    private JComboBox<String> cmbCustomer;
    private JComboBox<String> cmbOrderType;
    private JComboBox<String> cmbTable;
    private JTable menuTable;
    private JTable cartTable;
    private DefaultTableModel menuModel;
    private DefaultTableModel cartModel;
    private JLabel lblSubtotal;
    private JLabel lblDiscount;
    private JLabel lblTotal;
    private ArrayList<FoodItem> cartItems = new ArrayList<>();

    // Tab 2: Track Orders elements
    private JTable trackTable;
    private DefaultTableModel trackModel;

    public OrderPanel(Restaurant restaurant, DashboardFrame mainFrame) {
        this.restaurant = restaurant;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Order Operations Center");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Place new dining or delivery orders and monitor active kitchen progress");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // --- Tab 1: Place Order Tab ---
        JPanel newOrderPanel = new JPanel(new GridBagLayout());
        newOrderPanel.setBackground(Theme.getBg());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Frame Panel: Details Form & Menu JTable
        JPanel leftPanel = new JPanel(new BorderLayout(12, 12));
        leftPanel.setBackground(Theme.getPanelBg());
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        // Inputs Panel
        JPanel inputsPanel = new JPanel(new GridLayout(2, 2, 12, 12));
        inputsPanel.setBackground(Theme.getPanelBg());

        // Cust Selector
        JPanel custWrapper = new JPanel(new BorderLayout(4, 0));
        custWrapper.setBackground(Theme.getPanelBg());
        JLabel lblCust = new JLabel("Diner Name: ");
        lblCust.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCust.setForeground(Theme.getSubFg());
        cmbCustomer = new JComboBox<>();
        cmbCustomer.setPreferredSize(new Dimension(150, 32));
        custWrapper.add(lblCust, BorderLayout.WEST);
        custWrapper.add(cmbCustomer, BorderLayout.CENTER);
        inputsPanel.add(custWrapper);

        // Order Type Selector
        JPanel typeWrapper = new JPanel(new BorderLayout(4, 0));
        typeWrapper.setBackground(Theme.getPanelBg());
        JLabel lblType = new JLabel("Order Type: ");
        lblType.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblType.setForeground(Theme.getSubFg());
        String[] types = {"Dine-In", "Takeaway", "Delivery"};
        cmbOrderType = new JComboBox<>(types);
        cmbOrderType.setPreferredSize(new Dimension(120, 32));
        typeWrapper.add(lblType, BorderLayout.WEST);
        typeWrapper.add(cmbOrderType, BorderLayout.CENTER);
        inputsPanel.add(typeWrapper);

        // Empty label spacer
        inputsPanel.add(new JLabel(""));

        // Table Selection
        JPanel tableWrapper = new JPanel(new BorderLayout(4, 0));
        tableWrapper.setBackground(Theme.getPanelBg());
        JLabel lblTable = new JLabel("Assign Table: ");
        lblTable.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTable.setForeground(Theme.getSubFg());
        cmbTable = new JComboBox<>();
        cmbTable.setPreferredSize(new Dimension(120, 32));
        tableWrapper.add(lblTable, BorderLayout.WEST);
        tableWrapper.add(cmbTable, BorderLayout.CENTER);
        inputsPanel.add(tableWrapper);

        leftPanel.add(inputsPanel, BorderLayout.NORTH);

        // Available Menu JTable
        String[] menuCols = {"ID", "Type", "Dish Name", "Price"};
        menuModel = new DefaultTableModel(menuCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        menuTable = new JTable(menuModel);
        menuTable.setRowHeight(28);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuTable.getTableHeader().setBackground(new Color(241, 245, 249));
        menuTable.setSelectionBackground(Theme.getTableSelectionBg());
        menuTable.setShowGrid(true);
        menuTable.setGridColor(Theme.getBorderColor());
        
        JScrollPane menuScroll = new JScrollPane(menuTable);
        menuScroll.setBorder(null);
        leftPanel.add(menuScroll, BorderLayout.CENTER);

        JButton btnAddToCart = new JButton("Add Selected Item to Cart");
        btnAddToCart.setBackground(Theme.getPrimaryBlue());
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAddToCart.setPreferredSize(new Dimension(0, 38));
        btnAddToCart.setFocusPainted(false);
        leftPanel.add(btnAddToCart, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.weightx = 0.55;
        newOrderPanel.add(leftPanel, gbc);

        // Spacer
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel space = new JPanel();
        space.setBackground(Theme.getBg());
        newOrderPanel.add(space, gbc);

        // Right Panel: Diner Cart Summary
        JPanel rightPanel = new JPanel(new BorderLayout(12, 12));
        rightPanel.setBackground(Theme.getPanelBg());
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel cartTitle = new JLabel("Diner Cart");
        cartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cartTitle.setForeground(Theme.getFg());
        rightPanel.add(cartTitle, BorderLayout.NORTH);

        String[] cartCols = {"Item Name", "Price"};
        cartModel = new DefaultTableModel(cartCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(28);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        cartTable.getTableHeader().setBackground(new Color(241, 245, 249));
        cartTable.setSelectionBackground(new Color(254, 226, 226));
        cartTable.setSelectionForeground(new Color(153, 27, 27));
        cartTable.setShowGrid(true);
        cartTable.setGridColor(Theme.getBorderColor());
        
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBorder(null);
        rightPanel.add(cartScroll, BorderLayout.CENTER);

        // Calculations & Place Order Buttons
        JPanel pricingPanel = new JPanel(new GridBagLayout());
        pricingPanel.setBackground(Theme.getPanelBg());
        GridBagConstraints pgbc = new GridBagConstraints();
        pgbc.fill = GridBagConstraints.HORIZONTAL;
        pgbc.insets = new Insets(4, 0, 4, 0);
        pgbc.weightx = 1.0;

        pgbc.gridy = 0; pgbc.gridx = 0;
        pricingPanel.add(new JLabel("Subtotal:"), pgbc);
        pgbc.gridx = 1;
        lblSubtotal = new JLabel("₹0.00");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
        pricingPanel.add(lblSubtotal, pgbc);

        pgbc.gridy = 1; pgbc.gridx = 0;
        pricingPanel.add(new JLabel("Tier Discounts:"), pgbc);
        pgbc.gridx = 1;
        lblDiscount = new JLabel("-₹0.00");
        lblDiscount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDiscount.setForeground(Theme.getPrimaryRed());
        lblDiscount.setHorizontalAlignment(SwingConstants.RIGHT);
        pricingPanel.add(lblDiscount, pgbc);

        pgbc.gridy = 2; pgbc.gridx = 0;
        JLabel lblTotTitle = new JLabel("Net Payable:");
        lblTotTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pricingPanel.add(lblTotTitle, pgbc);
        pgbc.gridx = 1;
        lblTotal = new JLabel("₹0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(Theme.getPrimaryGreen());
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        pricingPanel.add(lblTotal, pgbc);

        JPanel pricingAndActions = new JPanel(new BorderLayout(0, 12));
        pricingAndActions.setBackground(Theme.getPanelBg());
        pricingAndActions.add(pricingPanel, BorderLayout.NORTH);

        JPanel cartActions = new JPanel(new GridLayout(1, 2, 8, 0));
        cartActions.setBackground(Theme.getPanelBg());
        
        JButton btnRemoveItem = new JButton("Remove Selected");
        btnRemoveItem.setBackground(Theme.getPrimaryRed());
        btnRemoveItem.setForeground(Color.WHITE);
        btnRemoveItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JButton btnPlaceOrder = new JButton("Confirm Order");
        btnPlaceOrder.setBackground(Theme.getPrimaryGreen());
        btnPlaceOrder.setForeground(Color.WHITE);
        btnPlaceOrder.setFont(new Font("Segoe UI", Font.BOLD, 13));

        cartActions.add(btnRemoveItem);
        cartActions.add(btnPlaceOrder);
        pricingAndActions.add(cartActions, BorderLayout.SOUTH);
        rightPanel.add(pricingAndActions, BorderLayout.SOUTH);

        gbc.gridx = 2;
        gbc.weightx = 0.43;
        newOrderPanel.add(rightPanel, gbc);

        // --- Tab 2: Track Orders Tab ---
        JPanel trackerPanel = new JPanel(new BorderLayout(12, 12));
        trackerPanel.setBackground(Theme.getBg());
        trackerPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        String[] trackCols = {"Order ID", "Customer Name", "Order Type", "Table Assigned", "Order Status", "Grand Total (₹)", "Payment"};
        trackModel = new DefaultTableModel(trackCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        trackTable = new JTable(trackModel);
        trackTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        trackTable.setRowHeight(30);
        trackTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        trackTable.getTableHeader().setBackground(new Color(241, 245, 249));
        trackTable.setSelectionBackground(Theme.getTableSelectionBg());
        trackTable.setShowGrid(true);
        trackTable.setGridColor(Theme.getBorderColor());

        JScrollPane trackScroll = new JScrollPane(trackTable);
        trackScroll.setBorder(null);
        
        JPanel trackCard = new JPanel(new BorderLayout(12, 12));
        trackCard.setBackground(Theme.getPanelBg());
        trackCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));
        trackCard.add(trackScroll, BorderLayout.CENTER);
        trackerPanel.add(trackCard, BorderLayout.CENTER);

        // Cancel Active Order Actions Panel
        JPanel trackBtnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        trackBtnRow.setBackground(Theme.getPanelBg());
        JButton btnCancel = new JButton("Cancel Selected Order");
        btnCancel.setBackground(Theme.getPrimaryRed());
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        trackBtnRow.add(btnCancel);
        trackCard.add(trackBtnRow, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> cancelActiveOrder());

        // Event actions
        btnAddToCart.addActionListener(e -> addToCart());
        btnRemoveItem.addActionListener(e -> removeFromCart());
        btnPlaceOrder.addActionListener(e -> placeOrderSubmit());
        cmbOrderType.addActionListener(e -> toggleTableDropdown());

        tabbedPane.addTab("Place New Order", newOrderPanel);
        tabbedPane.addTab("Track Active Orders & cancellation", trackerPanel);
        add(tabbedPane, BorderLayout.CENTER);

        // Default loaders
        refreshCustomersList();
        refreshMenuTable();
        refreshTableList();
        refreshTrackTable();
        toggleTableDropdown();
    }

    public void refreshCustomersList() {
        cmbCustomer.removeAllItems();
        Customer[] list = restaurant.getCustomersCopy();
        for (Customer c : list) {
            cmbCustomer.addItem(String.format("%d - %s", c.getCustomerId(), c.getName()));
        }
    }

    public void refreshTableList() {
        cmbTable.removeAllItems();
        Table[] list = restaurant.getTablesCopy();
        for (Table t : list) {
            if (t.getStatus().equalsIgnoreCase("Available")) {
                cmbTable.addItem(String.format("Table %d (Cap: %d)", t.getTableId(), t.getCapacity()));
            }
        }
    }

    private void refreshMenuTable() {
        menuModel.setRowCount(0);
        FoodItem[] menu = restaurant.getMenuCopy();
        for (FoodItem item : menu) {
            if (item.isAvailable()) {
                String type = (item instanceof VegItem) ? "Veg" : "Non-Veg";
                menuModel.addRow(new Object[]{
                    item.getItemId(),
                    type,
                    item.getItemName(),
                    String.format("₹%.2f", item.getPrice())
                });
            }
        }
    }

    public void refreshTrackTable() {
        trackModel.setRowCount(0);
        Order[] list = restaurant.getOrdersCopy();
        for (Order o : list) {
            trackModel.addRow(new Object[]{
                o.getOrderId(),
                o.getCustomer().getName(),
                o.getOrderType(),
                o.getTableId() == -1 ? "N/A" : "Table " + o.getTableId(),
                o.getOrderStatus(),
                String.format("₹%.2f", o.getGrandTotal()),
                o.isPaid() ? "Paid" : "Pending"
            });
        }
    }

    private void toggleTableDropdown() {
        String type = (String) cmbOrderType.getSelectedItem();
        boolean isDineIn = "Dine-In".equalsIgnoreCase(type);
        cmbTable.setEnabled(isDineIn);
    }

    private void addToCart() {
        int row = menuTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item from the menu.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int itemId = (int) menuModel.getValueAt(row, 0);
        FoodItem item = restaurant.searchFoodItemById(itemId);
        
        if (item != null) {
            cartItems.add(item);
            cartModel.addRow(new Object[]{
                item.getItemName(),
                String.format("₹%.2f", item.getPrice())
            });
            updateCalculations();
        }
    }

    private void removeFromCart() {
        int row = cartTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an item from the cart to remove.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cartItems.remove(row);
        cartModel.removeRow(row);
        updateCalculations();
    }

    private void updateCalculations() {
        double subtotal = 0.0;
        for (FoodItem item : cartItems) {
            subtotal += item.getPrice();
        }

        double discount = 0.0;
        if (subtotal > 1000.0) {
            discount = subtotal * 0.15;
        } else if (subtotal > 500.0) {
            discount = subtotal * 0.10;
        } else if (subtotal > 200.0) {
            discount = subtotal * 0.05;
        }

        double total = subtotal - discount;

        lblSubtotal.setText(String.format("₹%.2f", subtotal));
        lblDiscount.setText(String.format("-₹%.2f", discount));
        lblTotal.setText(String.format("₹%.2f", total));
    }

    private void placeOrderSubmit() {
        if (cmbCustomer.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "A registered customer is required.", "Customer Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty! Add items first.", "Cart Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedCust = (String) cmbCustomer.getSelectedItem();
        int customerId = Integer.parseInt(selectedCust.split(" - ")[0]);
        Customer customer = restaurant.findCustomerById(customerId);

        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer profile not found.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String orderType = (String) cmbOrderType.getSelectedItem();
        int tableId = -1;

        if (orderType.equalsIgnoreCase("Dine-In")) {
            if (cmbTable.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "No available tables. Change type or release a table.", "Table Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String tableStr = (String) cmbTable.getSelectedItem();
            tableId = Integer.parseInt(tableStr.split(" ")[1]);

            // Set table occupied
            Table tbl = restaurant.findTableById(tableId);
            if (tbl != null) {
                tbl.setStatus("Occupied");
                tbl.setAssignedCustomerId(customerId);
                restaurant.updateTable(tbl);
            }
        }

        try {
            Order order = restaurant.createOrder(customer);
            order.setOrderType(orderType);
            order.setTableId(tableId);
            order.setOrderStatus("Placed");
            
            for (FoodItem item : cartItems) {
                order.addItem(item);
            }
            
            // Re-sync customer loyalty visit counters
            customer.incrementVisits();
            restaurant.updateCustomer(customer);

            String msg = String.format("Order Placed Successfully!\nID Assigned: %d\nGrand Total: ₹%.2f", order.getOrderId(), order.getGrandTotal());
            JOptionPane.showMessageDialog(this, msg, "Order Placed", JOptionPane.INFORMATION_MESSAGE);

            // Clear Cart
            cartItems.clear();
            cartModel.setRowCount(0);
            updateCalculations();

            // Refresh tables & lists
            refreshTableList();
            refreshTrackTable();
            NotificationManager.addNotification("New " + orderType + " order #" + order.getOrderId() + " placed!");

            // Redirect Billing
            mainFrame.showBillingPaneWithOrder(order.getOrderId());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to place order: " + ex.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelActiveOrder() {
        int row = trackTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an active order to cancel.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) trackModel.getValueAt(row, 0);
        String status = (String) trackModel.getValueAt(row, 4);

        if (status.equalsIgnoreCase("Served") || status.equalsIgnoreCase("Delivered") || status.equalsIgnoreCase("Cancelled")) {
            JOptionPane.showMessageDialog(this, "Completed or already cancelled orders cannot be cancelled.", "Cancel Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel Order #" + orderId + "?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            restaurant.cancelOrder(orderId);
            refreshTrackTable();
            refreshTableList();
            NotificationManager.addNotification("Order #" + orderId + " has been cancelled.");
        }
    }
}
