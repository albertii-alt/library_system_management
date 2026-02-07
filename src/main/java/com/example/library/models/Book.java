package com.example.library.models;

public class Book {
    private String title;
    private String author;
    private boolean available; // ✅ true = available, false = borrowed

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true; // default is available
    }

    // --- Getters ---
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    // --- Setters ---
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // --- Display helper ---
    @Override
    public String toString() {
        return title + " by " + author + (available ? " (✅ Available)" : " (❌ Not Available)");
    }
}
