package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Customer Panel allowing registration of new customers and viewing existing customers.
 * Demonstrates GUI Forms, JTables, and validation dialogs.
 */
public class CustomerPanel extends JPanel {
    private Restaurant restaurant;
    private JTextField txtName;
    private JTextField txtContact;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(24, 24));
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Customer Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(15, 23, 42));
        JLabel subtitleLabel = new JLabel("Add new customers and view details of registered diners");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Main split content: Form on left, Table on right
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // --- Left panel: Form ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(24, 24, 24, 24)
        ));

        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.insets = new Insets(8, 0, 8, 0);
        fgbc.weightx = 1.0;
        fgbc.gridx = 0;

        JLabel formTitle = new JLabel("Register Customer");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(15, 23, 42));
        fgbc.gridy = 0;
        formPanel.add(formTitle, fgbc);

        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(241, 245, 249));
        fgbc.gridy = 1;
        formPanel.add(separator, fgbc);

        // Name field
        JLabel lblName = new JLabel("Customer Name *");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblName.setForeground(new Color(71, 85, 105));
        fgbc.gridy = 2;
        formPanel.add(lblName, fgbc);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setPreferredSize(new Dimension(0, 36));
        fgbc.gridy = 3;
        formPanel.add(txtName, fgbc);

        // Contact field
        JLabel lblContact = new JLabel("Contact Number *");
        lblContact.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblContact.setForeground(new Color(71, 85, 105));
        fgbc.gridy = 4;
        formPanel.add(lblContact, fgbc);

        txtContact = new JTextField();
        txtContact.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContact.setPreferredSize(new Dimension(0, 36));
        fgbc.gridy = 5;
        formPanel.add(txtContact, fgbc);

        // Register button
        JButton btnRegister = new JButton("Register Customer");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(new Color(37, 99, 235)); // Modern blue
        btnRegister.setPreferredSize(new Dimension(0, 40));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fgbc.gridy = 6;
        fgbc.insets = new Insets(20, 0, 8, 0);
        formPanel.add(btnRegister, fgbc);

        // Button action listener
        btnRegister.addActionListener(e -> handleAddCustomer());

        // Place formPanel on left of main split
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        mainContent.add(formPanel, gbc);

        // --- Right panel: Table ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel tableTitle = new JLabel("Registered Customers Registry");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(new Color(15, 23, 42));
        tableTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        // Setup table model & design
        String[] columns = {"Customer ID", "Name", "Contact Details"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only registry view
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        customerTable.setRowHeight(32);
        customerTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        customerTable.getTableHeader().setBackground(new Color(241, 245, 249));
        customerTable.getTableHeader().setForeground(new Color(71, 85, 105));
        customerTable.setSelectionBackground(new Color(219, 234, 254));
        customerTable.setSelectionForeground(new Color(30, 41, 59));
        customerTable.setShowGrid(true);
        customerTable.setGridColor(new Color(241, 245, 249));

        JScrollPane tableScroll = new JScrollPane(customerTable);
        tableScroll.setBorder(null);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // Add padding space between left form and right table panels
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacing = new JPanel();
        spacing.setBackground(getBackground());
        mainContent.add(spacing, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.63;
        mainContent.add(tablePanel, gbc);

        add(mainContent, BorderLayout.CENTER);

        // Refresh table initially
        refreshCustomerTable();
    }

    // Handles validation and addition of customer
    private void handleAddCustomer() {
        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (contact.length() < 10) {
            JOptionPane.showMessageDialog(this, "Contact number must be at least 10 digits.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Customer addedCustomer = restaurant.addCustomer(name, contact);
            JOptionPane.showMessageDialog(this, String.format("Customer registered successfully!\nID Assigned: %d", addedCustomer.getCustomerId()), "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
            txtName.setText("");
            txtContact.setText("");
            refreshCustomerTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving customer: " + ex.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Re-queries customers from database and updates JTable rows.
     */
    public void refreshCustomerTable() {
        tableModel.setRowCount(0);
        Customer[] list = restaurant.getCustomersCopy();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(),
                c.getName(),
                c.getContact()
            });
        }
    }
}
