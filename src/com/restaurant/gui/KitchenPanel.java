package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Order;
import com.restaurant.model.FoodItem;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Kitchen Display System Panel (KDS) for Chefs.
 * Divides orders into columns (New, Preparing, Ready, Served).
 */
public class KitchenPanel extends JPanel {
    private Restaurant restaurant;
    private JPanel newOrdersCol;
    private JPanel preparingCol;
    private JPanel readyCol;
    private JPanel servedCol;
    
    // Map to track highlighted priority levels of orders (in-memory)
    private static final Map<Integer, Boolean> priorityMap = new HashMap<>();

    public KitchenPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Kitchen Display System (KDS)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Monitor active orders, prioritize chef dishes, and manage kitchen status updates");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Grid split layout with 4 columns
        JPanel boardPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        boardPanel.setBackground(getBackground());

        newOrdersCol = createColumnPanel("NEW ORDERS", new Color(37, 99, 235)); // Primary Blue
        preparingCol = createColumnPanel("PREPARING", new Color(249, 115, 22)); // Orange
        readyCol = createColumnPanel("READY FOR SERVICE", new Color(14, 165, 233)); // Cyan
        servedCol = createColumnPanel("SERVED / DELIVERED", new Color(22, 163, 74)); // Green

        boardPanel.add(newOrdersCol);
        boardPanel.add(preparingCol);
        boardPanel.add(readyCol);
        boardPanel.add(servedCol);

        add(boardPanel, BorderLayout.CENTER);

        refreshBoard();
    }

    private JPanel createColumnPanel(String title, Color barColor) {
        JPanel col = new JPanel(new BorderLayout(8, 8));
        col.setBackground(Theme.getPanelBg());
        col.setBorder(BorderFactory.createLineBorder(Theme.getBorderColor(), 1, true));

        // Column Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(barColor);
        topBar.setPreferredSize(new Dimension(0, 40));
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(Color.WHITE);
        topBar.add(lblTitle, BorderLayout.CENTER);
        col.add(topBar, BorderLayout.NORTH);

        // Container scroll area for cards
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(Theme.getPanelBg());
        cardsContainer.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(cardsContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        col.add(scroll, BorderLayout.CENTER);

        // Save reference to sub panel for refreshing
        col.putClientProperty("cardsContainer", cardsContainer);

        return col;
    }

    public void refreshBoard() {
        // Clear sub panels
        JPanel newContainer = (JPanel) newOrdersCol.getClientProperty("cardsContainer");
        JPanel prepContainer = (JPanel) preparingCol.getClientProperty("cardsContainer");
        JPanel readyContainer = (JPanel) readyCol.getClientProperty("cardsContainer");
        JPanel servedContainer = (JPanel) servedCol.getClientProperty("cardsContainer");

        newContainer.removeAll();
        prepContainer.removeAll();
        readyContainer.removeAll();
        servedContainer.removeAll();

        Order[] list = restaurant.getOrdersCopy();
        for (Order o : list) {
            String status = o.getOrderStatus();
            if (status.equalsIgnoreCase("Placed")) {
                newContainer.add(createOrderCard(o));
                newContainer.add(Box.createVerticalStrut(10));
            } else if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Preparing")) {
                prepContainer.add(createOrderCard(o));
                prepContainer.add(Box.createVerticalStrut(10));
            } else if (status.equalsIgnoreCase("Ready")) {
                readyContainer.add(createOrderCard(o));
                readyContainer.add(Box.createVerticalStrut(10));
            } else if (status.equalsIgnoreCase("Served") || status.equalsIgnoreCase("Delivered")) {
                servedContainer.add(createOrderCard(o));
                servedContainer.add(Box.createVerticalStrut(10));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createOrderCard(Order o) {
        boolean isHighPriority = priorityMap.getOrDefault(o.getOrderId(), false);
        
        JPanel card = new JPanel(new BorderLayout(6, 6));
        card.setBackground(Theme.getPanelBg());
        
        // Dynamic border highlight for high priority or default
        Color borderColor = isHighPriority ? Theme.getPrimaryRed() : Theme.getBorderColor();
        int borderWidth = isHighPriority ? 2 : 1;
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderColor, borderWidth, true),
            new EmptyBorder(12, 12, 12, 12)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        // Top line
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(card.getBackground());
        
        JLabel lblId = new JLabel("Order #" + o.getOrderId() + " (" + o.getOrderType() + ")", SwingConstants.LEFT);
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblId.setForeground(Theme.getFg());
        
        JButton btnPriority = new JButton(isHighPriority ? "!!!" : "!");
        btnPriority.setPreferredSize(new Dimension(24, 24));
        btnPriority.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnPriority.setForeground(Color.WHITE);
        btnPriority.setBackground(isHighPriority ? Theme.getPrimaryRed() : Theme.getSubFg());
        btnPriority.setFocusPainted(false);
        btnPriority.setBorderPainted(false);
        btnPriority.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPriority.addActionListener(e -> {
            priorityMap.put(o.getOrderId(), !isHighPriority);
            refreshBoard();
        });

        top.add(lblId, BorderLayout.CENTER);
        top.add(btnPriority, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        // Items Center List
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(card.getBackground());
        
        FoodItem[] items = o.getOrderedItems();
        for (FoodItem item : items) {
            JLabel lblItem = new JLabel("• " + item.getItemName());
            lblItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblItem.setForeground(Theme.getFg());
            center.add(lblItem);
        }
        
        card.add(center, BorderLayout.CENTER);

        // Bottom Button Actions
        JPanel bottom = new JPanel(new BorderLayout(4, 0));
        bottom.setBackground(card.getBackground());
        bottom.setBorder(new EmptyBorder(8, 0, 0, 0));

        String curStatus = o.getOrderStatus();
        JButton btnAction = new JButton();
        btnAction.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAction.setForeground(Color.WHITE);
        btnAction.setFocusPainted(false);
        btnAction.setBorderPainted(false);
        btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (curStatus.equalsIgnoreCase("Placed")) {
            btnAction.setText("Accept Order");
            btnAction.setBackground(Theme.getPrimaryBlue());
            btnAction.addActionListener(e -> {
                o.setOrderStatus("Preparing");
                restaurant.updateOrder(o);
                NotificationManager.addNotification("Order #" + o.getOrderId() + " moved to Preparing");
                refreshBoard();
            });
            bottom.add(btnAction, BorderLayout.CENTER);
        } else if (curStatus.equalsIgnoreCase("Preparing") || curStatus.equalsIgnoreCase("Accepted")) {
            btnAction.setText("Mark Ready");
            btnAction.setBackground(new Color(249, 115, 22)); // Orange
            btnAction.addActionListener(e -> {
                o.setOrderStatus("Ready");
                restaurant.updateOrder(o);
                NotificationManager.addNotification("Order #" + o.getOrderId() + " is Ready!");
                refreshBoard();
            });
            bottom.add(btnAction, BorderLayout.CENTER);
        } else if (curStatus.equalsIgnoreCase("Ready")) {
            String actText = o.getOrderType().equalsIgnoreCase("Dine-In") ? "Serve Order" : "Deliver Order";
            btnAction.setText(actText);
            btnAction.setBackground(Theme.getPrimaryGreen());
            btnAction.addActionListener(e -> {
                o.setOrderStatus(o.getOrderType().equalsIgnoreCase("Dine-In") ? "Served" : "Delivered");
                
                // If Dine-in, also check if table status can be updated
                if (o.getTableId() != -1) {
                    // Table is occupied till customer pays
                }
                
                restaurant.updateOrder(o);
                NotificationManager.addNotification("Order #" + o.getOrderId() + " has been " + o.getOrderStatus());
                refreshBoard();
            });
            bottom.add(btnAction, BorderLayout.CENTER);
        } else {
            // Served / Delivered - display completed label
            JLabel lblDone = new JLabel("COMPLETED ✓", SwingConstants.CENTER);
            lblDone.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblDone.setForeground(Theme.getPrimaryGreen());
            bottom.add(lblDone, BorderLayout.CENTER);
        }

        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }
}
