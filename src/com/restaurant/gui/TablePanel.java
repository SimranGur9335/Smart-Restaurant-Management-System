package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Table;
import com.restaurant.model.Customer;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Visual Table Management Panel.
 * Displays tables as grid cards color-coded by status.
 */
public class TablePanel extends JPanel {
    private Restaurant restaurant;
    private JPanel tablesGrid;
    private JTextField txtTableId;
    private JComboBox<Integer> cmbCapacity;

    public TablePanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(24, 24));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Table Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Manage floor tables, track diner occupancies, and assign customer seats");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Split Content: Left (Form), Right (Visual Floor Grid)
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // --- Left Panel: Add Table Form ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.getPanelBg());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(24, 24, 24, 24)
        ));

        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.insets = new Insets(8, 0, 8, 0);
        fgbc.weightx = 1.0;
        fgbc.gridx = 0;

        JLabel formTitle = new JLabel("Add New Table");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(Theme.getFg());
        fgbc.gridy = 0;
        formPanel.add(formTitle, fgbc);

        JSeparator separator = new JSeparator();
        separator.setForeground(Theme.getBorderColor());
        fgbc.gridy = 1;
        formPanel.add(separator, fgbc);

        // Table Number field
        JLabel lblTableNo = new JLabel("Table Number (ID) *");
        lblTableNo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTableNo.setForeground(Theme.getSubFg());
        fgbc.gridy = 2;
        formPanel.add(lblTableNo, fgbc);

        txtTableId = new JTextField();
        txtTableId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTableId.setPreferredSize(new Dimension(0, 36));
        fgbc.gridy = 3;
        formPanel.add(txtTableId, fgbc);

        // Capacity
        JLabel lblCap = new JLabel("Seating Capacity *");
        lblCap.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCap.setForeground(Theme.getSubFg());
        fgbc.gridy = 4;
        formPanel.add(lblCap, fgbc);

        Integer[] capacities = {2, 4, 6, 8, 10, 12};
        cmbCapacity = new JComboBox<>(capacities);
        cmbCapacity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCapacity.setPreferredSize(new Dimension(0, 36));
        fgbc.gridy = 5;
        formPanel.add(cmbCapacity, fgbc);

        // Register button
        JButton btnAdd = new JButton("Register Table");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(Theme.getPrimaryBlue());
        btnAdd.setPreferredSize(new Dimension(0, 40));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fgbc.gridy = 6;
        fgbc.insets = new Insets(20, 0, 8, 0);
        formPanel.add(btnAdd, fgbc);

        btnAdd.addActionListener(e -> handleAddTable());

        gbc.gridx = 0;
        gbc.weightx = 0.30;
        mainContent.add(formPanel, gbc);

        // Spacing
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacing = new JPanel();
        spacing.setBackground(getBackground());
        mainContent.add(spacing, gbc);

        // --- Right Panel: Floor Grid Layout ---
        JPanel gridCard = new JPanel(new BorderLayout(12, 12));
        gridCard.setBackground(Theme.getPanelBg());
        gridCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        // Legend details
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        legendPanel.setBackground(Theme.getPanelBg());
        legendPanel.add(createLegendItem("Available", new Color(34, 197, 94)));
        legendPanel.add(createLegendItem("Reserved", new Color(249, 115, 22)));
        legendPanel.add(createLegendItem("Occupied", new Color(239, 68, 68)));
        gridCard.add(legendPanel, BorderLayout.NORTH);

        tablesGrid = new JPanel(new GridLayout(0, 4, 16, 16));
        tablesGrid.setBackground(Theme.getPanelBg());
        
        JScrollPane gridScroll = new JScrollPane(tablesGrid);
        gridScroll.setBorder(null);
        gridCard.add(gridScroll, BorderLayout.CENTER);

        gbc.gridx = 2;
        gbc.weightx = 0.68;
        mainContent.add(gridCard, gbc);

        add(mainContent, BorderLayout.CENTER);

        refreshTablesGrid();
    }

    private JPanel createLegendItem(String text, Color dotColor) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setBackground(Theme.getPanelBg());
        
        JPanel dot = new JPanel();
        dot.setPreferredSize(new Dimension(12, 12));
        dot.setBackground(dotColor);
        
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Theme.getSubFg());
        
        p.add(dot);
        p.add(lbl);
        return p;
    }

    private void handleAddTable() {
        String idStr = txtTableId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a table number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            int cap = (int) cmbCapacity.getSelectedItem();

            Table existing = restaurant.findTableById(id);
            if (existing != null) {
                JOptionPane.showMessageDialog(this, "Table number already exists.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            restaurant.addTable(id, cap);
            txtTableId.setText("");
            refreshTablesGrid();
            NotificationManager.addNotification("Table " + id + " Added!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Table ID must be a valid integer.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refreshTablesGrid() {
        tablesGrid.removeAll();
        Table[] list = restaurant.getTablesCopy();
        
        for (Table t : list) {
            tablesGrid.add(createTableCard(t));
        }

        tablesGrid.revalidate();
        tablesGrid.repaint();
    }

    private JPanel createTableCard(Table table) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(120, 110));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(12, 12, 12, 12)
        ));
        
        Color bgStatus;
        if (table.getStatus().equalsIgnoreCase("Occupied")) {
            bgStatus = new Color(239, 68, 68); // Red
        } else if (table.getStatus().equalsIgnoreCase("Reserved")) {
            bgStatus = new Color(249, 115, 22); // Orange
        } else {
            bgStatus = new Color(34, 197, 94); // Green
        }
        
        card.setBackground(bgStatus);

        JLabel lblNo = new JLabel("TABLE " + table.getTableId(), SwingConstants.CENTER);
        lblNo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNo.setForeground(Color.WHITE);
        
        JLabel lblCap = new JLabel("Capacity: " + table.getCapacity(), SwingConstants.CENTER);
        lblCap.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCap.setForeground(new Color(241, 245, 249));

        JLabel lblStatus = new JLabel(table.getStatus().toUpperCase(), SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(Color.WHITE);
        
        card.add(lblNo, BorderLayout.NORTH);
        card.add(lblCap, BorderLayout.CENTER);
        card.add(lblStatus, BorderLayout.SOUTH);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTableActions(table);
            }
        });

        return card;
    }

    private void showTableActions(Table table) {
        String[] options = {"Mark Available", "Mark Occupied (Seat Customer)", "Mark Reserved", "Delete Table", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
            this,
            String.format("Actions for Table %d (Capacity: %d, Status: %s)", table.getTableId(), table.getCapacity(), table.getStatus()),
            "Table Actions",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[4]
        );

        if (choice == 0) {
            table.setStatus("Available");
            table.setAssignedCustomerId(-1);
            restaurant.updateTable(table);
            refreshTablesGrid();
            NotificationManager.addNotification("Table " + table.getTableId() + " marked Available");
        } 
        else if (choice == 1) {
            // Pick customer
            Customer[] customers = restaurant.getCustomersCopy();
            if (customers.length == 0) {
                JOptionPane.showMessageDialog(this, "No registered customers. Please register a customer first.", "Seat Customer Failed", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] custOptions = new String[customers.length];
            for (int i = 0; i < customers.length; i++) {
                custOptions[i] = customers[i].getCustomerId() + " - " + customers[i].getName();
            }
            String result = (String) JOptionPane.showInputDialog(
                this, "Assign diner to Table " + table.getTableId() + ":", "Assign Customer",
                JOptionPane.QUESTION_MESSAGE, null, custOptions, custOptions[0]
            );
            if (result != null) {
                int id = Integer.parseInt(result.split(" - ")[0]);
                Customer c = restaurant.findCustomerById(id);
                table.setStatus("Occupied");
                table.setAssignedCustomerId(id);
                restaurant.updateTable(table);
                refreshTablesGrid();
                NotificationManager.addNotification("Seated " + c.getName() + " at Table " + table.getTableId());
            }
        } 
        else if (choice == 2) {
            table.setStatus("Reserved");
            restaurant.updateTable(table);
            refreshTablesGrid();
            NotificationManager.addNotification("Table " + table.getTableId() + " Reserved");
        } 
        else if (choice == 3) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Table " + table.getTableId() + "?", "Delete Table", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                restaurant.deleteTable(table.getTableId());
                refreshTablesGrid();
                NotificationManager.addNotification("Table " + table.getTableId() + " Deleted");
            }
        }
    }
}
