package com.restaurant.gui;

import com.restaurant.service.Restaurant;
import com.restaurant.model.Customer;
import com.restaurant.utility.Theme;
import com.restaurant.utility.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Modern star rating feedback dialog pop-up.
 */
public class FeedbackDialog extends JDialog {
    private Restaurant restaurant;
    private JComboBox<String> cmbCustomer;
    private int selectedStars = 5;
    private JTextArea txtComments;
    private JButton[] starBtns = new JButton[5];

    public FeedbackDialog(Frame owner, Restaurant restaurant) {
        super(owner, "Submit Diner Feedback", true);
        this.restaurant = restaurant;
        
        setSize(400, 360);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Theme.getPanelBg());
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title Header
        JLabel title = new JLabel("Diner Experience Feedback", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Theme.getFg());
        root.add(title, BorderLayout.NORTH);

        // Form fields
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.getPanelBg());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Diner Selection
        gbc.gridy = 0;
        JLabel lblCust = new JLabel("Select Customer:");
        lblCust.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCust.setForeground(Theme.getSubFg());
        form.add(lblCust, gbc);

        cmbCustomer = new JComboBox<>();
        Customer[] customers = restaurant.getCustomersCopy();
        for (Customer c : customers) {
            cmbCustomer.addItem(c.getName());
        }
        cmbCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbCustomer.setPreferredSize(new Dimension(0, 32));
        gbc.gridy = 1;
        form.add(cmbCustomer, gbc);

        // Rating Stars Row
        gbc.gridy = 2;
        JLabel lblStars = new JLabel("Rating Stars:");
        lblStars.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStars.setForeground(Theme.getSubFg());
        form.add(lblStars, gbc);

        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        starPanel.setBackground(Theme.getPanelBg());
        
        for (int i = 0; i < 5; i++) {
            final int starIndex = i + 1;
            JButton btn = new JButton("★");
            btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
            btn.setForeground(new Color(234, 179, 8)); // Golden Yellow
            btn.setBackground(Theme.getPanelBg());
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> updateStars(starIndex));
            starBtns[i] = btn;
            starPanel.add(btn);
        }
        gbc.gridy = 3;
        form.add(starPanel, gbc);

        // Comments text area
        gbc.gridy = 4;
        JLabel lblComm = new JLabel("Experience comments / notes:");
        lblComm.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblComm.setForeground(Theme.getSubFg());
        form.add(lblComm, gbc);

        txtComments = new JTextArea(4, 20);
        txtComments.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtComments.setLineWrap(true);
        txtComments.setWrapStyleWord(true);
        
        JScrollPane textScroll = new JScrollPane(txtComments);
        textScroll.setBorder(new LineBorder(Theme.getBorderColor(), 1, true));
        gbc.gridy = 5;
        form.add(textScroll, gbc);

        root.add(form, BorderLayout.CENTER);

        // Submit Button Row
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        bottom.setBackground(Theme.getPanelBg());
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancel.setBackground(new Color(100, 116, 139));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());
        
        JButton btnSubmit = new JButton("Submit Review");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSubmit.setBackground(Theme.getPrimaryBlue());
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.addActionListener(e -> submitReview());

        bottom.add(btnCancel);
        bottom.add(btnSubmit);
        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);
        updateStars(5); // Default to 5 stars
    }

    private void updateStars(int stars) {
        selectedStars = stars;
        for (int i = 0; i < 5; i++) {
            if (i < stars) {
                starBtns[i].setForeground(new Color(234, 179, 8)); // Gold
            } else {
                starBtns[i].setForeground(new Color(203, 213, 225)); // Slate 300
            }
        }
    }

    private void submitReview() {
        if (cmbCustomer.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "A registered customer is required.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = (String) cmbCustomer.getSelectedItem();
        String comments = txtComments.getText().trim();

        restaurant.addFeedback(name, selectedStars, comments);
        NotificationManager.addNotification("Feedback survey submitted by " + name);
        dispose();
    }
}
