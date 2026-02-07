package com.example.library.interfaces;

import com.example.library.models.Book;

public interface Borrowable {
    void borrowBook(Book book);
    void returnBook(Book book);
}
