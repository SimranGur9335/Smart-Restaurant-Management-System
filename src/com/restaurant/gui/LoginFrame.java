package com.restaurant.gui;

import com.restaurant.utility.LocalStorage;
import com.restaurant.utility.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication dialog frame that prompts users for credentials
 * and launches the main app based on their role permissions.
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    // Mock user credential database: Username -> (Role, Password)
    private static final Map<String, UserCredentials> userDb = new HashMap<>();

    static {
        userDb.put("admin", new UserCredentials("Admin", "admin"));
        userDb.put("manager", new UserCredentials("Manager", "manager"));
        userDb.put("waiter", new UserCredentials("Waiter", "waiter"));
        userDb.put("chef", new UserCredentials("Chef", "chef"));
        userDb.put("cashier", new UserCredentials("Cashier", "cashier"));
    }

    private static class UserCredentials {
        String role;
        String password;

        UserCredentials(String role, String password) {
            this.role = role;
            this.password = password;
        }
    }

    public LoginFrame() {
        setTitle("Smart Bistro - Login Gateway");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main theme colors
        Color primaryBlue = Theme.getPrimaryBlue();
        Color bgDark = new Color(15, 23, 42); // Slate 900
        Color fieldBg = new Color(248, 250, 252);
        
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bgDark);
        root.setBorder(new EmptyBorder(32, 32, 32, 32));

        // Top Brand Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        headerPanel.setBackground(bgDark);
        
        JLabel brandName = new JLabel("SMART BISTRO", SwingConstants.CENTER);
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandName.setForeground(Color.WHITE);
        
        JLabel brandSub = new JLabel("Restaurant Operations Control Panel", SwingConstants.CENTER);
        brandSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        brandSub.setForeground(new Color(148, 163, 184)); // Slate 400
        
        headerPanel.add(brandName);
        headerPanel.add(brandSub);
        root.add(headerPanel, BorderLayout.NORTH);

        // Center card form
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(51, 65, 85), 1, true),
            new EmptyBorder(24, 24, 24, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Form Title
        JLabel title = new JLabel("Authorized Sign In");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(15, 23, 42));
        gbc.gridy = 0;
        cardPanel.add(title, gbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(241, 245, 249));
        gbc.gridy = 1;
        cardPanel.add(sep, gbc);

        // Username
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(100, 116, 139));
        gbc.gridy = 2;
        cardPanel.add(lblUser, gbc);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(0, 36));
        txtUsername.setBackground(fieldBg);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(0, 8, 0, 8)
        ));
        gbc.gridy = 3;
        cardPanel.add(txtUsername, gbc);

        // Password
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(100, 116, 139));
        gbc.gridy = 4;
        cardPanel.add(lblPass, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(0, 36));
        txtPassword.setBackground(fieldBg);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(0, 8, 0, 8)
        ));
        gbc.gridy = 5;
        cardPanel.add(txtPassword, gbc);

        // Button
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(primaryBlue);
        btnLogin.setPreferredSize(new Dimension(0, 40));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.insets = new Insets(16, 0, 0, 0);
        cardPanel.add(btnLogin, gbc);

        root.add(cardPanel, BorderLayout.CENTER);

        // Footer instructions info
        JLabel tipsLabel = new JLabel("<html><center>Default credentials: admin/admin, manager/manager,<br>chef/chef, waiter/waiter, cashier/cashier</center></html>", SwingConstants.CENTER);
        tipsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tipsLabel.setForeground(new Color(148, 163, 184));
        tipsLabel.setBorder(new EmptyBorder(16, 0, 0, 0));
        root.add(tipsLabel, BorderLayout.SOUTH);

        setContentPane(root);

        // Submit action
        btnLogin.addActionListener(e -> attemptLogin());
        txtPassword.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String username = txtUsername.getText().trim().toLowerCase();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Required Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userDb.containsKey(username)) {
            UserCredentials cred = userDb.get(username);
            if (cred.password.equals(password)) {
                // Save session role
                LocalStorage.setItem("currentUser", cred.role);
                LocalStorage.setItem("currentUsername", txtUsername.getText().trim());

                // Launch Dashboard
                SwingUtilities.invokeLater(() -> {
                    new DashboardFrame().setVisible(true);
                });

                // Dispose login frame
                this.dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Sign In Error", JOptionPane.ERROR_MESSAGE);
    }
}
