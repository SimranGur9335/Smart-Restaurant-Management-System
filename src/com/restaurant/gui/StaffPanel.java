package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Employee;
import com.restaurant.model.Attendance;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Visual Staff Management & Attendance System Panel.
 * Logs employees and supports clock-in/clock-out logs.
 */
public class StaffPanel extends JPanel {
    private Restaurant restaurant;
    
    // Employee fields
    private JTextField txtName;
    private JTextField txtContact;
    private JComboBox<String> cmbRole;
    private JComboBox<String> cmbShift;
    private JTable staffTable;
    private DefaultTableModel staffModel;

    // Attendance elements
    private JTable attTable;
    private DefaultTableModel attModel;

    public StaffPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(24, 24));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Staff & Attendance System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Manage employee records, allocate working shifts, and track clock-in/out registers");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Panel: Tab 1: Staff Directory, Tab 2: Attendance Logger
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // --- Tab 1: Staff Directory ---
        JPanel staffDirectoryTab = new JPanel(new GridBagLayout());
        staffDirectoryTab.setBackground(Theme.getBg());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Form (Left)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.getPanelBg());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.insets = new Insets(5, 0, 5, 0);
        fgbc.weightx = 1.0;
        fgbc.gridx = 0;

        JLabel formTitle = new JLabel("Register Employee");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(Theme.getFg());
        fgbc.gridy = 0;
        formPanel.add(formTitle, fgbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.getBorderColor());
        fgbc.gridy = 1;
        formPanel.add(sep, fgbc);

        // Fields
        fgbc.gridy = 2;
        formPanel.add(new JLabel("Full Name *"), fgbc);
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(0, 30));
        fgbc.gridy = 3;
        formPanel.add(txtName, fgbc);

        fgbc.gridy = 4;
        formPanel.add(new JLabel("Role Position *"), fgbc);
        String[] roles = {"Manager", "Waiter", "Chef", "Cashier"};
        cmbRole = new JComboBox<>(roles);
        cmbRole.setPreferredSize(new Dimension(0, 30));
        fgbc.gridy = 5;
        formPanel.add(cmbRole, fgbc);

        fgbc.gridy = 6;
        formPanel.add(new JLabel("Contact Number *"), fgbc);
        txtContact = new JTextField();
        txtContact.setPreferredSize(new Dimension(0, 30));
        fgbc.gridy = 7;
        formPanel.add(txtContact, fgbc);

        fgbc.gridy = 8;
        formPanel.add(new JLabel("Working Shift *"), gbc);
        String[] shifts = {"Morning", "Evening", "Night"};
        cmbShift = new JComboBox<>(shifts);
        cmbShift.setPreferredSize(new Dimension(0, 30));
        fgbc.gridy = 9;
        formPanel.add(cmbShift, fgbc);

        JButton btnRegister = new JButton("Register Employee");
        btnRegister.setBackground(Theme.getPrimaryBlue());
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        fgbc.gridy = 10;
        fgbc.insets = new Insets(16, 0, 4, 0);
        formPanel.add(btnRegister, fgbc);

        btnRegister.addActionListener(e -> handleAddEmployee());

        gbc.gridx = 0;
        gbc.weightx = 0.35;
        staffDirectoryTab.add(formPanel, gbc);

        // Spacer
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacePanel = new JPanel();
        spacePanel.setBackground(getBackground());
        staffDirectoryTab.add(spacePanel, gbc);

        // Table List (Right)
        JPanel tablePanel = new JPanel(new BorderLayout(12, 12));
        tablePanel.setBackground(Theme.getPanelBg());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        String[] staffCols = {"ID", "Name", "Role Position", "Contact details", "Active Shift"};
        staffModel = new DefaultTableModel(staffCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        staffTable = new JTable(staffModel);
        staffTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        staffTable.setRowHeight(30);
        staffTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        staffTable.getTableHeader().setBackground(new Color(241, 245, 249));
        staffTable.setSelectionBackground(Theme.getTableSelectionBg());
        staffTable.setShowGrid(true);
        staffTable.setGridColor(Theme.getBorderColor());

        JScrollPane staffScroll = new JScrollPane(staffTable);
        staffScroll.setBorder(null);
        tablePanel.add(staffScroll, BorderLayout.CENTER);

        JPanel staffRowActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        staffRowActions.setBackground(Theme.getPanelBg());
        JButton btnDeleteStaff = new JButton("Remove Employee");
        btnDeleteStaff.setBackground(Theme.getPrimaryRed());
        btnDeleteStaff.setForeground(Color.WHITE);
        btnDeleteStaff.setFocusPainted(false);
        staffRowActions.add(btnDeleteStaff);
        tablePanel.add(staffRowActions, BorderLayout.SOUTH);

        btnDeleteStaff.addActionListener(e -> handleDeleteEmployee());

        gbc.gridx = 2;
        gbc.weightx = 0.63;
        staffDirectoryTab.add(tablePanel, gbc);

        // --- Tab 2: Attendance Logger ---
        JPanel attendanceTab = new JPanel(new BorderLayout(16, 16));
        attendanceTab.setBackground(Theme.getBg());
        attendanceTab.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel attControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        attControlPanel.setBackground(Theme.getPanelBg());
        attControlPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));

        JButton btnClockIn = new JButton("Clock In Now");
        btnClockIn.setBackground(Theme.getPrimaryGreen());
        btnClockIn.setForeground(Color.WHITE);
        btnClockIn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JButton btnClockOut = new JButton("Clock Out Now");
        btnClockOut.setBackground(Theme.getPrimaryRed());
        btnClockOut.setForeground(Color.WHITE);
        btnClockOut.setFont(new Font("Segoe UI", Font.BOLD, 12));

        attControlPanel.add(new JLabel("Select staff from list below to track attendance:"));
        attControlPanel.add(btnClockIn);
        attControlPanel.add(btnClockOut);
        attendanceTab.add(attControlPanel, BorderLayout.NORTH);

        String[] attCols = {"Date Logged", "Staff ID", "Employee Name", "Role", "Clock-In Time", "Clock-Out Time", "Presence"};
        attModel = new DefaultTableModel(attCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        attTable = new JTable(attModel);
        attTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        attTable.setRowHeight(30);
        attTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        attTable.getTableHeader().setBackground(new Color(241, 245, 249));
        attTable.setSelectionBackground(Theme.getTableSelectionBg());
        attTable.setShowGrid(true);
        attTable.setGridColor(Theme.getBorderColor());

        JScrollPane attScroll = new JScrollPane(attTable);
        attScroll.setBorder(null);
        
        JPanel attTablePanel = new JPanel(new BorderLayout(12, 12));
        attTablePanel.setBackground(Theme.getPanelBg());
        attTablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));
        attTablePanel.add(attScroll, BorderLayout.CENTER);
        
        attendanceTab.add(attTablePanel, BorderLayout.CENTER);

        btnClockIn.addActionListener(e -> handleClock(true));
        btnClockOut.addActionListener(e -> handleClock(false));

        tabbedPane.addTab("Employee Roster", staffDirectoryTab);
        tabbedPane.addTab("Clock-In / Attendance History", attendanceTab);

        add(tabbedPane, BorderLayout.CENTER);

        refreshStaffTable();
        refreshAttendanceTable();
    }

    private void handleAddEmployee() {
        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();
        String role = (String) cmbRole.getSelectedItem();
        String shift = (String) cmbShift.getSelectedItem();

        if (name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all employee details.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        restaurant.addEmployee(name, role, contact, shift);
        
        txtName.setText("");
        txtContact.setText("");
        
        refreshStaffTable();
        NotificationManager.addNotification("Staff " + name + " registered!");
    }

    private void handleDeleteEmployee() {
        int row = staffTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to remove.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) staffModel.getValueAt(row, 0);
        String name = (String) staffModel.getValueAt(row, 1);
        
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove " + name + " from payroll?", "Remove Employee", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            restaurant.deleteEmployee(id);
            refreshStaffTable();
            NotificationManager.addNotification("Employee " + name + " removed.");
        }
    }

    private void handleClock(boolean clockIn) {
        int row = staffTable.getSelectedRow();
        if (row == -1) {
            // Also try selecting from attendance table if staff table not active
            row = attTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select an employee from the Roster first.", "Select Employee", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int staffId = (int) attModel.getValueAt(row, 1);
            triggerClock(staffId, clockIn);
        } else {
            int staffId = (int) staffModel.getValueAt(row, 0);
            triggerClock(staffId, clockIn);
        }
    }

    private void triggerClock(int staffId, boolean clockIn) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeStr = LocalTime.now().format(dtf);
        
        Employee[] list = restaurant.getStaffCopy();
        Employee emp = null;
        for (Employee e : list) {
            if (e.getEmployeeId() == staffId) {
                emp = e;
                break;
            }
        }
        
        if (emp == null) return;
        
        if (clockIn) {
            restaurant.recordClockIn(staffId, timeStr);
            NotificationManager.addNotification("Clock-In recorded for " + emp.getName() + " at " + timeStr);
        } else {
            restaurant.recordClockOut(staffId, timeStr);
            NotificationManager.addNotification("Clock-Out recorded for " + emp.getName() + " at " + timeStr);
        }
        
        refreshAttendanceTable();
    }

    public void refreshStaffTable() {
        staffModel.setRowCount(0);
        Employee[] list = restaurant.getStaffCopy();
        for (Employee e : list) {
            staffModel.addRow(new Object[]{
                e.getEmployeeId(),
                e.getName(),
                e.getRole(),
                e.getContact(),
                e.getShift()
            });
        }
    }

    public void refreshAttendanceTable() {
        attModel.setRowCount(0);
        Attendance[] list = restaurant.getAttendanceCopy();
        for (Attendance a : list) {
            Employee[] staffList = restaurant.getStaffCopy();
            String name = "Unknown";
            String role = "Staff";
            for (Employee e : staffList) {
                if (e.getEmployeeId() == a.getEmployeeId()) {
                    name = e.getName();
                    role = e.getRole();
                    break;
                }
            }
            attModel.addRow(new Object[]{
                a.getDate(),
                a.getEmployeeId(),
                name,
                role,
                a.getClockInTime(),
                a.getClockOutTime(),
                a.isPresent() ? "Present" : "Absent"
            });
        }
    }
}
