package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Order;
import com.restaurant.model.FoodItem;
import com.restaurant.payment.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Billing Panel allowing search of placed orders, bill generation, and payments processing.
 * Demonstrates Abstraction (Interfaces), Polymorphism, and HTML-based UI rendering.
 */
public class BillingPanel extends JPanel {
    private Restaurant restaurant;
    private JTextField txtOrderId;
    private JEditorPane invoicePane;
    private JButton btnPay;
    private Order currentOrder;

    public BillingPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Billing & Payments");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(15, 23, 42));
        JLabel subtitleLabel = new JLabel("Generate customer invoices, manage totals, and process credit card/UPI transactions");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Control Row (Order ID search)
        JPanel controlRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        controlRow.setBackground(Color.WHITE);
        controlRow.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));

        JLabel lblOrderId = new JLabel("Enter Order ID:");
        lblOrderId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblOrderId.setForeground(new Color(71, 85, 105));
        controlRow.add(lblOrderId);

        txtOrderId = new JTextField();
        txtOrderId.setPreferredSize(new Dimension(120, 32));
        controlRow.add(txtOrderId);

        JButton btnSearch = new JButton("Generate Bill");
        btnSearch.setPreferredSize(new Dimension(130, 32));
        btnSearch.setBackground(new Color(37, 99, 235));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        controlRow.add(btnSearch);

        btnPay = new JButton("Process Payment");
        btnPay.setPreferredSize(new Dimension(160, 32));
        btnPay.setBackground(new Color(22, 163, 74));
        btnPay.setForeground(Color.WHITE);
        btnPay.setEnabled(false);
        btnPay.setFocusPainted(false);
        controlRow.add(btnPay);

        add(controlRow, BorderLayout.NORTH);

        // Main Center Area (Invoice Display Panel)
        JPanel invoiceWrapper = new JPanel(new BorderLayout());
        invoiceWrapper.setBackground(Color.WHITE);
        invoiceWrapper.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(24, 24, 24, 24)
        ));

        invoicePane = new JEditorPane();
        invoicePane.setEditable(false);
        invoicePane.setContentType("text/html");
        invoicePane.setText("<html><body style='font-family:Segoe UI,Arial; color:#64748b; text-align:center;'><br><br><h3>Enter a valid Order ID above and click \"Generate Bill\" to view the invoice details.</h3></body></html>");

        JScrollPane invoiceScroll = new JScrollPane(invoicePane);
        invoiceScroll.setBorder(null);
        invoiceWrapper.add(invoiceScroll, BorderLayout.CENTER);

        add(invoiceWrapper, BorderLayout.CENTER);

        // Event Triggers
        btnSearch.addActionListener(e -> searchOrderBill());
        btnPay.addActionListener(e -> processOrderPayment());
    }

    /**
     * Programmatically triggers bill look up (called when redirected from Place Order)
     */
    public void searchOrderBill(int orderId) {
        txtOrderId.setText(String.valueOf(orderId));
        searchOrderBill();
    }

    private void searchOrderBill() {
        String idStr = txtOrderId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Order ID.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int orderId = Integer.parseInt(idStr);
            Order order = restaurant.findOrderById(orderId);
            if (order != null) {
                currentOrder = order;
                renderHtmlInvoice(order);
                btnPay.setEnabled(!order.isPaid());
            } else {
                currentOrder = null;
                invoicePane.setText("<html><body style='font-family:Segoe UI,Arial; color:#dc2626; text-align:center;'><br><br><h3>Order ID " + orderId + " not found in the database.</h3></body></html>");
                btnPay.setEnabled(false);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Order ID must be a valid integer number.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Displays HTML representation of the invoice
    private void renderHtmlInvoice(Order order) {
        StringBuilder itemsHtml = new StringBuilder();
        FoodItem[] items = order.getOrderedItems();
        for (int i = 0; i < items.length; i++) {
            itemsHtml.append(String.format(
                "<tr>" +
                "  <td style='padding: 8px 0; border-bottom: 1px solid #f1f5f9; color:#334155;'>%d. %s</td>" +
                "  <td style='padding: 8px 0; border-bottom: 1px solid #f1f5f9; color:#475569; text-align:right;'>₹%.2f</td>" +
                "</tr>",
                (i + 1), items[i].getItemName(), items[i].getPrice()
            ));
        }

        String paymentBadge = order.isPaid() 
            ? "<span style='background-color:#dcfce7; color:#16a34a; padding: 4px 10px; border-radius: 4px; font-weight:bold; font-size:12px;'>PAID</span>" 
            : "<span style='background-color:#fee2e2; color:#dc2626; padding: 4px 10px; border-radius: 4px; font-weight:bold; font-size:12px;'>UNPAID</span>";

        String htmlContent = String.format(
            "<html>" +
            "<body style='font-family:Segoe UI,Arial; color:#334155; margin:0; padding:10px;'>" +
            "  <table width='100%%' cellpadding='0' cellspacing='0'>" +
            "    <tr>" +
            "      <td>" +
            "        <h2 style='margin:0; color:#1e293b;'>INVOICE</h2>" +
            "        <p style='margin:4px 0; color:#64748b; font-size:13px;'>Order ID: #%d | Status: %s</p>" +
            "      </td>" +
            "      <td style='text-align:right; font-size:13px; color:#64748b;'>" +
            "        <strong style='color:#0f172a;'>Smart Restaurant Inc.</strong><br>" +
            "        123 Culinary Boulevard<br>" +
            "        Gourmet City, GC 9401" +
            "      </td>" +
            "    </tr>" +
            "    <tr>" +
            "      <td colspan='2' style='padding:16px 0; border-bottom:2px solid #e2e8f0;'>" +
            "        <strong style='font-size:14px; color:#0f172a;'>Billed To:</strong><br>" +
            "        Name: %s<br>" +
            "        Contact: %s" +
            "      </td>" +
            "    </tr>" +
            "    <tr>" +
            "      <td colspan='2' style='padding:10px 0;'>" +
            "        <table width='100%%' cellpadding='0' cellspacing='0' style='font-size:14px;'>" +
            "          <tr style='font-weight:bold; color:#475569;'>" +
            "            <td style='padding: 8px 0; border-bottom: 2px solid #cbd5e1;'>Item Description</td>" +
            "            <td style='padding: 8px 0; border-bottom: 2px solid #cbd5e1; text-align:right;'>Price</td>" +
            "          </tr>" +
            "          %s" +
            "        </table>" +
            "      </td>" +
            "    </tr>" +
            "    <tr>" +
            "      <td colspan='2' style='padding-top:16px; border-top:2px solid #e2e8f0;'>" +
            "        <table width='40%%' align='right' cellpadding='0' cellspacing='0' style='font-size:14px;'>" +
            "          <tr>" +
            "            <td style='padding:4px 0; color:#64748b;'>Subtotal:</td>" +
            "            <td style='padding:4px 0; text-align:right; color:#475569;'>₹%.2f</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style='padding:4px 0; color:#dc2626;'>Discount:</td>" +
            "            <td style='padding:4px 0; text-align:right; color:#dc2626;'>-₹%.2f</td>" +
            "          </tr>" +
            "          <tr style='font-weight:bold; font-size:16px; color:#0f172a;'>" +
            "            <td style='padding:8px 0; border-top:1px solid #e2e8f0;'>Net Total:</td>" +
            "            <td style='padding:8px 0; text-align:right; border-top:1px solid #e2e8f0; color:#16a34a;'>₹%.2f</td>" +
            "          </tr>" +
            "        </table>" +
            "      </td>" +
            "    </tr>" +
            "  </table>" +
            "</body>" +
            "</html>",
            order.getOrderId(), paymentBadge,
            order.getCustomer().getName(), order.getCustomer().getContact(),
            itemsHtml.toString(),
            order.getTotalAmount(), order.getDiscountAmount(), order.getFinalAmount()
        );

        invoicePane.setText(htmlContent);
    }

    // Handles payment processing via dialog selections
    private void processOrderPayment() {
        if (currentOrder == null) return;

        if (currentOrder.isPaid()) {
            JOptionPane.showMessageDialog(this, "Order is already paid.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] methods = {"UPI VPA", "Credit/Debit Card", "Cash at Counter"};
        String method = (String) JOptionPane.showInputDialog(
            this, 
            String.format("Order Total: ₹%.2f\nSelect Payment Gateway Mode:", currentOrder.getFinalAmount()), 
            "Select Payment Method", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            methods, 
            methods[0]
        );

        if (method == null) return; // User cancelled

        PaymentService paymentService = null;

        if (method.equals("UPI VPA")) {
            String upiId = JOptionPane.showInputDialog(this, "Enter UPI VPA address (e.g. diner@okaxis):", "UPI Input Required", JOptionPane.PLAIN_MESSAGE);
            if (upiId == null) return; // Cancelled
            if (upiId.trim().isEmpty() || !upiId.contains("@")) {
                JOptionPane.showMessageDialog(this, "Invalid UPI address. Payment cancelled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            paymentService = new UPIPayment(upiId.trim());
        } 
        else if (method.equals("Credit/Debit Card")) {
            String cardNo = JOptionPane.showInputDialog(this, "Enter 16-digit Card Number:", "Card Input Required", JOptionPane.PLAIN_MESSAGE);
            if (cardNo == null) return;
            cardNo = cardNo.trim();
            if (cardNo.length() < 12) {
                JOptionPane.showMessageDialog(this, "Invalid card length. Payment cancelled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String[] cardTypes = {"Debit Card", "Credit Card"};
            String cardType = (String) JOptionPane.showInputDialog(this, "Select Card Account Type:", "Account Type", JOptionPane.QUESTION_MESSAGE, null, cardTypes, cardTypes[0]);
            if (cardType == null) return;
            
            paymentService = new CardPayment(cardNo, cardType);
        } 
        else {
            paymentService = new CashPayment();
        }

        // Polymorphic Call
        paymentService.pay(currentOrder.getFinalAmount());

        // Update database
        currentOrder.setPaid(true);
        restaurant.addSalesRecord(currentOrder.getFinalAmount());

        // Refresh panel UI
        renderHtmlInvoice(currentOrder);
        btnPay.setEnabled(false);
    }
}
