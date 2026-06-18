package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Supplier;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Visual Supplier Management Panel.
 * Logs supplier directories and products supplied.
 */
public class SupplierPanel extends JPanel {
    private Restaurant restaurant;
    private JTextField txtName;
    private JTextField txtContact;
    private JTextField txtAddress;
    private JTextField txtProducts;
    private JTable suppTable;
    private DefaultTableModel tableModel;

    public SupplierPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(24, 24));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Supplier Directory");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Manage vendor contacts, supplier profiles, and ingredient delivery sources");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Main Grid split: Form (Left), Table (Right)
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
            new EmptyBorder(24, 24, 24, 24)
        ));

        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.insets = new Insets(6, 0, 6, 0);
        fgbc.weightx = 1.0;
        fgbc.gridx = 0;

        JLabel formTitle = new JLabel("Register Supplier");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(Theme.getFg());
        fgbc.gridy = 0;
        formPanel.add(formTitle, fgbc);

        JSeparator separator = new JSeparator();
        separator.setForeground(Theme.getBorderColor());
        fgbc.gridy = 1;
        formPanel.add(separator, fgbc);

        // Supplier Name
        JLabel lblName = new JLabel("Supplier Company Name *");
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

        // Address
        JLabel lblAddr = new JLabel("Address *");
        lblAddr.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAddr.setForeground(Theme.getSubFg());
        fgbc.gridy = 6;
        formPanel.add(lblAddr, fgbc);

        txtAddress = new JTextField();
        txtAddress.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtAddress.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 7;
        formPanel.add(txtAddress, fgbc);

        // Products
        JLabel lblProd = new JLabel("Supplied Products (e.g. Dairy, Poultry) *");
        lblProd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProd.setForeground(Theme.getSubFg());
        fgbc.gridy = 8;
        formPanel.add(lblProd, fgbc);

        txtProducts = new JTextField();
        txtProducts.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtProducts.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 9;
        formPanel.add(txtProducts, fgbc);

        // Button submit
        JButton btnSubmit = new JButton("Register Vendor");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setBackground(Theme.getPrimaryBlue());
        btnSubmit.setPreferredSize(new Dimension(0, 36));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fgbc.gridy = 12;
        fgbc.insets = new Insets(16, 0, 4, 0);
        formPanel.add(btnSubmit, fgbc);

        btnSubmit.addActionListener(e -> handleAddSupplier());

        gbc.gridx = 0;
        gbc.weightx = 0.35;
        mainContent.add(formPanel, gbc);

        // Spacing
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacing = new JPanel();
        spacing.setBackground(getBackground());
        mainContent.add(spacing, gbc);

        // --- Right Panel: Logs JTable ---
        JPanel tablePanel = new JPanel(new BorderLayout(12, 12));
        tablePanel.setBackground(Theme.getPanelBg());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel tableTitle = new JLabel("Supplier Directory Listing");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(Theme.getFg());
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Company Name", "Contact No.", "Business Address", "Supplied Goods"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        suppTable = new JTable(tableModel);
        suppTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        suppTable.setRowHeight(30);
        suppTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        suppTable.getTableHeader().setBackground(new Color(241, 245, 249));
        suppTable.setSelectionBackground(Theme.getTableSelectionBg());
        suppTable.setShowGrid(true);
        suppTable.setGridColor(Theme.getBorderColor());

        JScrollPane tableScroll = new JScrollPane(suppTable);
        tableScroll.setBorder(null);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // Bottom Actions
        JPanel rowActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        rowActions.setBackground(Theme.getPanelBg());
        
        JButton btnDelete = new JButton("Delete Supplier");
        btnDelete.setBackground(Theme.getPrimaryRed());
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        rowActions.add(btnDelete);
        tablePanel.add(rowActions, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> handleDeleteSupplier());

        gbc.gridx = 2;
        gbc.weightx = 0.63;
        mainContent.add(tablePanel, gbc);

        add(mainContent, BorderLayout.CENTER);

        refreshTable();
    }

    private void handleAddSupplier() {
        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();
        String address = txtAddress.getText().trim();
        String products = txtProducts.getText().trim();

        if (name.isEmpty() || contact.isEmpty() || address.isEmpty() || products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all supplier fields.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        restaurant.addSupplier(name, contact, address, products);
        
        txtName.setText("");
        txtContact.setText("");
        txtAddress.setText("");
        txtProducts.setText("");
        
        refreshTable();
        NotificationManager.addNotification("Supplier " + name + " registered!");
    }

    private void handleDeleteSupplier() {
        int row = suppTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a supplier from the directory to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove " + name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            restaurant.deleteSupplier(id);
            refreshTable();
            NotificationManager.addNotification("Supplier " + name + " removed.");
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        Supplier[] list = restaurant.getSuppliersCopy();
        for (Supplier s : list) {
            tableModel.addRow(new Object[]{
                s.getSupplierId(),
                s.getName(),
                s.getContact(),
                s.getAddress(),
                s.getProducts()
            });
        }
    }
}
