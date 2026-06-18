package com.restaurant.utility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles application-wide Toast messages and log feeds.
 * Displays temporary sliding alerts in the bottom right corner of the screen.
 */
public class NotificationManager {
    private static final List<String> notificationsLog = new ArrayList<>();
    private static Runnable refreshCallback;

    public static void setRefreshCallback(Runnable callback) {
        refreshCallback = callback;
    }

    public static synchronized void addNotification(String message) {
        notificationsLog.add(message);
        if (refreshCallback != null) {
            SwingUtilities.invokeLater(refreshCallback);
        }
        showToast(message);
    }

    public static synchronized List<String> getNotifications() {
        return new ArrayList<>(notificationsLog);
    }

    public static synchronized int getUnreadCount() {
        return notificationsLog.size();
    }

    public static synchronized void clearAll() {
        notificationsLog.clear();
        if (refreshCallback != null) {
            refreshCallback.run();
        }
    }

    /**
     * Spawns a beautiful, smooth-fading borderless popup window at the bottom right.
     */
    private static void showToast(String message) {
        SwingUtilities.invokeLater(() -> {
            JWindow toast = new JWindow();
            toast.setLayout(new BorderLayout());
            
            // Slate style theme matching
            JPanel panel = new JPanel(new BorderLayout(8, 8));
            panel.setBackground(new Color(15, 23, 42)); // Slate-900 bg
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(37, 99, 235), 2, true), // Primary Blue outline
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
            ));

            JLabel lblMsg = new JLabel("<html><body style='width: 200px;'>" + message + "</body></html>");
            lblMsg.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblMsg.setForeground(Color.WHITE);
            panel.add(lblMsg, BorderLayout.CENTER);
            
            toast.add(panel);
            toast.pack();

            // Calculate screen coordinates for bottom right
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle screenRect = ge.getMaximumWindowBounds();
            int x = screenRect.x + screenRect.width - toast.getWidth() - 20;
            int y = screenRect.y + screenRect.height - toast.getHeight() - 20;
            
            toast.setLocation(x, y);
            toast.setAlwaysOnTop(true);
            toast.setVisible(true);

            // Simple timer thread to fade out/close toast
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
                SwingUtilities.invokeLater(() -> {
                    toast.setVisible(false);
                    toast.dispose();
                });
            }).start();
        });
    }
}
