package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Order;
import com.restaurant.model.Customer;
import com.restaurant.model.FoodItem;
import com.restaurant.payment.*;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Billing Panel allowing search of placed orders, bill generation, and payments processing.
 * Integrates GST taxes, coupon discounts, local file printing, and refunds tracking.
 */
public class BillingPanel extends JPanel {
    private Restaurant restaurant;
    private JTextField txtOrderId;
    private JTextField txtCouponCode;
    private JEditorPane invoicePane;
    private JButton btnPay;
    private JButton btnRefund;
    private JButton btnPrint;
    private Order currentOrder;

    public BillingPanel(Restaurant restaurant) {
        this.restaurant = restaurant;
        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.getBg());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        JLabel titleLabel = new JLabel("Billing & Payments");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Theme.getFg());
        JLabel subtitleLabel = new JLabel("Process transaction checkouts, calculate GST taxes, apply coupon discounts, and print bills");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getSubFg());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Control Row (Inputs & Actions)
        JPanel controlRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        controlRow.setBackground(Theme.getPanelBg());
        controlRow.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));

        JLabel lblOrderId = new JLabel("Enter Order ID:");
        lblOrderId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblOrderId.setForeground(Theme.getFg());
        controlRow.add(lblOrderId);

        txtOrderId = new JTextField();
        txtOrderId.setPreferredSize(new Dimension(80, 32));
        controlRow.add(txtOrderId);

        JButton btnSearch = new JButton("Generate Bill");
        btnSearch.setPreferredSize(new Dimension(110, 32));
        btnSearch.setBackground(Theme.getPrimaryBlue());
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        controlRow.add(btnSearch);

        // Coupon Section
        JLabel lblCoupon = new JLabel("Coupon Code:");
        lblCoupon.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCoupon.setForeground(Theme.getFg());
        controlRow.add(lblCoupon);

        txtCouponCode = new JTextField();
        txtCouponCode.setPreferredSize(new Dimension(90, 32));
        controlRow.add(txtCouponCode);

        JButton btnCoupon = new JButton("Apply");
        btnCoupon.setPreferredSize(new Dimension(80, 32));
        btnCoupon.setBackground(Theme.getPrimaryBlue());
        btnCoupon.setForeground(Color.WHITE);
        btnCoupon.setFocusPainted(false);
        controlRow.add(btnCoupon);

        btnPay = new JButton("Process Payment");
        btnPay.setPreferredSize(new Dimension(140, 32));
        btnPay.setBackground(Theme.getPrimaryGreen());
        btnPay.setForeground(Color.WHITE);
        btnPay.setEnabled(false);
        btnPay.setFocusPainted(false);
        controlRow.add(btnPay);

        btnRefund = new JButton("Refund Transaction");
        btnRefund.setPreferredSize(new Dimension(150, 32));
        btnRefund.setBackground(Theme.getPrimaryRed());
        btnRefund.setForeground(Color.WHITE);
        btnRefund.setEnabled(false);
        btnRefund.setFocusPainted(false);
        controlRow.add(btnRefund);

        btnPrint = new JButton("Print Invoice");
        btnPrint.setPreferredSize(new Dimension(110, 32));
        btnPrint.setBackground(new Color(100, 116, 139)); // Slate gray
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setEnabled(false);
        btnPrint.setFocusPainted(false);
        controlRow.add(btnPrint);

        add(controlRow, BorderLayout.NORTH);

        // Main Center Area (Invoice Display Panel)
        JPanel invoiceWrapper = new JPanel(new BorderLayout());
        invoiceWrapper.setBackground(Theme.getPanelBg());
        invoiceWrapper.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.getBorderColor(), 1, true),
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
        btnCoupon.addActionListener(e -> applyCouponDiscount());
        btnPay.addActionListener(e -> processOrderPayment());
        btnRefund.addActionListener(e -> processOrderRefund());
        btnPrint.addActionListener(e -> printInvoiceLocal());
    }

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
                txtCouponCode.setText(order.getCouponCode());
                renderHtmlInvoice(order);
                btnPay.setEnabled(!order.isPaid() && !order.getOrderStatus().equalsIgnoreCase("Cancelled") && !order.getOrderStatus().equalsIgnoreCase("Refunded"));
                btnRefund.setEnabled(order.isPaid() && !order.getOrderStatus().equalsIgnoreCase("Refunded"));
                btnPrint.setEnabled(true);
            } else {
                currentOrder = null;
                invoicePane.setText("<html><body style='font-family:Segoe UI,Arial; color:#dc2626; text-align:center;'><br><br><h3>Order ID " + orderId + " not found in the database.</h3></body></html>");
                btnPay.setEnabled(false);
                btnRefund.setEnabled(false);
                btnPrint.setEnabled(false);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Order ID must be a valid integer number.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyCouponDiscount() {
        if (currentOrder == null) {
            JOptionPane.showMessageDialog(this, "Generate a bill first before applying coupons.", "Action Restricted", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentOrder.isPaid()) {
            JOptionPane.showMessageDialog(this, "Paid orders cannot accept new coupon codes.", "Action Restricted", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String code = txtCouponCode.getText().trim().toUpperCase();
        double discount = 0.0;

        if (code.isEmpty()) {
            currentOrder.setCouponCode("");
            currentOrder.setCouponDiscount(0.0);
            restaurant.updateOrder(currentOrder);
            renderHtmlInvoice(currentOrder);
            JOptionPane.showMessageDialog(this, "Coupon cleared.", "Updated", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (code.equals("BISTRO50")) {
            discount = 50.0;
        } else if (code.equals("BISTRO100")) {
            discount = 100.0;
        } else if (code.equals("BISTRO15")) {
            // 15% coupon
            discount = (currentOrder.getTotalAmount() - currentOrder.getDiscountAmount()) * 0.15;
        } else {
            JOptionPane.showMessageDialog(this, "Invalid coupon code! Try BISTRO50, BISTRO100, or BISTRO15.", "Invalid Coupon", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentOrder.setCouponCode(code);
        currentOrder.setCouponDiscount(discount);
        restaurant.updateOrder(currentOrder);
        renderHtmlInvoice(currentOrder);

        JOptionPane.showMessageDialog(this, String.format("Coupon '%s' applied successfully! Discount: ₹%.2f", code, discount), "Coupon Applied", JOptionPane.INFORMATION_MESSAGE);
    }

    private void renderHtmlInvoice(Order order) {
        StringBuilder itemsHtml = new StringBuilder();
        FoodItem[] items = order.getOrderedItems();
        for (int i = 0; i < items.length; i++) {
            itemsHtml.append(String.format(
                "<tr>" +
                "  <td style='padding: 8px 0; border-bottom: 1px solid #e2e8f0; color:#334155;'>%d. %s</td>" +
                "  <td style='padding: 8px 0; border-bottom: 1px solid #e2e8f0; color:#475569; text-align:right;'>₹%.2f</td>" +
                "</tr>",
                (i + 1), items[i].getItemName(), items[i].getPrice()
            ));
        }

        String paymentBadge;
        if (order.getOrderStatus().equalsIgnoreCase("Refunded")) {
            paymentBadge = "<span style='background-color:#fee2e2; color:#dc2626; padding: 4px 10px; border-radius: 4px; font-weight:bold; font-size:12px;'>REFUNDED</span>";
        } else if (order.isPaid()) {
            paymentBadge = "<span style='background-color:#dcfce7; color:#16a34a; padding: 4px 10px; border-radius: 4px; font-weight:bold; font-size:12px;'>PAID</span>";
        } else {
            paymentBadge = "<span style='background-color:#fee2e2; color:#dc2626; padding: 4px 10px; border-radius: 4px; font-weight:bold; font-size:12px;'>UNPAID</span>";
        }

        String htmlContent = String.format(
            "<html>" +
            "<body style='font-family:Segoe UI,Arial; color:#334155; margin:0; padding:10px; background-color:#ffffff;'>" +
            "  <table width='100%%' cellpadding='0' cellspacing='0'>" +
            "    <tr>" +
            "      <td>" +
            "        <h2 style='margin:0; color:#1e293b;'>INVOICE BILL</h2>" +
            "        <p style='margin:4px 0; color:#64748b; font-size:13px;'>Order ID: #%d | Type: %s | Status: %s</p>" +
            "      </td>" +
            "      <td style='text-align:right; font-size:13px; color:#64748b;'>" +
            "        <strong style='color:#0f172a;'>Smart Bistro Inc.</strong><br>" +
            "        123 Culinary Boulevard<br>" +
            "        Gourmet City, GC 9401" +
            "      </td>" +
            "    </tr>" +
            "    <tr>" +
            "      <td colspan='2' style='padding:16px 0; border-bottom:2px solid #e2e8f0;'>" +
            "        <strong style='font-size:14px; color:#0f172a;'>Billed To:</strong><br>" +
            "        Diner Name: %s<br>" +
            "        Contact No: %s" +
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
            "        <table width='45%%' align='right' cellpadding='0' cellspacing='0' style='font-size:14px;'>" +
            "          <tr>" +
            "            <td style='padding:4px 0; color:#64748b;'>Subtotal:</td>" +
            "            <td style='padding:4px 0; text-align:right; color:#475569;'>₹%.2f</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style='padding:4px 0; color:#dc2626;'>Tier Discount:</td>" +
            "            <td style='padding:4px 0; text-align:right; color:#dc2626;'>-₹%.2f</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style='padding:4px 0; color:#dc2626;'>Coupon Discount:</td>" +
            "            <td style='padding:4px 0; text-align:right; color:#dc2626;'>-₹%.2f</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style='padding:4px 0; color:#64748b;'>GST (5%%):</td>" +
            "            <td style='padding:4px 0; text-align:right; color:#475569;'>₹%.2f</td>" +
            "          </tr>" +
            "          <tr style='font-weight:bold; font-size:16px; color:#0f172a;'>" +
            "            <td style='padding:8px 0; border-top:1px solid #e2e8f0;'>Grand Total:</td>" +
            "            <td style='padding:8px 0; text-align:right; border-top:1px solid #e2e8f0; color:#16a34a;'>₹%.2f</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td colspan='2' style='text-align:right; padding-top:8px;'>%s</td>" +
            "          </tr>" +
            "        </table>" +
            "      </td>" +
            "    </tr>" +
            "  </table>" +
            "</body>" +
            "</html>",
            order.getOrderId(), order.getOrderType(), order.getOrderStatus(),
            order.getCustomer().getName(), order.getCustomer().getContact(),
            itemsHtml.toString(),
            order.getTotalAmount(), order.getDiscountAmount(), order.getCouponDiscount(),
            order.getGst(), order.getGrandTotal(), paymentBadge
        );

        invoicePane.setText(htmlContent);
    }

    private void processOrderPayment() {
        if (currentOrder == null) return;

        if (currentOrder.isPaid()) {
            JOptionPane.showMessageDialog(this, "Order is already paid.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] methods = {"UPI VPA Gateway", "Credit/Debit Card Terminal", "Cash at Counter"};
        String method = (String) JOptionPane.showInputDialog(
            this, 
            String.format("Order Total: ₹%.2f\nSelect Payment Gateway Mode:", currentOrder.getGrandTotal()), 
            "Select Payment Method", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            methods, 
            methods[0]
        );

        if (method == null) return; // Cancelled

        PaymentService paymentService;

        if (method.equals("UPI VPA Gateway")) {
            String upiId = JOptionPane.showInputDialog(this, "Enter UPI ID (e.g. diner@okaxis):", "UPI Input Required", JOptionPane.PLAIN_MESSAGE);
            if (upiId == null) return;
            if (upiId.trim().isEmpty() || !upiId.contains("@")) {
                JOptionPane.showMessageDialog(this, "Invalid UPI ID. Transaction cancelled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            paymentService = new UPIPayment(upiId.trim());
        } 
        else if (method.equals("Credit/Debit Card Terminal")) {
            String cardNo = JOptionPane.showInputDialog(this, "Enter 16-digit Card Number:", "Card Input Required", JOptionPane.PLAIN_MESSAGE);
            if (cardNo == null) return;
            cardNo = cardNo.trim();
            if (cardNo.length() < 12) {
                JOptionPane.showMessageDialog(this, "Invalid card length. Transaction cancelled.", "Input Error", JOptionPane.ERROR_MESSAGE);
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

        // Process Simulation
        paymentService.pay(currentOrder.getGrandTotal());

        // Update details
        currentOrder.setPaid(true);
        currentOrder.setOrderStatus("Served"); // Update status to Served on payment complete
        
        // Add to sales record
        restaurant.addSalesRecord(currentOrder.getGrandTotal());

        // Release Table if applicable
        if (currentOrder.getTableId() != -1) {
            com.restaurant.model.Table tbl = restaurant.findTableById(currentOrder.getTableId());
            if (tbl != null) {
                tbl.setStatus("Available");
                tbl.setAssignedCustomerId(-1);
                restaurant.updateTable(tbl);
            }
        }

        // Calculate Reward Points: 1 point per ₹100 spent
        int pointsEarned = (int) (currentOrder.getGrandTotal() / 100);
        Customer customer = currentOrder.getCustomer();
        customer.addRewardPoints(pointsEarned);
        restaurant.updateCustomer(customer); // Saves customers

        // Sync orders
        restaurant.updateOrder(currentOrder);

        // Notify
        NotificationManager.addNotification(String.format("Order #%d Paid! %s earned %d reward points.", currentOrder.getOrderId(), customer.getName(), pointsEarned));

        String welcomeMsg = String.format("Payment Successful!\nCustomer: %s\nLoyalty Points Earned: %d\nCurrent Membership Tier: %s",
            customer.getName(), pointsEarned, customer.getMembershipLevel());
        JOptionPane.showMessageDialog(this, welcomeMsg, "Checkout Completed", JOptionPane.INFORMATION_MESSAGE);

        // Refresh UI
        searchOrderBill();
    }

    private void processOrderRefund() {
        if (currentOrder == null) return;

        if (!currentOrder.isPaid()) {
            JOptionPane.showMessageDialog(this, "Unpaid orders cannot be refunded.", "Refund Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to process a full refund for Order #" + currentOrder.getOrderId() + " of value ₹" + String.format("%.2f", currentOrder.getGrandTotal()) + "?", "Confirm Refund", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            currentOrder.setPaid(false);
            currentOrder.setOrderStatus("Refunded");
            
            // Register a negative sale record to adjust revenue metrics
            restaurant.addSalesRecord(-currentOrder.getGrandTotal());
            
            // Release Table if it wasn't done yet
            if (currentOrder.getTableId() != -1) {
                com.restaurant.model.Table tbl = restaurant.findTableById(currentOrder.getTableId());
                if (tbl != null) {
                    tbl.setStatus("Available");
                    tbl.setAssignedCustomerId(-1);
                    restaurant.updateTable(tbl);
                }
            }

            // Sync
            restaurant.updateOrder(currentOrder);

            NotificationManager.addNotification("Refund registered for Order #" + currentOrder.getOrderId());
            JOptionPane.showMessageDialog(this, "Transaction refunded successfully. Sales ledger has been adjusted.", "Refund Complete", JOptionPane.INFORMATION_MESSAGE);
            
            searchOrderBill();
        }
    }

    private void printInvoiceLocal() {
        if (currentOrder == null) return;

        File invoiceDir = new File("invoices");
        invoiceDir.mkdirs();

        File invoiceFile = new File(invoiceDir, "invoice_" + currentOrder.getOrderId() + ".html");

        try (FileWriter writer = new FileWriter(invoiceFile)) {
            writer.write(invoicePane.getText());
            
            JOptionPane.showMessageDialog(this, 
                String.format("Invoice printed successfully!\nSaved to: %s", invoiceFile.getAbsolutePath()), 
                "Invoice Printed", 
                JOptionPane.INFORMATION_MESSAGE
            );

            NotificationManager.addNotification("Invoice #" + currentOrder.getOrderId() + " printed to HTML!");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to print invoice file: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
