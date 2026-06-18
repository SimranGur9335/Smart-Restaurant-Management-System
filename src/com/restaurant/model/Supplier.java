package com.restaurant.model;

/**
 * Model representing food/pantry suppliers.
 */
public class Supplier {
    private int supplierId;
    private String name;
    private String contact;
    private String address;
    private String products; // Comma-separated list or description

    public Supplier(int supplierId, String name, String contact, String address, String products) {
        this.supplierId = supplierId;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.products = products;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
