package com.restaurant.utility;

import java.awt.Color;

/**
 * Handles application coloring for Light and Dark themes.
 * Integrates with LocalStorage for setting persistence.
 */
public class Theme {
    public static final int LIGHT = 0;
    public static final int DARK = 1;

    private static int currentMode = LIGHT;

    static {
        String savedTheme = LocalStorage.getItem("theme");
        if ("dark".equalsIgnoreCase(savedTheme)) {
            currentMode = DARK;
        }
    }

    public static int getMode() {
        return currentMode;
    }

    public static void toggleTheme() {
        setMode(currentMode == LIGHT ? DARK : LIGHT);
    }

    public static void setMode(int mode) {
        currentMode = mode;
        LocalStorage.setItem("theme", mode == DARK ? "dark" : "light");
    }

    public static Color getBg() {
        return currentMode == LIGHT ? new Color(248, 250, 252) : new Color(15, 23, 42);
    }

    public static Color getPanelBg() {
        return currentMode == LIGHT ? Color.WHITE : new Color(30, 41, 59);
    }

    public static Color getFg() {
        return currentMode == LIGHT ? new Color(15, 23, 42) : new Color(241, 245, 249);
    }

    public static Color getSubFg() {
        return currentMode == LIGHT ? new Color(100, 116, 139) : new Color(148, 163, 184);
    }

    public static Color getBorderColor() {
        return currentMode == LIGHT ? new Color(226, 232, 240) : new Color(51, 65, 85);
    }

    public static Color getSidebarBg() {
        return new Color(15, 23, 42); // Sidebar stays Slate-900 dark for aesthetic contrast
    }

    public static Color getPrimaryBlue() {
        return new Color(37, 99, 235);
    }

    public static Color getPrimaryGreen() {
        return new Color(22, 163, 74);
    }

    public static Color getPrimaryRed() {
        return new Color(220, 38, 38);
    }

    public static Color getTableSelectionBg() {
        return currentMode == LIGHT ? new Color(219, 234, 254) : new Color(71, 85, 105);
    }
}
