package com.example.library.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.library.exceptions.BookNotAvailableException;
import com.example.library.models.Book;
import com.example.library.models.Member;

public class Library {
    private final List<Book> books;
    private final List<Member> members;
    private final List<String> transactions;

    public Library(List<Book> books, List<Member> members, List<String> transactions) {
        // If null, replace with empty lists
        this.books = books != null ? new ArrayList<>(books) : new ArrayList<>();
        this.members = members != null ? new ArrayList<>(members) : new ArrayList<>();
        this.transactions = transactions != null ? new ArrayList<>(transactions) : new ArrayList<>();

        // ✅ FIX borrowedBooks to point to the same Book objects in books list
        syncBorrowedBooks();
    }

    // Default constructor for when no data is loaded yet
    public Library() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    // -------------------- BOOKS --------------------
    public void addBook(Book book) {
        books.add(book);
        transactions.add("Added book: " + book.getTitle() + " by " + book.getAuthor());
        FileHandler.saveLibrary(this);
    }

    public List<Book> getBooks() {
        // ✅ Always return books sorted by title
        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books.clear();
        this.books.addAll(books);
        syncBorrowedBooks(); // ✅ ensure borrowedBooks reference same objects
    }

    // -------------------- MEMBERS --------------------
    public void addMember(Member member) {
        members.add(member);
        transactions.add("Registered member: " + member.getName() + " (" + member.getType() + ")");
        FileHandler.saveLibrary(this);
    }

    public List<Member> getMembers() {
        // ✅ Always return members sorted by name
        members.sort(Comparator.comparing(Member::getName, String.CASE_INSENSITIVE_ORDER));
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members.clear();
        this.members.addAll(members);
        syncBorrowedBooks(); // ✅ fix borrowedBooks links after setting members
    }

    // -------------------- BORROW --------------------
    public void borrowBook(String memberName, String bookTitle) throws BookNotAvailableException {
        Member member = findMember(memberName);
        if (member == null) {
            throw new IllegalArgumentException("Member not found: " + memberName);
        }

        Book book = findBook(bookTitle);
        if (book == null || !book.isAvailable()) {
            throw new BookNotAvailableException("Book not available: " + bookTitle);
        }

        book.setAvailable(false);
        member.borrowBook(book);

        transactions.add(memberName + " borrowed \"" + book.getTitle() + "\" by " + book.getAuthor());
        FileHandler.saveLibrary(this);
    }

    // -------------------- RETURN --------------------
public void returnBook(String memberName, String bookTitle) {
    Member member = findMember(memberName);
    if (member == null) {
        throw new IllegalArgumentException("Member not found: " + memberName);
    }

    // Find the book in the member's borrowed list
    Book borrowedBook = member.getBorrowedBooks().stream()
            .filter(b -> b.getTitle().equalsIgnoreCase(bookTitle))
            .findFirst()
            .orElse(null);

    if (borrowedBook == null) {
        throw new IllegalArgumentException(memberName + " did not borrow this book!");
    }

    // ✅ Also find the book in the main books list
    Book mainBook = findBook(bookTitle);
    if (mainBook != null) {
        mainBook.setAvailable(true);
    }

    // Remove from member's borrowed list
    member.returnBook(borrowedBook);

    transactions.add(memberName + " returned \"" + borrowedBook.getTitle() + "\" by " + borrowedBook.getAuthor());
    FileHandler.saveLibrary(this);
}

    // -------------------- TRANSACTIONS --------------------
    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions.clear();
        this.transactions.addAll(transactions);
    }

    // -------------------- HELPERS --------------------
    private Member findMember(String name) {
        return members.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private Book findBook(String title) {
        return books.stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    // ✅ New method: ensure borrowedBooks reference same Book objects in `books`
    private void syncBorrowedBooks() {
        for (Member m : members) {
            List<Book> fixedBorrowed = new ArrayList<>();
            for (Book borrowed : m.getBorrowedBooks()) {
                Book match = findBook(borrowed.getTitle());
                if (match != null) {
                    match.setAvailable(false); // keep correct availability
                    fixedBorrowed.add(match);
                }
            }
            m.getBorrowedBooks().clear();
            m.getBorrowedBooks().addAll(fixedBorrowed);
        }
    }
}
