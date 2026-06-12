package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.utility.SalesAnalyzer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Analytics Panel housing daily sales tracking and the Sliding Window visualizer.
 * Demonstrates GUI representations of numerical lists, input forms, and dynamic streams.
 */
public class AnalyticsPanel extends JPanel {
    private Restaurant restaurant;
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JTextField txtAddRevenue;
    private JTextField txtWindowSize;
    private JTextArea txtAreaLogs;

    public AnalyticsPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Sales Analytics Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(15, 23, 42));
        JLabel subtitleLabel = new JLabel("Log daily sales records and visualize consecutive day maximum revenue via Sliding Window");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center split layout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // --- Left Panel: Daily Sales JTable & Add Revenue ---
        JPanel leftPanel = new JPanel(new BorderLayout(12, 12));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        // Add revenue form
        JPanel addSalesForm = new JPanel(new BorderLayout(8, 0));
        addSalesForm.setBackground(Color.WHITE);
        
        JLabel lblAddRev = new JLabel("Add Sales (₹): ");
        lblAddRev.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAddRev.setForeground(new Color(71, 85, 105));
        addSalesForm.add(lblAddRev, BorderLayout.WEST);

        txtAddRevenue = new JTextField();
        txtAddRevenue.setPreferredSize(new Dimension(100, 32));
        addSalesForm.add(txtAddRevenue, BorderLayout.CENTER);

        JButton btnAddSales = new JButton("Add Record");
        btnAddSales.setBackground(new Color(37, 99, 235));
        btnAddSales.setForeground(Color.WHITE);
        btnAddSales.setPreferredSize(new Dimension(110, 32));
        btnAddSales.setFocusPainted(false);
        addSalesForm.add(btnAddSales, BorderLayout.EAST);

        leftPanel.add(addSalesForm, BorderLayout.NORTH);

        // Sales Table
        String[] cols = {"Day Number", "Daily Revenue (₹)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        salesTable = new JTable(tableModel);
        salesTable.setRowHeight(26);
        salesTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        salesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        salesTable.getTableHeader().setBackground(new Color(241, 245, 249));
        salesTable.setShowGrid(true);
        salesTable.setGridColor(new Color(241, 245, 249));
        
        JScrollPane tableScroll = new JScrollPane(salesTable);
        tableScroll.setBorder(null);
        leftPanel.add(tableScroll, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.weightx = 0.40;
        centerPanel.add(leftPanel, gbc);

        // Spacing
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacer = new JPanel();
        spacer.setBackground(getBackground());
        centerPanel.add(spacer, gbc);

        // --- Right Panel: Sliding Window Visualizer ---
        JPanel rightPanel = new JPanel(new BorderLayout(12, 12));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        // Input controls for visualizer
        JPanel algosControls = new JPanel(new BorderLayout(8, 0));
        algosControls.setBackground(Color.WHITE);

        JLabel lblWindow = new JLabel("Consecutive Days (K): ");
        lblWindow.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblWindow.setForeground(new Color(71, 85, 105));
        algosControls.add(lblWindow, BorderLayout.WEST);

        txtWindowSize = new JTextField();
        txtWindowSize.setPreferredSize(new Dimension(80, 32));
        algosControls.add(txtWindowSize, BorderLayout.CENTER);

        JButton btnAnalyze = new JButton("Run Sliding Window");
        btnAnalyze.setBackground(new Color(22, 163, 74));
        btnAnalyze.setForeground(Color.WHITE);
        btnAnalyze.setPreferredSize(new Dimension(170, 32));
        btnAnalyze.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAnalyze.setFocusPainted(false);
        algosControls.add(btnAnalyze, BorderLayout.EAST);

        rightPanel.add(algosControls, BorderLayout.NORTH);

        // Logs text area for transitions output
        txtAreaLogs = new JTextArea();
        txtAreaLogs.setEditable(false);
        txtAreaLogs.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtAreaLogs.setForeground(new Color(15, 23, 42)); // Slate-900
        txtAreaLogs.setBackground(new Color(248, 250, 252));
        txtAreaLogs.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(8, 8, 8, 8)
        ));
        txtAreaLogs.setText("--- Sliding Window Execution Console ---\n" +
                            "Configure consecutive days 'K' and click run to visualize transition metrics here.");

        JScrollPane logScroll = new JScrollPane(txtAreaLogs);
        logScroll.setBorder(null);
        rightPanel.add(logScroll, BorderLayout.CENTER);

        gbc.gridx = 2;
        gbc.weightx = 0.58;
        centerPanel.add(rightPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Actions
        btnAddSales.addActionListener(e -> handleAddSalesRecord());
        btnAnalyze.addActionListener(e -> handleAnalyzeSales());

        // Fill Table
        refreshSalesTable();
    }

    public void refreshSalesTable() {
        tableModel.setRowCount(0);
        double[] sales = restaurant.getDailySales();
        for (int i = 0; i < sales.length; i++) {
            tableModel.addRow(new Object[]{
                "Day " + (i + 1),
                String.format("₹%.2f", sales[i])
            });
        }
    }

    private void handleAddSalesRecord() {
        String inputStr = txtAddRevenue.getText().trim();
        if (inputStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a sales amount.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(inputStr);
            if (amount < 0.0) {
                JOptionPane.showMessageDialog(this, "Sales revenue cannot be negative.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            restaurant.addSalesRecord(amount);
            txtAddRevenue.setText("");
            refreshSalesTable();
            JOptionPane.showMessageDialog(this, String.format("Registered day %d sales: ₹%.2f", restaurant.getSalesCount(), amount), "Record Logged", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format. Please enter a valid decimal number.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAnalyzeSales() {
        String kStr = txtWindowSize.getText().trim();
        if (kStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please specify consecutive days size (K).", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int k = Integer.parseInt(kStr);
            double[] sales = restaurant.getDailySales();
            int size = restaurant.getSalesCount();

            // Redirect Stdout stream temporarily to capture print outs of window slides
            PrintStream originalOut = System.out;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream interceptor = new PrintStream(baos);
            
            System.setOut(interceptor);
            
            try {
                double maxRev = SalesAnalyzer.findMaxRevenueConsecutiveDays(sales, size, k);
                
                System.out.flush();
                System.setOut(originalOut);
                
                // Render console log outputs in GUI JTextArea
                txtAreaLogs.setText(baos.toString());
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Sliding Window Complete!\nMaximum Consecutive Revenue: ₹%.2f", maxRev), 
                    "Analysis Result", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IllegalArgumentException ex) {
                System.out.flush();
                System.setOut(originalOut);
                txtAreaLogs.setText("Execution Error: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Analytics Error: " + ex.getMessage(), "Algorithm Bounds", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Window size (K) must be a valid integer.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
