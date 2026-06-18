package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Customer;
import com.restaurant.model.Order;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Customer Panel allowing registration, editing, deletion, and order history view of customers.
 */
public class CustomerPanel extends JPanel {
    private Restaurant restaurant;
    private JTextField txtName;
    private JTextField txtContact;
    private JTextField txtEmail;
    private JTextField txtVisits;
    private JTextField txtPoints;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JList<String> orderHistoryList;
    private DefaultListModel<String> historyListModel;
    private Customer selectedCustomer;

    public CustomerPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(24, 24));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Customer Registry");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Register new diners, manage contact accounts, and check loyalty tiers");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Main split content: Form on left, Table + History on right
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // --- Left Panel: Form ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.getPanelBg());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.insets = new Insets(6, 0, 6, 0);
        fgbc.weightx = 1.0;
        fgbc.gridx = 0;

        JLabel formTitle = new JLabel("Diner Registration Form");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(Theme.getFg());
        fgbc.gridy = 0;
        formPanel.add(formTitle, fgbc);

        JSeparator separator = new JSeparator();
        separator.setForeground(Theme.getBorderColor());
        fgbc.gridy = 1;
        formPanel.add(separator, fgbc);

        // Name
        JLabel lblName = new JLabel("Customer Name *");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblName.setForeground(Theme.getSubFg());
        fgbc.gridy = 2;
        formPanel.add(lblName, fgbc);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtName.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 3;
        formPanel.add(txtName, fgbc);

        // Contact
        JLabel lblContact = new JLabel("Contact Number *");
        lblContact.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblContact.setForeground(Theme.getSubFg());
        fgbc.gridy = 4;
        formPanel.add(lblContact, fgbc);

        txtContact = new JTextField();
        txtContact.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtContact.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 5;
        formPanel.add(txtContact, fgbc);

        // Email
        JLabel lblEmail = new JLabel("Email Address");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEmail.setForeground(Theme.getSubFg());
        fgbc.gridy = 6;
        formPanel.add(lblEmail, fgbc);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtEmail.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 7;
        formPanel.add(txtEmail, gbc.gridy = 7);
        formPanel.add(txtEmail, fgbc);

        // Visits
        JLabel lblVisits = new JLabel("Visits Frequency");
        lblVisits.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblVisits.setForeground(Theme.getSubFg());
        fgbc.gridy = 8;
        formPanel.add(lblVisits, fgbc);

        txtVisits = new JTextField("0");
        txtVisits.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtVisits.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 9;
        formPanel.add(txtVisits, fgbc);

        // Points
        JLabel lblPoints = new JLabel("Loyalty Reward Points");
        lblPoints.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPoints.setForeground(Theme.getSubFg());
        fgbc.gridy = 10;
        formPanel.add(lblPoints, fgbc);

        txtPoints = new JTextField("0");
        txtPoints.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPoints.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 11;
        formPanel.add(txtPoints, fgbc);

        // Actions Row
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 8, 0));
        btnRow.setBackground(Theme.getPanelBg());

        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(Theme.getPrimaryBlue());
        btnRegister.setFocusPainted(false);
        btnRegister.addActionListener(e -> handleAddCustomer());

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setBackground(Theme.getPrimaryGreen());
        btnUpdate.setFocusPainted(false);
        btnUpdate.addActionListener(e -> handleUpdateCustomer());

        btnRow.add(btnRegister);
        btnRow.add(btnUpdate);

        fgbc.gridy = 12;
        fgbc.insets = new Insets(16, 0, 4, 0);
        formPanel.add(btnRow, fgbc);

        JButton btnClear = new JButton("Clear / Reset Form");
        btnClear.setBackground(new Color(100, 116, 139));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(e -> resetForm());
        
        fgbc.gridy = 13;
        fgbc.insets = new Insets(4, 0, 4, 0);
        formPanel.add(btnClear, fgbc);

        gbc.gridx = 0;
        gbc.weightx = 0.33;
        mainContent.add(formPanel, gbc);

        // Spacing
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacing = new JPanel();
        spacing.setBackground(getBackground());
        mainContent.add(spacing, gbc);

        // --- Right Panel: Registry Table and history ---
        JPanel rightContent = new JPanel(new GridLayout(2, 1, 0, 16));
        rightContent.setBackground(getBackground());

        // Top Grid Card
        JPanel tablePanel = new JPanel(new BorderLayout(12, 12));
        tablePanel.setBackground(Theme.getPanelBg());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel tableTitle = new JLabel("Diner Records");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(Theme.getFg());
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Contact", "Email", "Visits", "Points", "Membership Level"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        customerTable = new JTable(tableModel);
        customerTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        customerTable.setRowHeight(28);
        customerTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        customerTable.getTableHeader().setBackground(new Color(241, 245, 249));
        customerTable.setSelectionBackground(Theme.getTableSelectionBg());
        customerTable.setShowGrid(true);
        customerTable.setGridColor(Theme.getBorderColor());

        JScrollPane tableScroll = new JScrollPane(customerTable);
        tableScroll.setBorder(null);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // Table Bottom delete Button
        JPanel tableBottomRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        tableBottomRow.setBackground(Theme.getPanelBg());
        
        JButton btnDelete = new JButton("Delete Diner Profile");
        btnDelete.setBackground(Theme.getPrimaryRed());
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        tableBottomRow.add(btnDelete);
        tablePanel.add(tableBottomRow, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> handleDeleteCustomer());

        rightContent.add(tablePanel);

        // Bottom History Card
        JPanel historyPanel = new JPanel(new BorderLayout(8, 8));
        historyPanel.setBackground(Theme.getPanelBg());
        historyPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel historyTitle = new JLabel("Diner Transaction & Order History");
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyTitle.setForeground(Theme.getFg());
        historyPanel.add(historyTitle, BorderLayout.NORTH);

        historyListModel = new DefaultListModel<>();
        orderHistoryList = new JList<>(historyListModel);
        orderHistoryList.setFont(new Font("Consolas", Font.PLAIN, 12));
        orderHistoryList.setBackground(Theme.getBg());
        orderHistoryList.setForeground(Theme.getFg());
        
        JScrollPane historyScroll = new JScrollPane(orderHistoryList);
        historyScroll.setBorder(null);
        historyPanel.add(historyScroll, BorderLayout.CENTER);

        rightContent.add(historyPanel);

        gbc.gridx = 2;
        gbc.weightx = 0.65;
        mainContent.add(rightContent, gbc);

        add(mainContent, BorderLayout.CENTER);

        // Row Click Listener
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = customerTable.getSelectedRow();
                if (row != -1) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    selectedCustomer = restaurant.findCustomerById(id);
                    if (selectedCustomer != null) {
                        txtName.setText(selectedCustomer.getName());
                        txtContact.setText(selectedCustomer.getContact());
                        txtEmail.setText(selectedCustomer.getEmail());
                        txtVisits.setText(String.valueOf(selectedCustomer.getVisits()));
                        txtPoints.setText(String.valueOf(selectedCustomer.getRewardPoints()));
                        
                        refreshHistory();
                    }
                }
            }
        });

        refreshCustomerTable();
    }

    private void resetForm() {
        txtName.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtVisits.setText("0");
        txtPoints.setText("0");
        selectedCustomer = null;
        historyListModel.clear();
        customerTable.clearSelection();
    }

    private void handleAddCustomer() {
        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();
        String email = txtEmail.getText().trim();
        String visitsStr = txtVisits.getText().trim();
        String ptsStr = txtPoints.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Contact fields are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int visits = Integer.parseInt(visitsStr);
            int points = Integer.parseInt(ptsStr);

            Customer c = restaurant.addCustomer(name, contact, email, visits, points);
            resetForm();
            refreshCustomerTable();
            JOptionPane.showMessageDialog(this, "Diner Profile registered successfully! ID Assigned: " + c.getCustomerId(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Visits and Points must be valid integers.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateCustomer() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer from the registry table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();
        String email = txtEmail.getText().trim();
        String visitsStr = txtVisits.getText().trim();
        String ptsStr = txtPoints.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Contact fields are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int visits = Integer.parseInt(visitsStr);
            int points = Integer.parseInt(ptsStr);

            selectedCustomer.setName(name);
            selectedCustomer.setContact(contact);
            selectedCustomer.setEmail(email);
            selectedCustomer.setVisits(visits);
            selectedCustomer.setRewardPoints(points);

            restaurant.updateCustomer(selectedCustomer);
            resetForm();
            refreshCustomerTable();
            
            NotificationManager.addNotification("Diner account updated successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Visits and Points must be valid integers.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer row to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            restaurant.deleteCustomer(id);
            resetForm();
            refreshCustomerTable();
            NotificationManager.addNotification("Customer profile deleted.");
        }
    }

    private void refreshHistory() {
        historyListModel.clear();
        if (selectedCustomer == null) return;

        Order[] orders = restaurant.getOrdersCopy();
        boolean hasOrders = false;
        
        for (Order o : orders) {
            if (o.getCustomer().getCustomerId() == selectedCustomer.getCustomerId()) {
                hasOrders = true;
                String detail = String.format("Order #%d | Date: %s | Type: %s | Status: %s | Net Paid: ₹%.2f | Status: %s",
                    o.getOrderId(), o.getOrderDate(), o.getOrderType(), o.getOrderStatus(), o.getGrandTotal(), o.isPaid() ? "PAID" : "UNPAID");
                historyListModel.addElement(detail);
            }
        }

        if (!hasOrders) {
            historyListModel.addElement("No past orders found for this customer profile.");
        }
    }

    public void refreshCustomerTable() {
        tableModel.setRowCount(0);
        Customer[] list = restaurant.getCustomersCopy();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(),
                c.getName(),
                c.getContact(),
                c.getEmail(),
                c.getVisits(),
                c.getRewardPoints(),
                c.getMembershipLevel()
            });
        }
    }
}
