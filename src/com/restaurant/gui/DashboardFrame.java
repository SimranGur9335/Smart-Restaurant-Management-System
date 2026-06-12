package com.restaurant.gui;

import com.restaurant.service.Restaurant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main application window JFrame with sidebar navigation and CardLayout cards.
 * Coordinates all panels and dynamic refreshes.
 */
public class DashboardFrame extends JFrame {
    private Restaurant restaurant;
    private JPanel cardsContainer;
    private CardLayout cardLayout;

    // References to panels for programmatic refreshes
    private DashboardPanel dashboardPanel;
    private CustomerPanel customerPanel;
    private MenuPanel menuPanel;
    private OrderPanel orderPanel;
    private BillingPanel billingPanel;
    private AnalyticsPanel analyticsPanel;

    // Track active sidebar button to style it
    private JButton activeBtn;

    public DashboardFrame() {
        this.restaurant = new Restaurant();

        setTitle("Smart Restaurant Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null); // Center on screen

        // Root container split into Left (Sidebar) and Right (Main Panels)
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(248, 250, 252));
        
        // --- Sidebar (Left Panel) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42)); // Slate-900 dark bg
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new EmptyBorder(16, 0, 16, 0));

        // Brand Banner
        JLabel brandLabel = new JLabel("SMART BISTRO");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandLabel.setBorder(new EmptyBorder(12, 0, 24, 0));
        sidebar.add(brandLabel);

        // Sidebar Navigation Buttons
        JButton btnDash = createSidebarButton("Dashboard", "DASHBOARD");
        JButton btnCust = createSidebarButton("Customers", "CUSTOMERS");
        JButton btnMenu = createSidebarButton("Menu Card", "MENU");
        JButton btnOrder = createSidebarButton("Place Order", "ORDER");
        JButton btnBill = createSidebarButton("Billing & Pay", "BILLING");
        JButton btnAnal = createSidebarButton("Analytics", "ANALYTICS");

        sidebar.add(btnDash);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(btnCust);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(btnMenu);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(btnOrder);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(btnBill);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(btnAnal);

        // Add flexible spacer push items up
        sidebar.add(Box.createVerticalGlue());

        // Footer in Sidebar
        JLabel footerLabel = new JLabel("Version 1.0 (Swing GUI)");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(148, 163, 184)); // Slate-400
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footerLabel);

        root.add(sidebar, BorderLayout.WEST);

        // --- Main Display Container (CardLayout) ---
        cardLayout = new CardLayout();
        cardsContainer = new JPanel(cardLayout);
        cardsContainer.setBackground(new Color(248, 250, 252));

        // Initialize sub-panels
        dashboardPanel = new DashboardPanel(restaurant);
        customerPanel = new CustomerPanel(restaurant);
        menuPanel = new MenuPanel(restaurant);
        orderPanel = new OrderPanel(restaurant, this);
        billingPanel = new BillingPanel(restaurant);
        analyticsPanel = new AnalyticsPanel(restaurant);

        // Add panels to card deck
        cardsContainer.add(dashboardPanel, "DASHBOARD");
        cardsContainer.add(customerPanel, "CUSTOMERS");
        cardsContainer.add(menuPanel, "MENU");
        cardsContainer.add(orderPanel, "ORDER");
        cardsContainer.add(billingPanel, "BILLING");
        cardsContainer.add(analyticsPanel, "ANALYTICS");

        root.add(cardsContainer, BorderLayout.CENTER);

        setContentPane(root);

        // Default: Select Dashboard
        selectCard(btnDash, "DASHBOARD");
    }

    // Creates formatted flat buttons for the left navigation column
    private JButton createSidebarButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(new Color(203, 213, 225)); // Slate-300
        btn.setBackground(new Color(15, 23, 42)); // Match sidebar dark bg
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(240, 44));
        btn.setPreferredSize(new Dimension(240, 44));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 24, 0, 0));

        // Hover events
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeBtn) {
                    btn.setBackground(new Color(30, 41, 59)); // Hover color
                    btn.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeBtn) {
                    btn.setBackground(new Color(15, 23, 42));
                    btn.setForeground(new Color(203, 213, 225));
                }
            }
        });

        // Click actions
        btn.addActionListener(e -> selectCard(btn, cardName));

        return btn;
    }

    private void selectCard(JButton button, String cardName) {
        // Reset old active button styling
        if (activeBtn != null) {
            activeBtn.setBackground(new Color(15, 23, 42));
            activeBtn.setForeground(new Color(203, 213, 225));
        }

        // Apply new active button styling
        activeBtn = button;
        activeBtn.setBackground(new Color(37, 99, 235)); // Primary blue
        activeBtn.setForeground(Color.WHITE);

        // Dynamic checks to refresh data on showing the panel
        if (cardName.equals("DASHBOARD")) {
            dashboardPanel.refreshDashboard();
        } else if (cardName.equals("CUSTOMERS")) {
            customerPanel.refreshCustomerTable();
        } else if (cardName.equals("ORDER")) {
            orderPanel.refreshCustomersList();
        } else if (cardName.equals("ANALYTICS")) {
            analyticsPanel.refreshSalesTable();
        }

        // Switch card view
        cardLayout.show(cardsContainer, cardName);
    }

    /**
     * Programmatic redirect helper (usually called from OrderPanel -> BillingPanel)
     */
    public void showBillingPaneWithOrder(int orderId) {
        // Manually switch active button styling to Billing
        // Traverse components in sidebar to locate the Billing button
        Component[] components = getContentPane().getComponents();
        JPanel sidebarPanel = null;
        for (Component comp : components) {
            if (comp instanceof JPanel && comp.getPreferredSize().width == 240) {
                sidebarPanel = (JPanel) comp;
                break;
            }
        }

        JButton billingBtn = null;
        if (sidebarPanel != null) {
            for (Component comp : sidebarPanel.getComponents()) {
                if (comp instanceof JButton && ((JButton) comp).getText().contains("Billing")) {
                    billingBtn = (JButton) comp;
                    break;
                }
            }
        }

        if (billingBtn != null) {
            selectCard(billingBtn, "BILLING");
        } else {
            cardLayout.show(cardsContainer, "BILLING");
        }
        
        billingPanel.searchOrderBill(orderId);
    }
}
