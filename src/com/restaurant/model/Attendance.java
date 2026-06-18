package com.restaurant.model;

/**
 * Model representing employee daily shift attendance logs.
 */
public class Attendance {
    private String date; // "YYYY-MM-DD"
    private int employeeId;
    private String clockInTime; // "HH:MM" or ""
    private String clockOutTime; // "HH:MM" or ""
    private boolean present;

    public Attendance(String date, int employeeId, String clockInTime, String clockOutTime, boolean present) {
        this.date = date;
        this.employeeId = employeeId;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.present = present;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getClockInTime() {
        return clockInTime != null ? clockInTime : "";
    }

    public void setClockInTime(String clockInTime) {
        this.clockInTime = clockInTime;
    }

    public String getClockOutTime() {
        return clockOutTime != null ? clockOutTime : "";
    }

    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
