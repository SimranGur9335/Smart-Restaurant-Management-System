package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Order;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Dashboard Panel displaying key restaurant statistics.
 * Demonstrates modern GUI design, layout managers, and card aesthetics.
 */
public class DashboardPanel extends JPanel {
    private Restaurant restaurant;
    private JLabel lblCustomersCount;
    private JLabel lblMenuCount;
    private JLabel lblOrdersCount;
    private JLabel lblRevenueCount;
    private JPanel recentOrdersFeedPanel;

    public DashboardPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252)); // Light slate bg
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Restaurant Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(15, 23, 42)); // Slate-900
        JLabel subtitleLabel = new JLabel("Real-time summary of sales, customers, and menu state");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139)); // Slate-500
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Stats Cards Grid
        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 16, 0));
        cardsGrid.setBackground(getBackground());
        cardsGrid.setBorder(new EmptyBorder(24, 0, 24, 0));

        // Create cards
        JPanel cardCust = createStatCard("Total Customers", "0", new Color(37, 99, 235)); // Primary blue
        lblCustomersCount = (JLabel) cardCust.getClientProperty("valLabel");

        JPanel cardMenu = createStatCard("Menu Items", "0", new Color(14, 165, 233)); // Cyan
        lblMenuCount = (JLabel) cardMenu.getClientProperty("valLabel");

        JPanel cardOrders = createStatCard("Orders Placed", "0", new Color(249, 115, 22)); // Orange
        lblOrdersCount = (JLabel) cardOrders.getClientProperty("valLabel");

        JPanel cardRev = createStatCard("Total Revenue", "₹0.00", new Color(22, 163, 74)); // Green
        lblRevenueCount = (JLabel) cardRev.getClientProperty("valLabel");

        cardsGrid.add(cardCust);
        cardsGrid.add(cardMenu);
        cardsGrid.add(cardOrders);
        cardsGrid.add(cardRev);

        // Center Content Area (Activity Feed / Recent Orders)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel recentTitle = new JLabel("Recent Orders & Transactions");
        recentTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        recentTitle.setForeground(new Color(15, 23, 42));
        centerPanel.add(recentTitle, BorderLayout.NORTH);

        recentOrdersFeedPanel = new JPanel();
        recentOrdersFeedPanel.setLayout(new BoxLayout(recentOrdersFeedPanel, BoxLayout.Y_AXIS));
        recentOrdersFeedPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(recentOrdersFeedPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel mainCenterWrapper = new JPanel(new BorderLayout());
        mainCenterWrapper.setBackground(getBackground());
        mainCenterWrapper.add(cardsGrid, BorderLayout.NORTH);
        mainCenterWrapper.add(centerPanel, BorderLayout.CENTER);

        add(mainCenterWrapper, BorderLayout.CENTER);

        refreshDashboard();
    }

    // Creates a dashboard statistics card
    private JPanel createStatCard(String title, String val, Color highlightColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        // Top line highlight color
        JPanel line = new JPanel();
        line.setPreferredSize(new Dimension(0, 4));
        line.setBackground(highlightColor);
        card.add(line, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(2, 1, 4, 4));
        content.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(100, 116, 139));

        JLabel lblVal = new JLabel(val);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblVal.setForeground(new Color(15, 23, 42));

        content.add(lblTitle);
        content.add(lblVal);
        card.add(content, BorderLayout.CENTER);

        // Save reference to label so it can be updated
        card.putClientProperty("valLabel", lblVal);

        return card;
    }

    /**
     * Refreshes stats counts and updates the recent orders activity feed
     */
    public void refreshDashboard() {
        // Update stats labels
        lblCustomersCount.setText(String.valueOf(restaurant.getCustomerCount()));
        lblMenuCount.setText(String.valueOf(restaurant.getMenuCount()));
        lblOrdersCount.setText(String.valueOf(restaurant.getOrderCount()));

        double totalRevenue = 0.0;
        double[] sales = restaurant.getDailySales();
        for (double sale : sales) {
            totalRevenue += sale;
        }
        lblRevenueCount.setText(String.format("₹%.2f", totalRevenue));

        // Refresh Feed
        recentOrdersFeedPanel.removeAll();
        Order[] orders = restaurant.getOrdersCopy();
        int count = Math.min(orders.length, 6); // Display last 6 orders
        
        if (count == 0) {
            JLabel noOrders = new JLabel("No transactions completed yet. Place an order to see details here!");
            noOrders.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noOrders.setForeground(new Color(148, 163, 184));
            noOrders.setAlignmentX(Component.LEFT_ALIGNMENT);
            noOrders.setBorder(new EmptyBorder(12, 0, 0, 0));
            recentOrdersFeedPanel.add(noOrders);
        } else {
            // Loop backwards (most recent first)
            for (int i = orders.length - 1; i >= orders.length - count; i--) {
                Order order = orders[i];
                JPanel orderRow = new JPanel(new BorderLayout());
                orderRow.setBackground(Color.WHITE);
                orderRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                orderRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)));
                
                JPanel leftPanel = new JPanel(new GridLayout(2, 1));
                leftPanel.setBackground(Color.WHITE);
                JLabel orderIDLbl = new JLabel("Order #" + order.getOrderId() + " - " + order.getCustomer().getName());
                orderIDLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                orderIDLbl.setForeground(new Color(51, 65, 85));
                JLabel itemsCountLbl = new JLabel(order.getItemCount() + " items ordered");
                itemsCountLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                itemsCountLbl.setForeground(new Color(148, 163, 184));
                leftPanel.add(orderIDLbl);
                leftPanel.add(itemsCountLbl);
                
                JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 0));
                rightPanel.setBackground(Color.WHITE);
                JLabel amtLbl = new JLabel(String.format("₹%.2f", order.getFinalAmount()));
                amtLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
                amtLbl.setForeground(new Color(30, 41, 59));
                amtLbl.setHorizontalAlignment(SwingConstants.RIGHT);
                
                JLabel statusLbl = new JLabel(order.isPaid() ? "Paid" : "Unpaid");
                statusLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                statusLbl.setForeground(order.isPaid() ? new Color(22, 163, 74) : new Color(220, 38, 38));
                statusLbl.setHorizontalAlignment(SwingConstants.RIGHT);
                
                rightPanel.add(amtLbl);
                rightPanel.add(statusLbl);
                
                orderRow.add(leftPanel, BorderLayout.WEST);
                orderRow.add(rightPanel, BorderLayout.EAST);
                orderRow.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)),
                    new EmptyBorder(8, 8, 8, 8)
                ));
                
                recentOrdersFeedPanel.add(orderRow);
            }
        }
        
        recentOrdersFeedPanel.revalidate();
        recentOrdersFeedPanel.repaint();
    }
}
