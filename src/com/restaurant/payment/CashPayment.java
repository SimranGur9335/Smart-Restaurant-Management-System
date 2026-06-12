package com.restaurant.payment;

import javax.swing.JOptionPane;

/**
 * Implementation of PaymentService for Cash Payments.
 * Displays a GUI notification dialog.
 */
public class CashPayment implements PaymentService {

    @Override
    public void pay(double amount) {
        String msg = String.format(
            "--- CASH PAYMENT SIMULATION ---\n" +
            "Amount Received: ₹%.2f at the counter\n" +
            "Status: Register updated\n" +
            "Transaction Successful!", 
            amount
        );
        JOptionPane.showMessageDialog(null, msg, "Cash Payment Status", JOptionPane.INFORMATION_MESSAGE);
    }
}
