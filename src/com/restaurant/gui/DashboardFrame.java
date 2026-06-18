package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.utility.LocalStorage;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Main application window JFrame with sidebar navigation and CardLayout cards.
 * Controls role-based restrictions, theme updates, and logouts.
 */
public class DashboardFrame extends JFrame {
    private Restaurant restaurant;
    private JPanel cardsContainer;
    private CardLayout cardLayout;

    // References to panels for programmatic refreshes & rendering
    private DashboardPanel dashboardPanel;
    private CustomerPanel customerPanel;
    private MenuPanel menuPanel;
    private OrderPanel orderPanel;
    private BillingPanel billingPanel;
    private AnalyticsPanel analyticsPanel;
    
    // New Panels
    private TablePanel tablePanel;
    private KitchenPanel kitchenPanel;
    private ReservationPanel reservationPanel;
    private InventoryPanel inventoryPanel;
    private SupplierPanel supplierPanel;
    private StaffPanel staffPanel;
    private ReportsPanel reportsPanel;

    private JButton activeBtn;
    private String userRole;
    private String userName;
    private JLabel notificationCountLabel;
    private JPanel sidebar;

    public DashboardFrame() {
        this.restaurant = new Restaurant();
        this.userRole = LocalStorage.getItem("currentUser");
        this.userName = LocalStorage.getItem("currentUsername");
        
        if (userRole == null) {
            userRole = "Admin";
        }
        if (userName == null) {
            userName = "staff";
        }

        setTitle("Smart Bistro Management System - [" + userRole + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        // Root container split into West (Sidebar) and Center (Main Shell)
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.getBg());
        
        // --- Sidebar (Left Panel) ---
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.getSidebarBg());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(16, 0, 16, 0));
        sidebar.putClientProperty("isSidebar", true);

        // Brand Banner
        JLabel brandLabel = new JLabel("SMART BISTRO");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandLabel.setBorder(new EmptyBorder(12, 0, 24, 0));
        sidebar.add(brandLabel);

        // Sub Panels Instantiation
        dashboardPanel = new DashboardPanel(restaurant);
        customerPanel = new CustomerPanel(restaurant);
        menuPanel = new MenuPanel(restaurant);
        orderPanel = new OrderPanel(restaurant, this);
        billingPanel = new BillingPanel(restaurant);
        analyticsPanel = new AnalyticsPanel(restaurant);
        tablePanel = new TablePanel(restaurant);
        kitchenPanel = new KitchenPanel(restaurant);
        reservationPanel = new ReservationPanel(restaurant);
        inventoryPanel = new InventoryPanel(restaurant);
        supplierPanel = new SupplierPanel(restaurant);
        staffPanel = new StaffPanel(restaurant);
        reportsPanel = new ReportsPanel(restaurant);

        // Main Display Card Layout Container
        cardLayout = new CardLayout();
        cardsContainer = new JPanel(cardLayout);
        cardsContainer.setBackground(Theme.getBg());

        // Add views to deck
        cardsContainer.add(dashboardPanel, "DASHBOARD");
        cardsContainer.add(customerPanel, "CUSTOMERS");
        cardsContainer.add(menuPanel, "MENU");
        cardsContainer.add(orderPanel, "ORDER");
        cardsContainer.add(billingPanel, "BILLING");
        cardsContainer.add(analyticsPanel, "ANALYTICS");
        cardsContainer.add(tablePanel, "TABLES");
        cardsContainer.add(kitchenPanel, "KITCHEN");
        cardsContainer.add(reservationPanel, "RESERVATIONS");
        cardsContainer.add(inventoryPanel, "INVENTORY");
        cardsContainer.add(supplierPanel, "SUPPLIERS");
        cardsContainer.add(staffPanel, "STAFF");
        cardsContainer.add(reportsPanel, "REPORTS");

        // Role based button filtering and views attachment
        JButton defaultBtn = configureSidebarMenus();

        sidebar.add(Box.createVerticalGlue());

        // Footer version tag
        JLabel footerLabel = new JLabel("Bistro Control Hub v1.2");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(148, 163, 184));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footerLabel);

        root.add(sidebar, BorderLayout.WEST);

        // --- Center Panel: Header + Cards Container ---
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Theme.getBg());

        // Top Control Header Bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.getPanelBg());
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.getBorderColor()),
            new EmptyBorder(12, 24, 12, 24)
        ));

        // Left: Profile Info
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        profilePanel.setBackground(Theme.getPanelBg());
        JLabel userLabel = new JLabel("Welcome, " + userName.toUpperCase() + " (" + userRole + ")");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Theme.getFg());
        profilePanel.add(userLabel);
        header.add(profilePanel, BorderLayout.WEST);

        // Right: Theme, Notifications & Logouts
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        actionsPanel.setBackground(Theme.getPanelBg());

        // Theme Mode Toggle
        JButton btnTheme = new JButton(Theme.getMode() == Theme.LIGHT ? "Dark Mode" : "Light Mode");
        btnTheme.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTheme.setForeground(Color.WHITE);
        btnTheme.setBackground(new Color(100, 116, 139));
        btnTheme.setFocusPainted(false);
        btnTheme.addActionListener(e -> {
            Theme.toggleTheme();
            btnTheme.setText(Theme.getMode() == Theme.LIGHT ? "Dark Mode" : "Light Mode");
            applyActiveColors();
        });
        actionsPanel.add(btnTheme);

        // Notification Bell dropdown trigger
        JPanel notificationBellPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        notificationBellPanel.setBackground(Theme.getPanelBg());
        
        JButton btnNotifications = new JButton("🔔 Notifications");
        btnNotifications.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNotifications.setBackground(Theme.getPrimaryBlue());
        btnNotifications.setForeground(Color.WHITE);
        btnNotifications.setFocusPainted(false);
        btnNotifications.addActionListener(e -> showNotificationsFeed());
        
        notificationCountLabel = new JLabel("0");
        notificationCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        notificationCountLabel.setForeground(Theme.getPrimaryRed());
        
        notificationBellPanel.add(btnNotifications);
        notificationBellPanel.add(notificationCountLabel);
        actionsPanel.add(notificationBellPanel);

        // Logout
        JButton btnLogout = new JButton("Logout ⮞");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(Theme.getPrimaryRed());
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> handleLogout());
        actionsPanel.add(btnLogout);

        header.add(actionsPanel, BorderLayout.EAST);
        mainContent.add(header, BorderLayout.NORTH);
        
        mainContent.add(cardsContainer, BorderLayout.CENTER);
        root.add(mainContent, BorderLayout.CENTER);

        setContentPane(root);

        // Sync count badge updates
        NotificationManager.setRefreshCallback(this::updateNotificationBadge);
        updateNotificationBadge();

        // Default screen show
        if (defaultBtn != null) {
            defaultBtn.doClick();
        } else {
            cardLayout.show(cardsContainer, "DASHBOARD");
        }
    }

    private void updateNotificationBadge() {
        int count = NotificationManager.getUnreadCount();
        notificationCountLabel.setText("(" + count + ")");
    }

    private void showNotificationsFeed() {
        List<String> list = NotificationManager.getNotifications();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No notifications logged.", "Notifications Center", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("--- Active Alerts Log ---\n\n");
        for (int i = list.size() - 1; i >= 0; i--) {
            sb.append("• ").append(list.get(i)).append("\n");
        }
        
        String[] options = {"Clear Logs", "Close"};
        int choice = JOptionPane.showOptionDialog(
            this,
            new JScrollPane(new JTextArea(sb.toString(), 15, 30)),
            "Notification Center Logs",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[1]
        );

        if (choice == 0) {
            NotificationManager.clearAll();
            updateNotificationBadge();
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to sign out?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Wipe session
            LocalStorage.removeItem("currentUser");
            LocalStorage.removeItem("currentUsername");
            
            // Redirect
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
            this.dispose();
        }
    }

    private JButton configureSidebarMenus() {
        JButton defaultBtn = null;

        // Admin Role
        if (userRole.equalsIgnoreCase("Admin")) {
            JButton btnDash = createSidebarButton("Dashboard", "DASHBOARD");
            JButton btnCust = createSidebarButton("Customers Registry", "CUSTOMERS");
            JButton btnMenu = createSidebarButton("Menu Card", "MENU");
            JButton btnOrder = createSidebarButton("Place Order", "ORDER");
            JButton btnBill = createSidebarButton("Billing & pay", "BILLING");
            JButton btnTable = createSidebarButton("Visual Tables", "TABLES");
            JButton btnKitchen = createSidebarButton("Kitchen KDS", "KITCHEN");
            JButton btnRes = createSidebarButton("Reservations", "RESERVATIONS");
            JButton btnInv = createSidebarButton("Pantry Inventory", "INVENTORY");
            JButton btnSupp = createSidebarButton("Suppliers list", "SUPPLIERS");
            JButton btnStaff = createSidebarButton("Staff Directory", "STAFF");
            JButton btnReports = createSidebarButton("Reports Module", "REPORTS");
            JButton btnAnal = createSidebarButton("Sales Analytics", "ANALYTICS");

            sidebar.add(btnDash);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnCust);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnMenu);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnOrder);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnBill);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnTable);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnKitchen);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnRes);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnInv);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnSupp);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnStaff);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnReports);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(btnAnal);

            defaultBtn = btnDash;
        } 
        // Manager Role
        else if (userRole.equalsIgnoreCase("Manager")) {
            JButton btnDash = createSidebarButton("Dashboard", "DASHBOARD");
            JButton btnOrder = createSidebarButton("Place Order", "ORDER");
            JButton btnKitchen = createSidebarButton("Kitchen KDS", "KITCHEN");
            JButton btnRes = createSidebarButton("Reservations", "RESERVATIONS");
            JButton btnInv = createSidebarButton("Pantry Inventory", "INVENTORY");
            JButton btnSupp = createSidebarButton("Suppliers list", "SUPPLIERS");
            JButton btnStaff = createSidebarButton("Staff Directory", "STAFF");
            JButton btnReports = createSidebarButton("Reports Module", "REPORTS");

            sidebar.add(btnDash);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(btnOrder);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(btnKitchen);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(btnRes);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(btnInv);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(btnSupp);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(btnStaff);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(btnReports);

            defaultBtn = btnDash;
        } 
        // Waiter Role
        else if (userRole.equalsIgnoreCase("Waiter")) {
            JButton btnOrder = createSidebarButton("Place Order", "ORDER");
            JButton btnTable = createSidebarButton("Visual Tables", "TABLES");
            JButton btnRes = createSidebarButton("Reservations", "RESERVATIONS");

            sidebar.add(btnOrder);
            sidebar.add(Box.createVerticalStrut(8));
            sidebar.add(btnTable);
            sidebar.add(Box.createVerticalStrut(8));
            sidebar.add(btnRes);

            defaultBtn = btnOrder;
        } 
        // Chef Role
        else if (userRole.equalsIgnoreCase("Chef")) {
            JButton btnKitchen = createSidebarButton("Kitchen KDS", "KITCHEN");
            sidebar.add(btnKitchen);
            defaultBtn = btnKitchen;
        } 
        // Cashier Role
        else if (userRole.equalsIgnoreCase("Cashier")) {
            JButton btnBill = createSidebarButton("Billing & Pay", "BILLING");
            sidebar.add(btnBill);
            defaultBtn = btnBill;
        }

        return defaultBtn;
    }

    private JButton createSidebarButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(Theme.getSidebarBg());
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(220, 38));
        btn.setPreferredSize(new Dimension(220, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeBtn) {
                    btn.setBackground(new Color(30, 41, 59));
                    btn.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeBtn) {
                    btn.setBackground(Theme.getSidebarBg());
                    btn.setForeground(new Color(203, 213, 225));
                }
            }
        });

        btn.addActionListener(e -> selectCard(btn, cardName));

        return btn;
    }

    private void selectCard(JButton button, String cardName) {
        if (activeBtn != null) {
            activeBtn.setBackground(Theme.getSidebarBg());
            activeBtn.setForeground(new Color(203, 213, 225));
        }

        activeBtn = button;
        activeBtn.setBackground(Theme.getPrimaryBlue());
        activeBtn.setForeground(Color.WHITE);

        // Refresh triggers on select
        if (cardName.equals("DASHBOARD")) {
            dashboardPanel.refreshDashboard();
        } else if (cardName.equals("CUSTOMERS")) {
            customerPanel.refreshCustomerTable();
        } else if (cardName.equals("ORDER")) {
            orderPanel.refreshCustomersList();
            orderPanel.refreshTableList();
        } else if (cardName.equals("ANALYTICS")) {
            analyticsPanel.refreshSalesTable();
        } else if (cardName.equals("TABLES")) {
            tablePanel.refreshTablesGrid();
        } else if (cardName.equals("KITCHEN")) {
            kitchenPanel.refreshBoard();
        } else if (cardName.equals("RESERVATIONS")) {
            reservationPanel.refreshTable();
            reservationPanel.refreshTablesList();
        } else if (cardName.equals("INVENTORY")) {
            inventoryPanel.refreshTable();
        } else if (cardName.equals("SUPPLIERS")) {
            supplierPanel.refreshTable();
        } else if (cardName.equals("STAFF")) {
            staffPanel.refreshStaffTable();
            staffPanel.refreshAttendanceTable();
        } else if (cardName.equals("REPORTS")) {
            reportsPanel.refreshSummary();
        }

        cardLayout.show(cardsContainer, cardName);
    }

    public void showBillingPaneWithOrder(int orderId) {
        // Find billing button in sidebar components
        JButton billingBtn = null;
        for (Component comp : sidebar.getComponents()) {
            if (comp instanceof JButton && ((JButton) comp).getText().toLowerCase().contains("billing")) {
                billingBtn = (JButton) comp;
                break;
            }
        }

        if (billingBtn != null) {
            selectCard(billingBtn, "BILLING");
        } else {
            cardLayout.show(cardsContainer, "BILLING");
        }
        
        billingPanel.searchOrderBill(orderId);
    }

    private void applyActiveColors() {
        // Redraw recursively utilizing updated theme color constants
        getContentPane().setBackground(Theme.getBg());
        cardsContainer.setBackground(Theme.getBg());
        sidebar.setBackground(Theme.getSidebarBg());
        
        // Refresh specific active states
        if (activeBtn != null) {
            activeBtn.setBackground(Theme.getPrimaryBlue());
        }
        
        // Loop panels refresh colors
        dashboardPanel.setBackground(Theme.getBg());
        customerPanel.setBackground(Theme.getBg());
        menuPanel.setBackground(Theme.getBg());
        orderPanel.setBackground(Theme.getBg());
        billingPanel.setBackground(Theme.getBg());
        analyticsPanel.setBackground(Theme.getBg());
        tablePanel.setBackground(Theme.getBg());
        kitchenPanel.setBackground(Theme.getBg());
        reservationPanel.setBackground(Theme.getBg());
        inventoryPanel.setBackground(Theme.getBg());
        supplierPanel.setBackground(Theme.getBg());
        staffPanel.setBackground(Theme.getBg());
        reportsPanel.setBackground(Theme.getBg());
        
        SwingUtilities.updateComponentTreeUI(this);
    }
}
