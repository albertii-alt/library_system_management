package com.example.library.models;

import java.time.LocalDateTime;

public class Transaction {
    private String memberName;
    private String bookTitle;
    private String action; // BORROW or RETURN
    private String timestamp;

    public Transaction(String memberName, String bookTitle, String action) {
        this.memberName = memberName;
        this.bookTitle = bookTitle;
        this.action = action;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String getMemberName() { return memberName; }
    public String getBookTitle() { return bookTitle; }
    public String getAction() { return action; }
    public String getTimestamp() { return timestamp; }
}
