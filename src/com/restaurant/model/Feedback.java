package com.restaurant.model;

/**
 * Model representing diner review submissions.
 */
public class Feedback {
    private int feedbackId;
    private String customerName;
    private int rating; // 1 to 5 stars
    private String comments;
    private String date; // "YYYY-MM-DD"

    public Feedback(int feedbackId, String customerName, int rating, String comments, String date) {
        this.feedbackId = feedbackId;
        this.customerName = customerName;
        this.rating = rating;
        this.comments = comments;
        this.date = date;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments != null ? comments : "";
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
