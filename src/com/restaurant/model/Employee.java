package com.restaurant.model;

/**
 * Model representing restaurant staff members.
 */
public class Employee {
    private int employeeId;
    private String name;
    private String role; // "Manager", "Waiter", "Chef", "Cashier"
    private String contact;
    private String shift; // "Morning", "Evening", "Night"

    public Employee(int employeeId, String name, String role, String contact, String shift) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
        this.contact = contact;
        this.shift = shift;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
}
