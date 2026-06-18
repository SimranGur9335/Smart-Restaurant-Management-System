package com.restaurant.model;

/**
 * Model representing customer dining reservation logs.
 */
public class Reservation {
    private int reservationId;
    private String customerName;
    private String date; // "YYYY-MM-DD"
    private String time; // "HH:MM"
    private int guests;
    private int tableId;
    private String status; // "Pending", "Confirmed", "Cancelled"

    public Reservation(int reservationId, String customerName, String date, String time, int guests, int tableId) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.guests = guests;
        this.tableId = tableId;
        this.status = "Pending";
    }

    public Reservation(int reservationId, String customerName, String date, String time, int guests, int tableId, String status) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.guests = guests;
        this.tableId = tableId;
        this.status = status;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
