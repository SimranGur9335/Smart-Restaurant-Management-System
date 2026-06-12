package com.restaurant.payment;

import javax.swing.JOptionPane;

/**
 * Implementation of PaymentService for UPI Payments.
 * Displays a GUI notification dialog.
 */
public class UPIPayment implements PaymentService {
    private String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public void pay(double amount) {
        String msg = String.format(
            "--- UPI PAYMENT SIMULATION ---\n" +
            "UPI VPA: %s\n" +
            "Amount: ₹%.2f\n" +
            "Status: Connecting to UPI Gateway...\n" +
            "Transaction Successful!", 
            upiId, amount
        );
        JOptionPane.showMessageDialog(null, msg, "UPI Payment Status", JOptionPane.INFORMATION_MESSAGE);
    }
}
