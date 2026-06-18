package com.restaurant;

import com.restaurant.gui.DashboardFrame;
import com.restaurant.gui.LoginFrame;
import com.restaurant.utility.LocalStorage;

import javax.swing.SwingUtilities;

/**
 * Main launcher class for the Smart Restaurant Management System.
 * Checks for stored authentication session and launches the appropriate Swing window.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String loggedUser = LocalStorage.getItem("currentUser");
            if (loggedUser != null && !loggedUser.trim().isEmpty()) {
                // Persistent session active
                new DashboardFrame().setVisible(true);
            } else {
                // Redirect to Login gate
                new LoginFrame().setVisible(true);
            }
        });
    }
}