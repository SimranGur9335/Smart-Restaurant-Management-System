package com.restaurant.model;

/**
 * Model class representing a physical dining table.
 */
public class Table {
    private int tableId;
    private int capacity;
    private String status; // "Available", "Reserved", "Occupied"
    private int assignedCustomerId; // -1 if none

    public Table(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.status = "Available";
        this.assignedCustomerId = -1;
    }

    public Table(int tableId, int capacity, String status, int assignedCustomerId) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.status = status;
        this.assignedCustomerId = assignedCustomerId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAssignedCustomerId() {
        return assignedCustomerId;
    }

    public void setAssignedCustomerId(int assignedCustomerId) {
        this.assignedCustomerId = assignedCustomerId;
    }
}
