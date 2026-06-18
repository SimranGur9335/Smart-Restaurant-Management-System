package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Reservation;
import com.restaurant.model.Table;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Reservation Booking management panel.
 * Contains forms to book customer tables and list reservation logs.
 */
public class ReservationPanel extends JPanel {
    private Restaurant restaurant;
    private JTextField txtCustomerName;
    private JTextField txtDate;
    private JTextField txtTime;
    private JSpinner spinGuests;
    private JComboBox<String> cmbTableSelect;
    private JTable resTable;
    private DefaultTableModel tableModel;

    public ReservationPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(24, 24));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Reservation Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Log table bookings, allocate dining seats, and track reservations");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Main Grid split: Form (Left), Logs (Right)
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // --- Left Panel: Book Form ---
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

        JLabel formTitle = new JLabel("Book Reservation");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(Theme.getFg());
        fgbc.gridy = 0;
        formPanel.add(formTitle, fgbc);

        JSeparator separator = new JSeparator();
        separator.setForeground(Theme.getBorderColor());
        fgbc.gridy = 1;
        formPanel.add(separator, fgbc);

        // Cust Name
        JLabel lblName = new JLabel("Customer Name *");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblName.setForeground(Theme.getSubFg());
        fgbc.gridy = 2;
        formPanel.add(lblName, fgbc);

        txtCustomerName = new JTextField();
        txtCustomerName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCustomerName.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 3;
        formPanel.add(txtCustomerName, fgbc);

        // Date
        JLabel lblDate = new JLabel("Booking Date (YYYY-MM-DD) *");
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDate.setForeground(Theme.getSubFg());
        fgbc.gridy = 4;
        formPanel.add(lblDate, fgbc);

        txtDate = new JTextField();
        txtDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDate.setPreferredSize(new Dimension(0, 32));
        txtDate.setText(java.time.LocalDate.now().toString());
        fgbc.gridy = 5;
        formPanel.add(txtDate, fgbc);

        // Time
        JLabel lblTime = new JLabel("Booking Time (HH:MM) *");
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTime.setForeground(Theme.getSubFg());
        fgbc.gridy = 6;
        formPanel.add(lblTime, fgbc);

        txtTime = new JTextField();
        txtTime.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTime.setPreferredSize(new Dimension(0, 32));
        txtTime.setText("19:00");
        fgbc.gridy = 7;
        formPanel.add(txtTime, fgbc);

        // Guests
        JLabel lblGuests = new JLabel("Number of Guests *");
        lblGuests.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblGuests.setForeground(Theme.getSubFg());
        fgbc.gridy = 8;
        formPanel.add(lblGuests, fgbc);

        spinGuests = new JSpinner(new SpinnerNumberModel(2, 1, 30, 1));
        spinGuests.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinGuests.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 9;
        formPanel.add(spinGuests, fgbc);

        // Tables List selector
        JLabel lblTable = new JLabel("Allocate Table *");
        lblTable.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTable.setForeground(Theme.getSubFg());
        fgbc.gridy = 10;
        formPanel.add(lblTable, fgbc);

        cmbTableSelect = new JComboBox<>();
        cmbTableSelect.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTableSelect.setPreferredSize(new Dimension(0, 32));
        fgbc.gridy = 11;
        formPanel.add(cmbTableSelect, fgbc);

        // Button submit
        JButton btnSubmit = new JButton("Confirm Booking");
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

        btnSubmit.addActionListener(e -> handleBookReservation());

        gbc.gridx = 0;
        gbc.weightx = 0.35;
        mainContent.add(formPanel, gbc);

        // Spacing
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacing = new JPanel();
        spacing.setBackground(getBackground());
        mainContent.add(spacing, gbc);

        // --- Right Panel: Logs registry JTable ---
        JPanel tablePanel = new JPanel(new BorderLayout(12, 12));
        tablePanel.setBackground(Theme.getPanelBg());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel tableTitle = new JLabel("Reservation Logs");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(Theme.getFg());
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Customer", "Date", "Time", "Guests", "Table Allocation", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        resTable = new JTable(tableModel);
        resTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resTable.setRowHeight(30);
        resTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        resTable.getTableHeader().setBackground(new Color(241, 245, 249));
        resTable.setSelectionBackground(Theme.getTableSelectionBg());
        resTable.setShowGrid(true);
        resTable.setGridColor(Theme.getBorderColor());

        JScrollPane tableScroll = new JScrollPane(resTable);
        tableScroll.setBorder(null);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // Bottom action buttons
        JPanel rowActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        rowActions.setBackground(Theme.getPanelBg());
        
        JButton btnConfirm = new JButton("Confirm Booking");
        btnConfirm.setBackground(Theme.getPrimaryGreen());
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        
        JButton btnCancel = new JButton("Cancel Booking");
        btnCancel.setBackground(Theme.getPrimaryRed());
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        rowActions.add(btnConfirm);
        rowActions.add(btnCancel);
        tablePanel.add(rowActions, BorderLayout.SOUTH);

        btnConfirm.addActionListener(e -> updateStatus("Confirmed"));
        btnCancel.addActionListener(e -> updateStatus("Cancelled"));

        gbc.gridx = 2;
        gbc.weightx = 0.63;
        mainContent.add(tablePanel, gbc);

        add(mainContent, BorderLayout.CENTER);

        refreshTable();
        refreshTablesList();
    }

    public void refreshTablesList() {
        cmbTableSelect.removeAllItems();
        Table[] list = restaurant.getTablesCopy();
        for (Table t : list) {
            if (t.getStatus().equalsIgnoreCase("Available")) {
                cmbTableSelect.addItem(String.format("Table %d (Cap: %d)", t.getTableId(), t.getCapacity()));
            }
        }
    }

    private void handleBookReservation() {
        String name = txtCustomerName.getText().trim();
        String date = txtDate.getText().trim();
        String time = txtTime.getText().trim();
        int guests = (int) spinGuests.getValue();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all booking fields.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cmbTableSelect.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No available tables to allocate.", "Booking Failed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tableStr = (String) cmbTableSelect.getSelectedItem();
        int tableId = Integer.parseInt(tableStr.split(" ")[1]);

        // Allocate table in database
        Table tbl = restaurant.findTableById(tableId);
        if (tbl != null) {
            tbl.setStatus("Reserved");
            restaurant.updateTable(tbl);
        }

        restaurant.createReservation(name, date, time, guests, tableId);
        
        txtCustomerName.setText("");
        txtDate.setText(java.time.LocalDate.now().toString());
        txtTime.setText("19:00");
        spinGuests.setValue(2);
        
        refreshTable();
        refreshTablesList();
        
        NotificationManager.addNotification("New booking confirmed for " + name + "!");
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        Reservation[] list = restaurant.getReservationsCopy();
        for (Reservation r : list) {
            tableModel.addRow(new Object[]{
                r.getReservationId(),
                r.getCustomerName(),
                r.getDate(),
                r.getTime(),
                r.getGuests(),
                "Table #" + r.getTableId(),
                r.getStatus()
            });
        }
    }

    private void updateStatus(String newStatus) {
        int row = resTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a booking record from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resId = (int) tableModel.getValueAt(row, 0);
        Reservation[] list = restaurant.getReservationsCopy();
        for (Reservation r : list) {
            if (r.getReservationId() == resId) {
                // If moving away from confirmed or cancel, check table occupancy
                if (newStatus.equalsIgnoreCase("Cancelled")) {
                    Table tbl = restaurant.findTableById(r.getTableId());
                    if (tbl != null) {
                        tbl.setStatus("Available");
                        restaurant.updateTable(tbl);
                    }
                }
                r.setStatus(newStatus);
                restaurant.updateReservation(r);
                break;
            }
        }
        refreshTable();
        refreshTablesList();
        NotificationManager.addNotification("Reservation status changed to " + newStatus);
    }
}
