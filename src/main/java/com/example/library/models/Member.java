package com.example.library.models;

import java.util.ArrayList;
import java.util.List;

public abstract class Member {
    private String name;
    private String type; // ✅ real field now
    private List<Book> borrowedBooks = new ArrayList<>();

    // --- Constructor
    public Member(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // --- Getters & Setters (needed for Gson) ---
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() { // ✅ Gson will serialize this
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    // --- Borrow & Return ---
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }
}
