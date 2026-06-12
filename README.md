# Smart Restaurant Management System

A modern desktop-based Restaurant Management System built using **Java Swing** and **Core Java** concepts. The application provides an intuitive graphical interface for managing customers, menu items, orders, billing, payments, and sales analytics without any terminal interaction.

## Project Highlights

* Fully GUI-Based Java Swing Application
* Professional Sidebar Navigation Layout
* Real-Time Dashboard Statistics
* Customer Management System
* Smart Order Processing
* Automated Billing & Payments
* Sales Analytics using Sliding Window Algorithm
* Search & Sort Operations
* Clean OOP Architecture
* INR (₹) Currency Support
* Exception Handling & Input Validation

---

## Features

### Dashboard

* View total customers
* View active menu items
* Track total orders
* Monitor total sales revenue
* Recent order activity timeline

### Customer Management

* Add new customers
* View customer records
* Contact validation
* Interactive JTable display

### Menu Management

* View complete menu
* Veg and Non-Veg categorization
* Search by Item Name (Linear Search)
* Search by Item ID (Binary Search)
* Sort by Price using:

  * Bubble Sort
  * Selection Sort
  * Insertion Sort

### Order Management

* Create customer orders
* Add/Remove menu items
* Automatic total calculation
* Discount handling

### Billing & Payments

* Professional invoice generation
* UPI Payment
* Card Payment
* Cash Payment
* Dynamic payment processing using interfaces

### Analytics

* Daily sales tracking
* Revenue analysis
* Sliding Window implementation
* Performance insights

---

## Technologies Used

| Technology           | Purpose                       |
| -------------------- | ----------------------------- |
| Java                 | Core Development              |
| Java Swing           | GUI Development               |
| OOP                  | Software Design               |
| Arrays               | Data Storage                  |
| Searching Algorithms | Item Lookup                   |
| Sorting Algorithms   | Data Organization             |
| Sliding Window       | Sales Analysis                |
| Exception Handling   | Validation & Error Management |

---

## Core Concepts Implemented

| Concept                | Implementation          |
| ---------------------- | ----------------------- |
| Conditional Statements | Discounts & Validations |
| Loops                  | Data Processing         |
| Arrays                 | Storage Management      |
| Linear Search          | Menu Search             |
| Binary Search          | Fast Item Lookup        |
| Bubble Sort            | Price Sorting           |
| Selection Sort         | Price Sorting           |
| Insertion Sort         | Price Sorting           |
| Sliding Window         | Revenue Analytics       |
| OOP                    | Classes & Objects       |
| Inheritance            | FoodItem Hierarchy      |
| Polymorphism           | Payment Processing      |
| Abstract Class         | FoodItem                |
| Interface              | PaymentService          |
| Exception Handling     | Input Validation        |

---

## Project Structure

```text
src/
│
├── model/
├── payment/
├── service/
├── utility/
├── exception/
├── gui/
└── Main.java
```

---

## Application Screens

* Dashboard
<img width="1536" height="816" alt="image" src="https://github.com/user-attachments/assets/b8e49a93-6eb8-40f0-8b6b-334499a02038" />

* Customer Management
<img width="1536" height="816" alt="image" src="https://github.com/user-attachments/assets/de9e5419-ac45-4c34-a595-4b8da826bf63" />

* Menu Management
<img width="1536" height="815" alt="image" src="https://github.com/user-attachments/assets/dda47464-8003-45ad-9fc9-a15547e9ed77" />

* Order Management
<img width="1536" height="815" alt="image" src="https://github.com/user-attachments/assets/4887d260-d10c-440c-badf-e38b3141c801" />

* Billing & Payments
<img width="1536" height="817" alt="image" src="https://github.com/user-attachments/assets/ec5b7eb1-b795-44d3-9927-e9d2e6be92a3" />

* Sales Analytics
<img width="1536" height="816" alt="image" src="https://github.com/user-attachments/assets/730a0089-3c51-4ad0-9a27-ce3fb55e3d1a" />

---

## Currency Support

All prices, invoices, analytics, and payment modules use:

```text
Indian Rupee (₹)
```

---

## How to Run

### Compile

```bash
javac -d bin src/com/restaurant/**/*.java
```

### Run

```bash
java -cp bin com.restaurant.Main
```

---

## Future Enhancements

* MySQL Database Integration
* Login & Authentication System
* Inventory Management
* QR Code Payments
* Online Food Ordering
* Report Export (PDF/Excel)
* Admin Dashboard

---

## Learning Outcomes

This project demonstrates practical implementation of:

* Java Swing GUI Development
* Data Structures & Algorithms
* Object-Oriented Programming
* Software Architecture
* Event-Driven Programming
* Real-World Application Development

---

## Author

**Gursimran Singh Saini**

Java Developer | AI/ML Enthusiast | Cybersecurity Enthusiast

Built as an academic project to demonstrate Core Java, GUI Development, OOP, Searching, Sorting, and Algorithmic Problem Solving.
