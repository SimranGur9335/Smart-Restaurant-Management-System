package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Order;
import com.restaurant.model.Ingredient;
import com.restaurant.model.Employee;
import com.restaurant.model.Attendance;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Report Generation and Export panel.
 * Provides views for Sales, Orders, Inventory, and Staff logs.
 * Exports summaries in standard CSV format.
 */
public class ReportsPanel extends JPanel {
    private Restaurant restaurant;
    private JComboBox<String> cmbReportType;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextArea summaryLabel;

    public ReportsPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(24, 24));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Restaurant Operations Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Review restaurant metrics and export database segments as CSV spreadsheet documents");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        controlPanel.setBackground(Theme.getPanelBg());
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));

        JLabel lblSel = new JLabel("Select Report Type: ");
        lblSel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSel.setForeground(Theme.getFg());
        controlPanel.add(lblSel);

        String[] reports = {"Sales Revenue", "Placed Orders", "Pantry Inventory", "Staff Attendance Logs"};
        cmbReportType = new JComboBox<>(reports);
        cmbReportType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbReportType.setPreferredSize(new Dimension(200, 32));
        controlPanel.add(cmbReportType);

        JButton btnGenerate = new JButton("Generate Report");
        btnGenerate.setBackground(Theme.getPrimaryBlue());
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setPreferredSize(new Dimension(140, 32));
        btnGenerate.setFocusPainted(false);
        controlPanel.add(btnGenerate);

        JButton btnExport = new JButton("Export to CSV");
        btnExport.setBackground(Theme.getPrimaryGreen());
        btnExport.setForeground(Color.WHITE);
        btnExport.setPreferredSize(new Dimension(140, 32));
        btnExport.setFocusPainted(false);
        controlPanel.add(btnExport);

        add(controlPanel, BorderLayout.NORTH);

        // Center Content Card (Preview grid and metadata details)
        JPanel previewCard = new JPanel(new BorderLayout(12, 12));
        previewCard.setBackground(Theme.getPanelBg());
        previewCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        summaryLabel = new JTextArea("Click 'Generate Report' to populate operational statistics.");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        summaryLabel.setEditable(false);
        summaryLabel.setLineWrap(true);
        summaryLabel.setWrapStyleWord(true);
        summaryLabel.setBackground(Theme.getBg());
        summaryLabel.setForeground(Theme.getSubFg());
        summaryLabel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        previewCard.add(summaryLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reportTable.setRowHeight(30);
        reportTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        reportTable.getTableHeader().setBackground(new Color(241, 245, 249));
        reportTable.setSelectionBackground(Theme.getTableSelectionBg());
        reportTable.setShowGrid(true);
        reportTable.setGridColor(Theme.getBorderColor());

        JScrollPane scroll = new JScrollPane(reportTable);
        scroll.setBorder(null);
        previewCard.add(scroll, BorderLayout.CENTER);

        add(previewCard, BorderLayout.CENTER);

        // Events
        btnGenerate.addActionListener(e -> generateReport());
        btnExport.addActionListener(e -> exportToCSV());

        // Default load
        generateReport();
    }

    private void generateReport() {
        int reportIndex = cmbReportType.getSelectedIndex();
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);

        if (reportIndex == 0) {
            // Sales Revenue
            tableModel.addColumn("Day number");
            tableModel.addColumn("Logged Sales amount (₹)");
            
            double[] sales = restaurant.getDailySales();
            double total = 0.0;
            for (int i = 0; i < sales.length; i++) {
                tableModel.addRow(new Object[]{"Day " + (i + 1), String.format("₹%.2f", sales[i])});
                total += sales[i];
            }
            summaryLabel.setText(String.format("Sales Summary Overview\nTotal recorded days: %d | Net sales revenue generated: ₹%.2f", sales.length, total));
        } 
        else if (reportIndex == 1) {
            // Placed Orders
            tableModel.addColumn("Order ID");
            tableModel.addColumn("Customer Name");
            tableModel.addColumn("Type");
            tableModel.addColumn("Table Allocated");
            tableModel.addColumn("Order Status");
            tableModel.addColumn("Bill amount (₹)");
            tableModel.addColumn("Paid status");

            Order[] orders = restaurant.getOrdersCopy();
            double totalSales = 0.0;
            int countPaid = 0;
            for (Order o : orders) {
                tableModel.addRow(new Object[]{
                    o.getOrderId(),
                    o.getCustomer().getName(),
                    o.getOrderType(),
                    o.getTableId() == -1 ? "N/A" : "Table " + o.getTableId(),
                    o.getOrderStatus(),
                    String.format("₹%.2f", o.getGrandTotal()),
                    o.isPaid() ? "Paid" : "Unpaid"
                });
                if (o.isPaid()) {
                    totalSales += o.getGrandTotal();
                    countPaid++;
                }
            }
            summaryLabel.setText(String.format("Orders Directory Overview\nTotal Orders Placed: %d | Paid Transactions: %d | Net paid collection: ₹%.2f", orders.length, countPaid, totalSales));
        } 
        else if (reportIndex == 2) {
            // Pantry Inventory
            tableModel.addColumn("Item ID");
            tableModel.addColumn("Ingredient Name");
            tableModel.addColumn("Current Stock");
            tableModel.addColumn("Min Stock Limit");
            tableModel.addColumn("Expiry Date");
            tableModel.addColumn("Status");

            Ingredient[] list = restaurant.getInventoryCopy();
            int lowCount = 0;
            for (Ingredient ing : list) {
                boolean isLow = ing.isLowStock();
                tableModel.addRow(new Object[]{
                    ing.getIngredientId(),
                    ing.getName(),
                    ing.getQuantity() + " " + ing.getUnit(),
                    ing.getMinimumStock() + " " + ing.getUnit(),
                    ing.getExpiryDate(),
                    isLow ? "LOW STOCK" : "Normal"
                });
                if (isLow) lowCount++;
            }
            summaryLabel.setText(String.format("Pantry Stock Levels Summary\nTotal Inventory Items: %d | Ingredients requiring reorder: %d", list.length, lowCount));
        } 
        else if (reportIndex == 3) {
            // Staff Attendance Logs
            tableModel.addColumn("Date logged");
            tableModel.addColumn("Employee ID");
            tableModel.addColumn("Name");
            tableModel.addColumn("Role Position");
            tableModel.addColumn("Clock In");
            tableModel.addColumn("Clock Out");
            tableModel.addColumn("Status");

            Attendance[] attList = restaurant.getAttendanceCopy();
            Employee[] staffList = restaurant.getStaffCopy();
            
            for (Attendance a : attList) {
                String name = "Unknown";
                String role = "Staff";
                for (Employee e : staffList) {
                    if (e.getEmployeeId() == a.getEmployeeId()) {
                        name = e.getName();
                        role = e.getRole();
                        break;
                    }
                }
                tableModel.addRow(new Object[]{
                    a.getDate(),
                    a.getEmployeeId(),
                    name,
                    role,
                    a.getClockInTime(),
                    a.getClockOutTime(),
                    a.isPresent() ? "Present" : "Absent"
                });
            }
            summaryLabel.setText(String.format("Staff Clock Registers\nTotal logged attendance sheets: %d", attList.length));
        }
    }

    public void refreshSummary() {
        generateReport();
    }

    private void exportToCSV() {
        int reportIndex = cmbReportType.getSelectedIndex();
        String reportName = cmbReportType.getSelectedItem().toString().replace(" ", "_").toLowerCase();
        
        File reportsDir = new File("reports");
        reportsDir.mkdirs();
        
        File csvFile = new File(reportsDir, reportName + "_report.csv");
        
        try (FileWriter csvWriter = new FileWriter(csvFile)) {
            // Write column headers
            int colCount = tableModel.getColumnCount();
            for (int i = 0; i < colCount; i++) {
                csvWriter.append(tableModel.getColumnName(i));
                if (i < colCount - 1) csvWriter.append(",");
            }
            csvWriter.append("\n");

            // Write row data
            int rowCount = tableModel.getRowCount();
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < colCount; col++) {
                    Object val = tableModel.getValueAt(row, col);
                    String valStr = val != null ? val.toString().replace(",", " ") : "";
                    csvWriter.append(valStr);
                    if (col < colCount - 1) csvWriter.append(",");
                }
                csvWriter.append("\n");
            }

            JOptionPane.showMessageDialog(this, 
                String.format("Report successfully exported to:\n%s", csvFile.getAbsolutePath()), 
                "Export Successful", 
                JOptionPane.INFORMATION_MESSAGE
            );

            NotificationManager.addNotification(cmbReportType.getSelectedItem() + " exported as CSV!");
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to export report: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
