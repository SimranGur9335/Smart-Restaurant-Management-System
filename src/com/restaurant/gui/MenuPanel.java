package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.FoodItem;
import com.restaurant.model.VegItem;
import com.restaurant.model.NonVegItem;
import com.restaurant.utility.Theme;
import com.restaurant.utility.SearchUtility;
import com.restaurant.utility.SortUtility;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Menu Panel with search (Linear/Binary), sorting algorithms, category filters, and CRUD controls.
 */
public class MenuPanel extends JPanel {
    private Restaurant restaurant;
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearchName;
    private JTextField txtSearchId;
    private JComboBox<String> cmbSortType;
    private JRadioButton rdoAsc;
    private JRadioButton rdoDesc;

    // Filters
    private JComboBox<String> cmbCategoryFilter;
    private JCheckBox chkAvailableOnly;

    public MenuPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Menu Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Browse food recipes, search by index, and manage dishes in the inventory card");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Control Panel
        JPanel controlsPanel = new JPanel(new GridBagLayout());
        controlsPanel.setBackground(Theme.getPanelBg());
        controlsPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.weightx = 1.0;

        // --- Row 1: Search Name & ID ---
        gbc.gridy = 0;
        gbc.gridx = 0;
        JLabel lblLinear = new JLabel("Linear Search (Name):");
        lblLinear.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLinear.setForeground(Theme.getSubFg());
        controlsPanel.add(lblLinear, gbc);

        gbc.gridx = 1;
        txtSearchName = new JTextField();
        txtSearchName.setPreferredSize(new Dimension(140, 32));
        controlsPanel.add(txtSearchName, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JButton btnLinearSearch = new JButton("Search Name");
        btnLinearSearch.setPreferredSize(new Dimension(110, 32));
        btnLinearSearch.setBackground(Theme.getPrimaryBlue());
        btnLinearSearch.setForeground(Color.WHITE);
        btnLinearSearch.setFocusPainted(false);
        controlsPanel.add(btnLinearSearch, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        JLabel lblBinary = new JLabel("Binary Search (ID):");
        lblBinary.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBinary.setForeground(Theme.getSubFg());
        controlsPanel.add(lblBinary, gbc);

        gbc.gridx = 1;
        txtSearchId = new JTextField();
        txtSearchId.setPreferredSize(new Dimension(140, 32));
        controlsPanel.add(txtSearchId, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JButton btnBinarySearch = new JButton("Search ID");
        btnBinarySearch.setPreferredSize(new Dimension(110, 32));
        btnBinarySearch.setBackground(Theme.getPrimaryBlue());
        btnBinarySearch.setForeground(Color.WHITE);
        btnBinarySearch.setFocusPainted(false);
        controlsPanel.add(btnBinarySearch, gbc);

        // --- Row 2: Sort Controls ---
        gbc.gridy = 0;
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        JLabel lblSort = new JLabel("Sort Menu by Price:");
        lblSort.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSort.setForeground(Theme.getSubFg());
        controlsPanel.add(lblSort, gbc);

        gbc.gridx = 4;
        String[] sortAlgos = {"Bubble Sort", "Selection Sort", "Insertion Sort"};
        cmbSortType = new JComboBox<>(sortAlgos);
        cmbSortType.setPreferredSize(new Dimension(130, 32));
        controlsPanel.add(cmbSortType, gbc);

        gbc.gridx = 5;
        JPanel rdoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        rdoPanel.setBackground(Theme.getPanelBg());
        rdoAsc = new JRadioButton("Low-High", true);
        rdoAsc.setBackground(Theme.getPanelBg());
        rdoAsc.setForeground(Theme.getFg());
        rdoDesc = new JRadioButton("High-Low");
        rdoDesc.setBackground(Theme.getPanelBg());
        rdoDesc.setForeground(Theme.getFg());
        ButtonGroup sortGrp = new ButtonGroup();
        sortGrp.add(rdoAsc);
        sortGrp.add(rdoDesc);
        rdoPanel.add(rdoAsc);
        rdoPanel.add(rdoDesc);
        controlsPanel.add(rdoPanel, gbc);

        gbc.gridx = 6;
        gbc.weightx = 0.0;
        JButton btnSort = new JButton("Sort Menu");
        btnSort.setPreferredSize(new Dimension(100, 32));
        btnSort.setBackground(Theme.getPrimaryGreen());
        btnSort.setForeground(Color.WHITE);
        btnSort.setFocusPainted(false);
        controlsPanel.add(btnSort, gbc);

        // --- Row 3: Category Filtering & CRUD buttons ---
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        JLabel lblFilter = new JLabel("Category Filter:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFilter.setForeground(Theme.getSubFg());
        controlsPanel.add(lblFilter, gbc);

        gbc.gridx = 1;
        String[] filterCats = {"All", "Starters", "Main Course", "Beverages", "Desserts"};
        cmbCategoryFilter = new JComboBox<>(filterCats);
        cmbCategoryFilter.setPreferredSize(new Dimension(140, 32));
        controlsPanel.add(cmbCategoryFilter, gbc);

        gbc.gridx = 2;
        chkAvailableOnly = new JCheckBox("Available Only");
        chkAvailableOnly.setBackground(Theme.getPanelBg());
        chkAvailableOnly.setForeground(Theme.getFg());
        controlsPanel.add(chkAvailableOnly, gbc);

        // RESET
        gbc.gridy = 1;
        gbc.gridx = 6;
        JButton btnReset = new JButton("Reset Card");
        btnReset.setPreferredSize(new Dimension(100, 32));
        btnReset.setBackground(new Color(100, 116, 139));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        controlsPanel.add(btnReset, gbc);

        add(controlsPanel, BorderLayout.NORTH);

        // Center Table Area
        JPanel tablePanel = new JPanel(new BorderLayout(12, 12));
        tablePanel.setBackground(Theme.getPanelBg());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        String[] cols = {"Item ID", "Food Type", "Dishes Name", "Category", "Price", "Availability", "Description"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        menuTable = new JTable(tableModel);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menuTable.setRowHeight(30);
        menuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuTable.getTableHeader().setBackground(new Color(241, 245, 249));
        menuTable.setSelectionBackground(Theme.getTableSelectionBg());
        menuTable.setShowGrid(true);
        menuTable.setGridColor(Theme.getBorderColor());

        JScrollPane tableScroll = new JScrollPane(menuTable);
        tableScroll.setBorder(null);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // Table Bottom: CRUD trigger buttons
        JPanel crudRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        crudRow.setBackground(Theme.getPanelBg());
        
        JButton btnAdd = new JButton("Add Dish");
        btnAdd.setBackground(Theme.getPrimaryBlue());
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        
        JButton btnEdit = new JButton("Edit Dish");
        btnEdit.setBackground(Theme.getPrimaryGreen());
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        
        JButton btnDelete = new JButton("Delete Dish");
        btnDelete.setBackground(Theme.getPrimaryRed());
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        crudRow.add(btnAdd);
        crudRow.add(btnEdit);
        crudRow.add(btnDelete);
        tablePanel.add(crudRow, BorderLayout.SOUTH);

        add(tablePanel, BorderLayout.CENTER);

        // Action Triggers
        btnLinearSearch.addActionListener(e -> runLinearSearchName());
        btnBinarySearch.addActionListener(e -> runBinarySearchId());
        btnSort.addActionListener(e -> runSortingAlgorithms());
        
        cmbCategoryFilter.addActionListener(e -> applyFilters());
        chkAvailableOnly.addActionListener(e -> applyFilters());

        btnReset.addActionListener(e -> {
            txtSearchName.setText("");
            txtSearchId.setText("");
            cmbCategoryFilter.setSelectedIndex(0);
            chkAvailableOnly.setSelected(false);
            refreshMenuTable(restaurant.getMenuCopy());
        });

        btnAdd.addActionListener(e -> triggerAddEditDialog(null));
        btnEdit.addActionListener(e -> {
            int row = menuTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a dish from the menu table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            FoodItem item = restaurant.searchFoodItemById(id);
            triggerAddEditDialog(item);
        });
        btnDelete.addActionListener(e -> handleDeleteDish());

        // Initial table load
        refreshMenuTable(restaurant.getMenuCopy());
    }

    private void refreshMenuTable(FoodItem[] list) {
        tableModel.setRowCount(0);
        for (FoodItem item : list) {
            String typeStr = (item instanceof VegItem) ? "Veg" : "Non-Veg";
            tableModel.addRow(new Object[]{
                item.getItemId(),
                typeStr,
                item.getItemName(),
                item.getCategory(),
                String.format("₹%.2f", item.getPrice()),
                item.isAvailable() ? "In Stock" : "Sold Out",
                item.getDescription()
            });
        }
    }

    private void applyFilters() {
        String catFilter = (String) cmbCategoryFilter.getSelectedItem();
        boolean availOnly = chkAvailableOnly.isSelected();

        FoodItem[] fullList = restaurant.getMenuCopy();
        ArrayList<FoodItem> filtered = new ArrayList<>();
        
        for (FoodItem item : fullList) {
            boolean catMatch = catFilter.equalsIgnoreCase("All") || item.getCategory().equalsIgnoreCase(catFilter);
            boolean availMatch = !availOnly || item.isAvailable();
            
            if (catMatch && availMatch) {
                filtered.add(item);
            }
        }
        refreshMenuTable(filtered.toArray(new FoodItem[0]));
    }

    private void runLinearSearchName() {
        String nameQuery = txtSearchName.getText().trim();
        if (nameQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a food name to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FoodItem item = restaurant.searchFoodItemByName(nameQuery);
        if (item != null) {
            refreshMenuTable(new FoodItem[]{item});
            JOptionPane.showMessageDialog(this, String.format("Item Found!\nName: %s\nCategory: %s\nPrice: ₹%.2f", item.getItemName(), item.getCategory(), item.getPrice()), "Search Match", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No food items matching \"" + nameQuery + "\" were found.", "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void runBinarySearchId() {
        String idStr = txtSearchId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a food ID to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            FoodItem item = restaurant.searchFoodItemById(id);
            if (item != null) {
                refreshMenuTable(new FoodItem[]{item});
                JOptionPane.showMessageDialog(this, String.format("Item Found!\nID: %d\nName: %s\nCategory: %s\nPrice: ₹%.2f", id, item.getItemName(), item.getCategory(), item.getPrice()), "Search Match", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No food item with ID " + id + " was found.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid format! Please enter a valid integer for the ID.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runSortingAlgorithms() {
        int algoIndex = cmbSortType.getSelectedIndex();
        boolean asc = rdoAsc.isSelected();

        FoodItem[] copy = restaurant.getMenuCopy();
        int size = restaurant.getMenuCount();

        long start = System.nanoTime();
        switch (algoIndex) {
            case 0:
                SortUtility.bubbleSortByPrice(copy, size, asc);
                break;
            case 1:
                SortUtility.selectionSortByPrice(copy, size, asc);
                break;
            case 2:
                SortUtility.insertionSortByPrice(copy, size, asc);
                break;
        }
        long durationNano = System.nanoTime() - start;
        double durationMs = durationNano / 1_000_000.0;

        refreshMenuTable(copy);

        String orderStr = asc ? "Ascending (Low to High)" : "Descending (High to Low)";
        String algoName = cmbSortType.getSelectedItem().toString();
        JOptionPane.showMessageDialog(this, 
            String.format("Menu sorted successfully!\nAlgorithm: %s\nDirection: %s\nExecution Time: %.4f ms", algoName, orderStr, durationMs), 
            "Sort Statistics", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleDeleteDish() {
        int row = menuTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a dish from the menu table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            restaurant.deleteFoodItem(id);
            refreshMenuTable(restaurant.getMenuCopy());
            NotificationManager.addNotification("Dish " + name + " deleted.");
        }
    }

    private void triggerAddEditDialog(FoodItem editItem) {
        boolean isEdit = editItem != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isEdit ? "Modify Menu Dish" : "Register Menu Dish", true);
        dlg.setSize(380, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(Theme.getPanelBg());
        pnl.setBorder(new EmptyBorder(16, 20, 16, 20));
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.insets = new Insets(4, 0, 4, 0);
        fgbc.weightx = 1.0;
        fgbc.gridx = 0;

        // ID
        fgbc.gridy = 0;
        pnl.add(new JLabel("Dish Item ID (Integer) *"), fgbc);
        JTextField txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(0, 30));
        if (isEdit) {
            txtId.setText(String.valueOf(editItem.getItemId()));
            txtId.setEnabled(false); // ID locked on edit
        }
        fgbc.gridy = 1;
        pnl.add(txtId, fgbc);

        // Name
        fgbc.gridy = 2;
        pnl.add(new JLabel("Dish Name *"), fgbc);
        JTextField txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(0, 30));
        if (isEdit) txtName.setText(editItem.getItemName());
        fgbc.gridy = 3;
        pnl.add(txtName, fgbc);

        // Price
        fgbc.gridy = 4;
        pnl.add(new JLabel("Dish Price (₹) *"), fgbc);
        JTextField txtPrice = new JTextField();
        txtPrice.setPreferredSize(new Dimension(0, 30));
        if (isEdit) txtPrice.setText(String.valueOf(editItem.getPrice()));
        fgbc.gridy = 5;
        pnl.add(txtPrice, fgbc);

        // Description
        fgbc.gridy = 6;
        pnl.add(new JLabel("Dishes recipe notes / description"), fgbc);
        JTextField txtDesc = new JTextField();
        txtDesc.setPreferredSize(new Dimension(0, 30));
        if (isEdit) txtDesc.setText(editItem.getDescription());
        fgbc.gridy = 7;
        pnl.add(txtDesc, fgbc);

        // Category dropdown
        fgbc.gridy = 8;
        pnl.add(new JLabel("Category Group *"), fgbc);
        String[] cats = {"Starters", "Main Course", "Beverages", "Desserts"};
        JComboBox<String> cmbCat = new JComboBox<>(cats);
        cmbCat.setPreferredSize(new Dimension(0, 30));
        if (isEdit) cmbCat.setSelectedItem(editItem.getCategory());
        fgbc.gridy = 9;
        pnl.add(cmbCat, fgbc);

        // Type
        fgbc.gridy = 10;
        pnl.add(new JLabel("Dish Type *"), fgbc);
        String[] types = {"Vegetarian", "Non-Vegetarian"};
        JComboBox<String> cmbType = new JComboBox<>(types);
        cmbType.setPreferredSize(new Dimension(0, 30));
        if (isEdit) cmbType.setSelectedItem((editItem instanceof VegItem) ? "Vegetarian" : "Non-Vegetarian");
        fgbc.gridy = 11;
        pnl.add(cmbType, fgbc);

        // Availability checkbox
        fgbc.gridy = 12;
        JCheckBox chkAvail = new JCheckBox("Available in stock", true);
        chkAvail.setBackground(Theme.getPanelBg());
        chkAvail.setForeground(Theme.getFg());
        if (isEdit) chkAvail.setSelected(editItem.isAvailable());
        pnl.add(chkAvail, fgbc);

        // Action row
        JPanel actPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actPanel.setBackground(Theme.getPanelBg());
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(100, 116, 139));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(e -> dlg.dispose());

        JButton btnSubmit = new JButton(isEdit ? "Update Dish" : "Save Dish");
        btnSubmit.setBackground(Theme.getPrimaryBlue());
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.addActionListener(e -> {
            String idVal = txtId.getText().trim();
            String nameVal = txtName.getText().trim();
            String priceVal = txtPrice.getText().trim();
            String descVal = txtDesc.getText().trim();
            String catVal = (String) cmbCat.getSelectedItem();
            String typeVal = (String) cmbType.getSelectedItem();
            boolean availVal = chkAvail.isSelected();

            if (idVal.isEmpty() || nameVal.isEmpty() || priceVal.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "ID, Name, and Price are required.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idVal);
                double price = Double.parseDouble(priceVal);

                if (!isEdit) {
                    FoodItem test = restaurant.searchFoodItemById(id);
                    if (test != null) {
                        JOptionPane.showMessageDialog(dlg, "Dish ID already exists.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                FoodItem item;
                if ("Vegetarian".equalsIgnoreCase(typeVal)) {
                    item = new VegItem(id, nameVal, price, descVal, catVal, availVal, "");
                } else {
                    item = new NonVegItem(id, nameVal, price, descVal, catVal, availVal, "");
                }

                if (isEdit) {
                    restaurant.updateFoodItem(item);
                    NotificationManager.addNotification("Dish " + nameVal + " updated!");
                } else {
                    restaurant.addFoodItem(item);
                    NotificationManager.addNotification("New dish " + nameVal + " registered!");
                }

                dlg.dispose();
                refreshMenuTable(restaurant.getMenuCopy());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "ID must be integer and Price must be valid decimal.", "Format Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        actPanel.add(btnCancel);
        actPanel.add(btnSubmit);
        
        fgbc.gridy = 13;
        fgbc.insets = new Insets(16, 0, 0, 0);
        pnl.add(actPanel, fgbc);

        dlg.add(pnl, BorderLayout.CENTER);
        dlg.setVisible(true);
    }
}
