package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Ingredient;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

/**
 * Visual Inventory management panel.
 * Logs kitchen items and alerts on low-stock/expiring goods.
 */
public class InventoryPanel extends JPanel {
    private Restaurant restaurant;
    private JTextField txtName;
    private JTextField txtQty;
    private JTextField txtMinStock;
    private JTextField txtExpiry;
    private JComboBox<String> cmbUnit;
    private JTable invTable;
    private DefaultTableModel tableModel;

    public InventoryPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(24, 24));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Inventory & Ingredients");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Log stock quantities, monitor reorder thresholds, and check item expirations");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Grid split layout
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // --- Left Panel: Log Form ---
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

        JLabel formTitle = new JLabel("Add Ingredient");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(Theme.getFg());
        fgbc.gridy = 0;
        formPanel.add(formTitle, fgbc);

        JSeparator separator = new JSeparator();
        separator.setForeground(Theme.getBorderColor());
        fgbc.gridy = 1;
        formPanel.add(separator, fgbc);

        // Ingredient Name
        JLabel lblName = new JLabel("Ingredient Name *");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblName.setForeground(Theme.getSubFg());
        fgbc.gridy = 2;
        formPanel.add(lblName, fgbc);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtName.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 3;
        formPanel.add(txtName, fgbc);

        // Quantity
        JLabel lblQty = new JLabel("Current Quantity *");
        lblQty.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblQty.setForeground(Theme.getSubFg());
        fgbc.gridy = 4;
        formPanel.add(lblQty, fgbc);

        txtQty = new JTextField();
        txtQty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtQty.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 5;
        formPanel.add(txtQty, fgbc);

        // Unit
        JLabel lblUnit = new JLabel("Unit of Measure *");
        lblUnit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUnit.setForeground(Theme.getSubFg());
        fgbc.gridy = 6;
        formPanel.add(lblUnit, fgbc);

        String[] units = {"kg", "liters", "grams", "milliliters", "units", "pouches"};
        cmbUnit = new JComboBox<>(units);
        cmbUnit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbUnit.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 7;
        formPanel.add(cmbUnit, fgbc);

        // Min Stock
        JLabel lblMin = new JLabel("Minimum Stock Alert Limit *");
        lblMin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMin.setForeground(Theme.getSubFg());
        fgbc.gridy = 8;
        formPanel.add(lblMin, fgbc);

        txtMinStock = new JTextField();
        txtMinStock.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMinStock.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 9;
        formPanel.add(txtMinStock, fgbc);

        // Expiry
        JLabel lblExp = new JLabel("Expiry Date (YYYY-MM-DD) *");
        lblExp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblExp.setForeground(Theme.getSubFg());
        fgbc.gridy = 10;
        formPanel.add(lblExp, fgbc);

        txtExpiry = new JTextField();
        txtExpiry.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtExpiry.setPreferredSize(new Dimension(0, 32));
        txtExpiry.setText(java.time.LocalDate.now().plusMonths(1).toString());
        fgbc.gridy = 11;
        formPanel.add(txtExpiry, fgbc);

        // Button submit
        JButton btnSubmit = new JButton("Save Ingredient");
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

        btnSubmit.addActionListener(e -> handleAddIngredient());

        gbc.gridx = 0;
        gbc.weightx = 0.35;
        mainContent.add(formPanel, gbc);

        // Spacing
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacing = new JPanel();
        spacing.setBackground(getBackground());
        mainContent.add(spacing, gbc);

        // --- Right Panel: Logs registry table ---
        JPanel tablePanel = new JPanel(new BorderLayout(12, 12));
        tablePanel.setBackground(Theme.getPanelBg());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel tableTitle = new JLabel("Pantry Stock Levels Registry");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(Theme.getFg());
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Quantity", "Min Reorder Level", "Expiry Date", "Low Stock?", "Near Expiry?"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        invTable = new JTable(tableModel);
        invTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        invTable.setRowHeight(30);
        invTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        invTable.getTableHeader().setBackground(new Color(241, 245, 249));
        invTable.setSelectionBackground(Theme.getTableSelectionBg());
        invTable.setShowGrid(true);
        invTable.setGridColor(Theme.getBorderColor());

        JScrollPane tableScroll = new JScrollPane(invTable);
        tableScroll.setBorder(null);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // Bottom delete button
        JPanel rowActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        rowActions.setBackground(Theme.getPanelBg());
        
        JButton btnDelete = new JButton("Delete Item");
        btnDelete.setBackground(Theme.getPrimaryRed());
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        rowActions.add(btnDelete);
        tablePanel.add(rowActions, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> handleDeleteIngredient());

        gbc.gridx = 2;
        gbc.weightx = 0.63;
        mainContent.add(tablePanel, gbc);

        add(mainContent, BorderLayout.CENTER);

        refreshTable();
    }

    private void handleAddIngredient() {
        String name = txtName.getText().trim();
        String qtyStr = txtQty.getText().trim();
        String minStr = txtMinStock.getText().trim();
        String exp = txtExpiry.getText().trim();
        String unit = (String) cmbUnit.getSelectedItem();

        if (name.isEmpty() || qtyStr.isEmpty() || minStr.isEmpty() || exp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all ingredient fields.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double qty = Double.parseDouble(qtyStr);
            double min = Double.parseDouble(minStr);

            Ingredient added = restaurant.addIngredient(name, qty, unit, min, exp);
            
            txtName.setText("");
            txtQty.setText("");
            txtMinStock.setText("");
            txtExpiry.setText(java.time.LocalDate.now().plusMonths(1).toString());
            
            refreshTable();
            
            // Check immediate alerts
            if (added.isLowStock()) {
                NotificationManager.addNotification("Low Stock Alert: " + name + "!");
            } else if (added.isNearExpiry()) {
                NotificationManager.addNotification("Near Expiry Alert: " + name + "!");
            } else {
                NotificationManager.addNotification("Ingredient " + name + " saved!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity and Min Stock must be valid decimals.", "Input Format Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDeleteIngredient() {
        int row = invTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ingredient from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            restaurant.deleteIngredient(id);
            refreshTable();
            NotificationManager.addNotification("Ingredient " + name + " deleted.");
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        Ingredient[] list = restaurant.getInventoryCopy();
        boolean lowAlertTriggered = false;
        
        for (Ingredient ing : list) {
            boolean isLow = ing.isLowStock();
            boolean isExp = ing.isNearExpiry();
            
            tableModel.addRow(new Object[]{
                ing.getIngredientId(),
                ing.getName(),
                ing.getQuantity() + " " + ing.getUnit(),
                ing.getMinimumStock() + " " + ing.getUnit(),
                ing.getExpiryDate(),
                isLow ? "LOW STOCK" : "OK",
                isExp ? "EXPIRING SOON" : "OK"
            });

            if (isLow && !lowAlertTriggered) {
                // Limit notifications to prevent spamming
                lowAlertTriggered = true;
                NotificationManager.addNotification("Pantry alert: " + ing.getName() + " is running low!");
            }
        }
    }
}
