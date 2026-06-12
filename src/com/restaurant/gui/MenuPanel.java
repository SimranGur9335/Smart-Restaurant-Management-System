package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.FoodItem;
import com.restaurant.model.VegItem;
import com.restaurant.utility.SearchUtility;
import com.restaurant.utility.SortUtility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Menu Panel with search (Linear/Binary) and sort (Bubble/Selection/Insertion) actions.
 * Demonstrates GUI search filtering, sorting integration, and JTable representation.
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

    public MenuPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Menu Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(15, 23, 42));
        JLabel subtitleLabel = new JLabel("Browse foods, search items using linear/binary algorithms, and sort by price");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Control Panel (Top controls inside a card container)
        JPanel controlsPanel = new JPanel(new GridBagLayout());
        controlsPanel.setBackground(Color.WHITE);
        controlsPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.weightx = 1.0;

        // --- Linear Search ---
        gbc.gridy = 0;
        gbc.gridx = 0;
        JLabel lblLinear = new JLabel("Linear Search (Name):");
        lblLinear.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLinear.setForeground(new Color(71, 85, 105));
        controlsPanel.add(lblLinear, gbc);

        gbc.gridx = 1;
        txtSearchName = new JTextField();
        txtSearchName.setPreferredSize(new Dimension(140, 32));
        controlsPanel.add(txtSearchName, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JButton btnLinearSearch = new JButton("Search Name");
        btnLinearSearch.setPreferredSize(new Dimension(110, 32));
        btnLinearSearch.setBackground(new Color(37, 99, 235));
        btnLinearSearch.setForeground(Color.WHITE);
        btnLinearSearch.setFocusPainted(false);
        controlsPanel.add(btnLinearSearch, gbc);

        // --- Binary Search ---
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        JLabel lblBinary = new JLabel("Binary Search (ID):");
        lblBinary.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBinary.setForeground(new Color(71, 85, 105));
        controlsPanel.add(lblBinary, gbc);

        gbc.gridx = 1;
        txtSearchId = new JTextField();
        txtSearchId.setPreferredSize(new Dimension(140, 32));
        controlsPanel.add(txtSearchId, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JButton btnBinarySearch = new JButton("Search ID");
        btnBinarySearch.setPreferredSize(new Dimension(110, 32));
        btnBinarySearch.setBackground(new Color(37, 99, 235));
        btnBinarySearch.setForeground(Color.WHITE);
        btnBinarySearch.setFocusPainted(false);
        controlsPanel.add(btnBinarySearch, gbc);

        // --- Sort Panel ---
        gbc.gridy = 0;
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        JLabel lblSort = new JLabel("Sort Menu by Price:");
        lblSort.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSort.setForeground(new Color(71, 85, 105));
        controlsPanel.add(lblSort, gbc);

        gbc.gridx = 4;
        String[] sortAlgos = {"Bubble Sort", "Selection Sort", "Insertion Sort"};
        cmbSortType = new JComboBox<>(sortAlgos);
        cmbSortType.setPreferredSize(new Dimension(130, 32));
        controlsPanel.add(cmbSortType, gbc);

        // Sort Direction Panel
        gbc.gridx = 5;
        JPanel rdoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        rdoPanel.setBackground(Color.WHITE);
        rdoAsc = new JRadioButton("Low-High", true);
        rdoAsc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rdoAsc.setBackground(Color.WHITE);
        rdoDesc = new JRadioButton("High-Low");
        rdoDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rdoDesc.setBackground(Color.WHITE);
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
        btnSort.setBackground(new Color(22, 163, 74)); // Green theme
        btnSort.setForeground(Color.WHITE);
        btnSort.setFocusPainted(false);
        controlsPanel.add(btnSort, gbc);

        // --- Actions Buttons (Reset) ---
        gbc.gridy = 1;
        gbc.gridx = 6;
        JButton btnReset = new JButton("Reset Menu");
        btnReset.setPreferredSize(new Dimension(100, 32));
        btnReset.setBackground(new Color(100, 116, 139)); // Slate gray
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        controlsPanel.add(btnReset, gbc);

        add(controlsPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        String[] cols = {"Item ID", "Food Type", "Food Name", "Price (₹)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        menuTable = new JTable(tableModel);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menuTable.setRowHeight(32);
        menuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        menuTable.getTableHeader().setBackground(new Color(241, 245, 249));
        menuTable.getTableHeader().setForeground(new Color(71, 85, 105));
        menuTable.setSelectionBackground(new Color(219, 234, 254));
        menuTable.setSelectionForeground(new Color(30, 41, 59));
        menuTable.setShowGrid(true);
        menuTable.setGridColor(new Color(241, 245, 249));

        JScrollPane tableScroll = new JScrollPane(menuTable);
        tableScroll.setBorder(null);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Action Listeners
        btnLinearSearch.addActionListener(e -> runLinearSearchName());
        btnBinarySearch.addActionListener(e -> runBinarySearchId());
        btnSort.addActionListener(e -> runSortingAlgorithms());
        btnReset.addActionListener(e -> {
            txtSearchName.setText("");
            txtSearchId.setText("");
            refreshMenuTable(restaurant.getMenuCopy());
        });

        // Initialize table
        refreshMenuTable(restaurant.getMenuCopy());
    }

    // Refresh display
    private void refreshMenuTable(FoodItem[] list) {
        tableModel.setRowCount(0);
        for (FoodItem item : list) {
            String typeStr = (item instanceof VegItem) ? "Veg" : "Non-Veg";
            tableModel.addRow(new Object[]{
                item.getItemId(),
                typeStr,
                item.getItemName(),
                String.format("₹%.2f", item.getPrice())
            });
        }
    }

    // Executes linear search name
    private void runLinearSearchName() {
        String nameQuery = txtSearchName.getText().trim();
        if (nameQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a food name to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FoodItem item = restaurant.searchFoodItemByName(nameQuery);
        if (item != null) {
            refreshMenuTable(new FoodItem[]{item}); // Render only the matched row
            JOptionPane.showMessageDialog(this, String.format("Item Found!\nName: %s\nPrice: ₹%.2f", item.getItemName(), item.getPrice()), "Search Match", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No food items matching \"" + nameQuery + "\" were found.", "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Executes binary search by ID
    private void runBinarySearchId() {
        String idStr = txtSearchId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a food ID to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            FoodItem item = restaurant.searchFoodItemById(id); // Clones and sorts by ID before binary search inside utility
            if (item != null) {
                refreshMenuTable(new FoodItem[]{item}); // Render only the matched row
                JOptionPane.showMessageDialog(this, String.format("Item Found!\nID: %d\nName: %s\nPrice: ₹%.2f\n\n(Note: List was automatically sorted by ID first inside the search utility)", id, item.getItemName(), item.getPrice()), "Search Match", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No food item with ID " + id + " was found.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid format! Please enter a valid integer for the ID.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Executes sorting logic
    private void runSortingAlgorithms() {
        int algoIndex = cmbSortType.getSelectedIndex(); // 0: Bubble, 1: Selection, 2: Insertion
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
}
