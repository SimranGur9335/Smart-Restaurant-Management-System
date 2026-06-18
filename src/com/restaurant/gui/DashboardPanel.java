package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.*;
import com.restaurant.utility.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.time.LocalDate;

/**
 * Dashboard Panel displaying key restaurant statistics and interactive charts.
 * Draws custom vector-based charts (bar/line) with anti-aliasing.
 */
public class DashboardPanel extends JPanel {
    private Restaurant restaurant;
    
    // Labels for stats cards
    private JLabel lblRevenue;
    private JLabel lblTodayOrders;
    private JLabel lblActiveTables;
    private JLabel lblReservations;
    private JLabel lblInvAlerts;
    private JLabel lblStaffPresent;
    
    private JPanel recentOrdersFeedPanel;
    private ChartPanel salesChart;
    private ChartPanel dishesChart;

    public DashboardPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Bistro Dashboard Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Real-time summary of sales collection, seating layout, and kitchen performance");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Stats Cards Grid: 2 rows, 3 columns
        JPanel cardsGrid = new JPanel(new GridLayout(2, 3, 16, 16));
        cardsGrid.setBackground(getBackground());
        cardsGrid.setBorder(new EmptyBorder(12, 0, 16, 0));

        JPanel cardRev = createStatCard("Revenue Today", "₹0.00", Theme.getPrimaryGreen());
        lblRevenue = (JLabel) cardRev.getClientProperty("valLabel");
        
        JPanel cardOrders = createStatCard("Today's Orders", "0", new Color(249, 115, 22)); // Orange
        lblTodayOrders = (JLabel) cardOrders.getClientProperty("valLabel");
        
        JPanel cardTables = createStatCard("Active Tables", "0/8", Theme.getPrimaryRed());
        lblActiveTables = (JLabel) cardTables.getClientProperty("valLabel");
        
        JPanel cardRes = createStatCard("Reservations Today", "0", Theme.getPrimaryBlue());
        lblReservations = (JLabel) cardRes.getClientProperty("valLabel");
        
        JPanel cardInv = createStatCard("Inventory Alerts", "0", Color.MAGENTA);
        lblInvAlerts = (JLabel) cardInv.getClientProperty("valLabel");
        
        JPanel cardStaff = createStatCard("Staff Present", "0", new Color(14, 165, 233)); // Cyan
        lblStaffPresent = (JLabel) cardStaff.getClientProperty("valLabel");

        cardsGrid.add(cardRev);
        cardsGrid.add(cardOrders);
        cardsGrid.add(cardTables);
        cardsGrid.add(cardRes);
        cardsGrid.add(cardInv);
        cardsGrid.add(cardStaff);

        // Bottom Dashboard section: Left (Visual charts), Right (Recent orders feed)
        JPanel bottomSection = new JPanel(new GridBagLayout());
        bottomSection.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Panel: Charts
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        chartsPanel.setBackground(getBackground());
        
        salesChart = new ChartPanel("Daily Sales Revenue (Consecutive Days)", ChartPanel.LINE);
        dishesChart = new ChartPanel("Top Selling Categories", ChartPanel.BAR);
        
        chartsPanel.add(salesChart);
        chartsPanel.add(dishesChart);

        gbc.gridx = 0;
        gbc.weightx = 0.65;
        bottomSection.add(chartsPanel, gbc);

        // Spacer
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        JPanel spacing = new JPanel();
        spacing.setBackground(getBackground());
        bottomSection.add(spacing, gbc);

        // Right Panel: Recent activity timeline
        JPanel timelinePanel = new JPanel(new BorderLayout(12, 12));
        timelinePanel.setBackground(Theme.getPanelBg());
        timelinePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel timelineTitle = new JLabel("Order Operations Stream");
        timelineTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timelineTitle.setForeground(Theme.getFg());
        timelinePanel.add(timelineTitle, BorderLayout.NORTH);

        recentOrdersFeedPanel = new JPanel();
        recentOrdersFeedPanel.setLayout(new BoxLayout(recentOrdersFeedPanel, BoxLayout.Y_AXIS));
        recentOrdersFeedPanel.setBackground(Theme.getPanelBg());
        
        JScrollPane scrollPane = new JScrollPane(recentOrdersFeedPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        timelinePanel.add(scrollPane, BorderLayout.CENTER);

        gbc.gridx = 2;
        gbc.weightx = 0.33;
        bottomSection.add(timelinePanel, gbc);

        JPanel mainCenterWrapper = new JPanel(new BorderLayout());
        mainCenterWrapper.setBackground(getBackground());
        mainCenterWrapper.add(cardsGrid, BorderLayout.NORTH);
        mainCenterWrapper.add(bottomSection, BorderLayout.CENTER);

        add(mainCenterWrapper, BorderLayout.CENTER);

        refreshDashboard();
    }

    private JPanel createStatCard(String title, String val, Color highlightColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.getPanelBg());
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JPanel line = new JPanel();
        line.setPreferredSize(new Dimension(0, 4));
        line.setBackground(highlightColor);
        card.add(line, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(2, 1, 4, 4));
        content.setBackground(Theme.getPanelBg());

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(Theme.getSubFg());

        JLabel lblVal = new JLabel(val);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblVal.setForeground(Theme.getFg());

        content.add(lblTitle);
        content.add(lblVal);
        card.add(content, BorderLayout.CENTER);

        card.putClientProperty("valLabel", lblVal);
        return card;
    }

    public void refreshDashboard() {
        String todayDate = LocalDate.now().toString();

        // 1. Calculate today's revenue (sum of paid orders today)
        double todayRevenue = 0.0;
        int todayOrdersCount = 0;
        Order[] ordersList = restaurant.getOrdersCopy();
        for (Order o : ordersList) {
            if (o.getOrderDate().equals(todayDate)) {
                todayOrdersCount++;
                if (o.isPaid()) {
                    todayRevenue += o.getGrandTotal();
                }
            }
        }
        lblRevenue.setText(String.format("₹%.2f", todayRevenue));
        lblTodayOrders.setText(String.valueOf(todayOrdersCount));

        // 2. Active occupied tables
        int occupiedCount = 0;
        Table[] tableList = restaurant.getTablesCopy();
        for (Table t : tableList) {
            if (t.getStatus().equalsIgnoreCase("Occupied")) {
                occupiedCount++;
            }
        }
        lblActiveTables.setText(occupiedCount + "/" + tableList.length);

        // 3. Reservations today
        int resCount = 0;
        Reservation[] resList = restaurant.getReservationsCopy();
        for (Reservation r : resList) {
            if (r.getDate().equals(todayDate) && !r.getStatus().equalsIgnoreCase("Cancelled")) {
                resCount++;
            }
        }
        lblReservations.setText(String.valueOf(resCount));

        // 4. Low stock inventory alerts count
        int lowCount = 0;
        Ingredient[] ingList = restaurant.getInventoryCopy();
        for (Ingredient ing : ingList) {
            if (ing.isLowStock()) {
                lowCount++;
            }
        }
        lblInvAlerts.setText(String.valueOf(lowCount));

        // 5. Staff present today
        int staffPresent = 0;
        Attendance[] attList = restaurant.getAttendanceCopy();
        for (Attendance att : attList) {
            if (att.getDate().equals(todayDate) && att.isPresent()) {
                staffPresent++;
            }
        }
        lblStaffPresent.setText(String.valueOf(staffPresent));

        // 6. Refresh recent activity feed log
        recentOrdersFeedPanel.removeAll();
        int displayCount = Math.min(ordersList.length, 6);
        
        if (displayCount == 0) {
            JLabel noOrders = new JLabel("No operations activity logged.");
            noOrders.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noOrders.setForeground(Theme.getSubFg());
            noOrders.setBorder(new EmptyBorder(12, 0, 0, 0));
            recentOrdersFeedPanel.add(noOrders);
        } else {
            for (int i = ordersList.length - 1; i >= ordersList.length - displayCount; i--) {
                Order order = ordersList[i];
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Theme.getPanelBg());
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
                row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.getBorderColor()),
                    new EmptyBorder(6, 6, 6, 6)
                ));

                JPanel left = new JPanel(new GridLayout(2, 1));
                left.setBackground(Theme.getPanelBg());
                JLabel nameLabel = new JLabel("Order #" + order.getOrderId() + " - " + order.getCustomer().getName());
                nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                nameLabel.setForeground(Theme.getFg());
                JLabel descLabel = new JLabel(order.getItemCount() + " items | Type: " + order.getOrderType() + " | Status: " + order.getOrderStatus());
                descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                descLabel.setForeground(Theme.getSubFg());
                left.add(nameLabel);
                left.add(descLabel);

                JPanel right = new JPanel(new GridLayout(2, 1));
                right.setBackground(Theme.getPanelBg());
                JLabel amtLabel = new JLabel(String.format("₹%.2f", order.getGrandTotal()));
                amtLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                amtLabel.setForeground(Theme.getFg());
                amtLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                
                JLabel stateLabel = new JLabel(order.isPaid() ? "Paid" : "Pending");
                stateLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
                stateLabel.setForeground(order.isPaid() ? Theme.getPrimaryGreen() : Theme.getPrimaryRed());
                stateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                
                right.add(amtLabel);
                right.add(stateLabel);

                row.add(left, BorderLayout.WEST);
                row.add(right, BorderLayout.EAST);
                recentOrdersFeedPanel.add(row);
            }
        }

        // 7. Update Charts Data
        double[] salesData = restaurant.getDailySales();
        double[] chartSales = new double[7]; // Show last 7 values
        String[] chartDays = new String[7];
        for (int i = 0; i < 7; i++) {
            int salesIdx = salesData.length - 7 + i;
            if (salesIdx >= 0 && salesIdx < salesData.length) {
                chartSales[i] = salesData[salesIdx];
                chartDays[i] = "Day " + (salesIdx + 1);
            } else {
                chartSales[i] = 0.0;
                chartDays[i] = "-";
            }
        }
        salesChart.setData(chartSales, chartDays);

        // Categories metrics
        double startersAmt = 0.0;
        double mainAmt = 0.0;
        double bevAmt = 0.0;
        double desAmt = 0.0;
        
        for (Order o : ordersList) {
            for (FoodItem item : o.getOrderedItems()) {
                String cat = item.getCategory();
                if (cat.equalsIgnoreCase("Starters")) startersAmt += item.getPrice();
                else if (cat.equalsIgnoreCase("Main Course")) mainAmt += item.getPrice();
                else if (cat.equalsIgnoreCase("Beverages")) bevAmt += item.getPrice();
                else if (cat.equalsIgnoreCase("Desserts")) desAmt += item.getPrice();
            }
        }
        dishesChart.setData(
            new double[]{startersAmt, mainAmt, bevAmt, desAmt},
            new String[]{"Starters", "Main", "Beverage", "Dessert"}
        );

        recentOrdersFeedPanel.revalidate();
        recentOrdersFeedPanel.repaint();
    }

    /**
     * Vector drawing chart component.
     */
    private static class ChartPanel extends JPanel {
        static final int BAR = 0;
        static final int LINE = 1;

        private String title;
        private int type;
        private double[] data = new double[0];
        private String[] labels = new String[0];

        ChartPanel(String title, int type) {
            this.title = title;
            this.type = type;
            setPreferredSize(new Dimension(300, 220));
        }

        void setData(double[] data, String[] labels) {
            this.data = data;
            this.labels = labels;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();

            // Background card
            g2.setColor(Theme.getPanelBg());
            g2.fillRoundRect(0, 0, w, h, 12, 12);
            g2.setColor(Theme.getBorderColor());
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);

            // Chart Title
            g2.setColor(Theme.getFg());
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.drawString(title, 16, 26);

            if (data == null || data.length == 0) return;

            // Find Max Value
            double maxVal = 100.0;
            for (double d : data) {
                if (d > maxVal) maxVal = d;
            }

            int pad = 40;
            int graphW = w - (2 * pad);
            int graphH = h - (2 * pad) - 10;
            int startX = pad;
            int startY = h - pad;

            // Draw grid lines & axis values
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.setColor(Theme.getBorderColor());
            g2.drawLine(startX, startY, startX + graphW, startY); // bottom axis
            
            // Draw visual graph elements
            if (type == BAR) {
                int barW = graphW / data.length - 12;
                for (int i = 0; i < data.length; i++) {
                    double val = data[i];
                    int barH = (int) ((val / maxVal) * graphH);
                    int x = startX + i * (graphW / data.length) + 6;
                    int y = startY - barH;

                    // Rounded column bars
                    g2.setPaint(new GradientPaint(x, y, Theme.getPrimaryBlue(), x, startY, new Color(14, 165, 233)));
                    g2.fillRoundRect(x, y, barW, barH, 6, 6);
                    
                    // Values on top
                    g2.setColor(Theme.getFg());
                    g2.drawString(String.format("₹%.0f", val), x, y - 4);

                    // Label below
                    g2.setColor(Theme.getSubFg());
                    g2.drawString(labels[i], x, startY + 16);
                }
            } 
            else if (type == LINE) {
                Path2D.Double path = new Path2D.Double();
                int xDiff = graphW / (data.length - 1);
                
                // Draw line points
                for (int i = 0; i < data.length; i++) {
                    double val = data[i];
                    int x = startX + i * xDiff;
                    int y = startY - (int) ((val / maxVal) * graphH);

                    if (i == 0) {
                        path.moveTo(x, y);
                    } else {
                        path.lineTo(x, y);
                    }
                }
                
                // Stroke path line
                g2.setColor(Theme.getPrimaryGreen());
                g2.setStroke(new BasicStroke(2.5f));
                g2.draw(path);

                // Draw dots
                for (int i = 0; i < data.length; i++) {
                    double val = data[i];
                    int x = startX + i * xDiff;
                    int y = startY - (int) ((val / maxVal) * graphH);

                    g2.setColor(Color.WHITE);
                    g2.fillOval(x - 5, y - 5, 10, 10);
                    g2.setColor(Theme.getPrimaryGreen());
                    g2.drawOval(x - 5, y - 5, 10, 10);

                    // Value label on hover simulation
                    if (val > 0) {
                        g2.setColor(Theme.getFg());
                        g2.drawString(String.format("₹%.0f", val), x - 12, y - 8);
                    }

                    g2.setColor(Theme.getSubFg());
                    if (labels[i] != null) {
                        g2.drawString(labels[i], x - 12, startY + 16);
                    }
                }
            }
        }
    }
}
