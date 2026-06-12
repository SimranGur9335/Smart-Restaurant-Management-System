package com.restaurant.payment;

import javax.swing.JOptionPane;

/**
 * Implementation of PaymentService for Credit/Debit Card Payments.
 * Displays a GUI notification dialog.
 */
public class CardPayment implements PaymentService {
    private String cardNumber;
    private String cardType;

    public CardPayment(String cardNumber, String cardType) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public void pay(double amount) {
        String maskedCard = "XXXX-XXXX-XXXX-" + (cardNumber.length() > 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber);
        String msg = String.format(
            "--- CARD PAYMENT SIMULATION ---\n" +
            "Card Type: %s\n" +
            "Card Number: %s\n" +
            "Amount: ₹%.2f\n" +
            "Status: Authorizing transaction...\n" +
            "Transaction Successful!", 
            cardType, maskedCard, amount
        );
        JOptionPane.showMessageDialog(null, msg, "Card Payment Status", JOptionPane.INFORMATION_MESSAGE);
    }
}
