package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Customer;
import com.restaurant.model.FoodItem;
import com.restaurant.model.Order;
import com.restaurant.model.VegItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Order Panel allowing users to select a customer, add/remove items to a cart,
 * dynamically compute discounts, and place an order.
 * Demonstrates interaction between GUI components, event listeners, and models.
 */
public class OrderPanel extends JPanel {
    private Restaurant restaurant;
    private JComboBox<String> cmbCustomer;
    private JTable menuTable;
    private JTable cartTable;
    private DefaultTableModel menuModel;
    private DefaultTableModel cartModel;
    
    // Labels for amounts
    private JLabel lblSubtotal;
    private JLabel lblDiscount;
    private JLabel lblTotal;

    // Temporary cart list
    private ArrayList<FoodItem> cartItems = new ArrayList<>();
    private DashboardFrame mainFrame;

    public OrderPanel(Restaurant restaurant, DashboardFrame mainFrame) {
        this.restaurant = restaurant;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Place Order");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(15, 23, 42));
        JLabel subtitleLabel = new JLabel("Select a registered customer, manage cart items, and confirm your order");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Split Area: Left (Customer & Available Menu), Right (Cart Summary)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // --- Left Panel: Available Menu ---
        JPanel leftPanel = new JPanel(new BorderLayout(12, 12));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        // Customer selection row
        JPanel custSelectPanel = new JPanel(new BorderLayout(8, 0));
        custSelectPanel.setBackground(Color.WHITE);
        JLabel lblChooseCust = new JLabel("Customer Select: ");
        lblChooseCust.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblChooseCust.setForeground(new Color(71, 85, 105));
        custSelectPanel.add(lblChooseCust, BorderLayout.WEST);

        cmbCustomer = new JComboBox<>();
        cmbCustomer.setPreferredSize(new Dimension(200, 32));
        custSelectPanel.add(cmbCustomer, BorderLayout.CENTER);
        leftPanel.add(custSelectPanel, BorderLayout.NORTH);

        // Menu Items Table
        String[] menuCols = {"ID", "Type", "Name", "Price"};
        menuModel = new DefaultTableModel(menuCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        menuTable = new JTable(menuModel);
        menuTable.setRowHeight(28);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuTable.getTableHeader().setBackground(new Color(241, 245, 249));
        menuTable.setSelectionBackground(new Color(219, 234, 254));
        menuTable.setShowGrid(true);
        menuTable.setGridColor(new Color(241, 245, 249));
        
        JScrollPane menuScroll = new JScrollPane(menuTable);
        menuScroll.setBorder(null);
        leftPanel.add(menuScroll, BorderLayout.CENTER);

        // Action additions
        JButton btnAddToCart = new JButton("Add Selected Item to Cart");
        btnAddToCart.setBackground(new Color(37, 99, 235));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAddToCart.setPreferredSize(new Dimension(0, 38));
        btnAddToCart.setFocusPainted(false);
        leftPanel.add(btnAddToCart, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.weightx = 0.55;
        centerPanel.add(leftPanel, gbc);

        // Spacing
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacer = new JPanel();
        spacer.setBackground(getBackground());
        centerPanel.add(spacer, gbc);

        // --- Right Panel: Cart Summary ---
        JPanel rightPanel = new JPanel(new BorderLayout(12, 12));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel cartTitle = new JLabel("Diner Cart");
        cartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cartTitle.setForeground(new Color(15, 23, 42));
        rightPanel.add(cartTitle, BorderLayout.NORTH);

        // Cart items table
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
        cartTable.setSelectionBackground(new Color(254, 226, 226)); // Soft Red
        cartTable.setSelectionForeground(new Color(153, 27, 27));
        cartTable.setShowGrid(true);
        cartTable.setGridColor(new Color(241, 245, 249));
        
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBorder(null);
        rightPanel.add(cartScroll, BorderLayout.CENTER);

        // Pricing details & action panel
        JPanel pricingPanel = new JPanel(new GridBagLayout());
        pricingPanel.setBackground(Color.WHITE);
        GridBagConstraints pgbc = new GridBagConstraints();
        pgbc.fill = GridBagConstraints.HORIZONTAL;
        pgbc.insets = new Insets(4, 0, 4, 0);
        pgbc.weightx = 1.0;

        // Subtotal row
        pgbc.gridy = 0;
        pgbc.gridx = 0;
        JLabel lblSubTitle = new JLabel("Subtotal:");
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubTitle.setForeground(new Color(100, 116, 139));
        pricingPanel.add(lblSubTitle, pgbc);

        pgbc.gridx = 1;
        lblSubtotal = new JLabel("₹0.00");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSubtotal.setForeground(new Color(71, 85, 105));
        pricingPanel.add(lblSubtotal, pgbc);

        // Discount row
        pgbc.gridy = 1;
        pgbc.gridx = 0;
        JLabel lblDiscTitle = new JLabel("Discounts:");
        lblDiscTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDiscTitle.setForeground(new Color(100, 116, 139));
        pricingPanel.add(lblDiscTitle, pgbc);

        pgbc.gridx = 1;
        lblDiscount = new JLabel("-₹0.00");
        lblDiscount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDiscount.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDiscount.setForeground(new Color(220, 38, 38)); // Red for discount
        pricingPanel.add(lblDiscount, pgbc);

        // Total row
        pgbc.gridy = 2;
        pgbc.gridx = 0;
        JLabel lblTotTitle = new JLabel("Net Payable:");
        lblTotTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotTitle.setForeground(new Color(15, 23, 42));
        pricingPanel.add(lblTotTitle, pgbc);

        pgbc.gridx = 1;
        lblTotal = new JLabel("₹0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setForeground(new Color(22, 163, 74)); // Green theme
        pricingPanel.add(lblTotal, pgbc);

        // Dynamic pricing split panel
        JPanel pricingAndActions = new JPanel(new BorderLayout(0, 12));
        pricingAndActions.setBackground(Color.WHITE);
        pricingAndActions.add(pricingPanel, BorderLayout.NORTH);

        JPanel cartActions = new JPanel(new GridLayout(1, 2, 8, 0));
        cartActions.setBackground(Color.WHITE);
        
        JButton btnRemoveItem = new JButton("Remove Selected");
        btnRemoveItem.setBackground(new Color(239, 68, 68)); // Red theme
        btnRemoveItem.setForeground(Color.WHITE);
        btnRemoveItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRemoveItem.setPreferredSize(new Dimension(0, 36));
        btnRemoveItem.setFocusPainted(false);
        
        JButton btnPlaceOrder = new JButton("Place Order");
        btnPlaceOrder.setBackground(new Color(22, 163, 74));
        btnPlaceOrder.setForeground(Color.WHITE);
        btnPlaceOrder.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPlaceOrder.setFocusPainted(false);

        cartActions.add(btnRemoveItem);
        cartActions.add(btnPlaceOrder);
        pricingAndActions.add(cartActions, BorderLayout.SOUTH);

        rightPanel.add(pricingAndActions, BorderLayout.SOUTH);

        gbc.gridx = 2;
        gbc.weightx = 0.43;
        centerPanel.add(rightPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Action triggers
        btnAddToCart.addActionListener(e -> addToCart());
        btnRemoveItem.addActionListener(e -> removeFromCart());
        btnPlaceOrder.addActionListener(e -> placeOrderSubmit());

        // Initial loads
        refreshCustomersList();
        refreshMenuTable();
    }

    /**
     * Refreshes the customers dropdown with updated registration logs.
     */
    public void refreshCustomersList() {
        cmbCustomer.removeAllItems();
        Customer[] list = restaurant.getCustomersCopy();
        for (Customer c : list) {
            cmbCustomer.addItem(String.format("%d - %s", c.getCustomerId(), c.getName()));
        }
    }

    private void refreshMenuTable() {
        menuModel.setRowCount(0);
        FoodItem[] menu = restaurant.getMenuCopy();
        for (FoodItem item : menu) {
            String type = (item instanceof VegItem) ? "Veg" : "Non-Veg";
            menuModel.addRow(new Object[]{
                item.getItemId(),
                type,
                item.getItemName(),
                String.format("₹%.2f", item.getPrice())
            });
        }
    }

    // Cart actions
    private void addToCart() {
        int row = menuTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a food item from the menu first.", "Selection Error", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Please select an item from the cart to remove.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cartItems.remove(row);
        cartModel.removeRow(row);
        updateCalculations();
    }

    // Pricing calculation updates
    private void updateCalculations() {
        double subtotal = 0.0;
        for (FoodItem item : cartItems) {
            subtotal += item.getPrice();
        }

        // Apply discount conditions identical to Order.java
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

    // Submit order logic
    private void placeOrderSubmit() {
        if (cmbCustomer.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No registered customers available. Please add a customer first.", "Customer Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty! Add items to order.", "Cart Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Parse chosen customer ID
        String selectedCust = (String) cmbCustomer.getSelectedItem();
        int customerId = Integer.parseInt(selectedCust.split(" - ")[0]);
        Customer customer = restaurant.findCustomerById(customerId);

        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Instantiate Order entity
            Order order = restaurant.createOrder(customer);
            for (FoodItem item : cartItems) {
                order.addItem(item);
            }

            // Success dialog containing invoice summary
            String msg = String.format(
                "Order Placed Successfully!\n\n" +
                "Order ID: %d\n" +
                "Customer: %s\n" +
                "Total Items: %d\n" +
                "Subtotal: ₹%.2f\n" +
                "Discount Applied: ₹%.2f\n" +
                "Net Amount Due: ₹%.2f",
                order.getOrderId(), customer.getName(), order.getItemCount(), 
                order.getTotalAmount(), order.getDiscountAmount(), order.getFinalAmount()
            );
            JOptionPane.showMessageDialog(this, msg, "Order Placed", JOptionPane.INFORMATION_MESSAGE);

            // Clean cart state
            cartItems.clear();
            cartModel.setRowCount(0);
            updateCalculations();

            // Auto navigate to billing panel and populate order ID to simplify flow!
            mainFrame.showBillingPaneWithOrder(order.getOrderId());
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to place order: " + ex.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
