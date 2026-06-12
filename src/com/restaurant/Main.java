package com.restaurant;

import com.restaurant.gui.DashboardFrame;
import javax.swing.SwingUtilities;

/**
 * Main launcher class for the Smart Restaurant Management System.
 * Initializes and displays the modern Java Swing GUI application.
 */
public class Main {
    public static void main(String[] args) {
        // Launch the graphical dashboard frame inside the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new DashboardFrame().setVisible(true);
        });
    }
}
