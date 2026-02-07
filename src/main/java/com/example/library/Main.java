package com.example.library;

import java.lang.reflect.Type;
import java.util.Map;

import com.example.library.exceptions.BookNotAvailableException;
import com.example.library.models.Book;
import com.example.library.models.Member;
import com.example.library.models.Student;
import com.example.library.models.Teacher;
import com.example.library.services.FileHandler;
import com.example.library.services.Library;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class Main {

    public static void main(String[] args) {

        // ✅ Load library data (books, members, transactions)
        Library lib = FileHandler.loadLibrary();
        Gson gson = FileHandler.getGson();

        // Run server on port 8080
        port(8080);

        // Serve static files (frontend) from the "ui" folder
        staticFiles.location("/public");

        // TypeToken for parsing JSON requests
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();

        // --- Health Check ---
        get("/hello", (req, res) -> "Library System is running!");

        // --- Add Book ---
        post("/books", (req, res) -> {
            res.type("application/json");
            Map<String, String> data = gson.fromJson(req.body(), mapType);
            String title = data.get("title");
            String author = data.get("author");

            Book book = new Book(title, author);
            lib.addBook(book); // ✅ auto-saves inside Library
            return gson.toJson(book);
        });

        // --- Add Member ---
        post("/members", (req, res) -> {
            res.type("application/json");
            Map<String, String> data = gson.fromJson(req.body(), mapType);
            String name = data.get("name");
            String type = data.get("type");

            Member member;
            if (type.equalsIgnoreCase("student")) {
                member = new Student(name);
            } else if (type.equalsIgnoreCase("teacher")) {
                member = new Teacher(name);
            } else {
                res.status(400);
                return "{\"error\":\"Invalid member type\"}";
            }

            lib.addMember(member); // ✅ auto-saves inside Library
            return gson.toJson(member);
        });

        // --- Borrow Book ---
        post("/borrow", (req, res) -> {
            res.type("application/json");
            Map<String, String> data = gson.fromJson(req.body(), mapType);
            String memberName = data.get("memberName");
            String bookTitle = data.get("bookTitle");

            try {
                lib.borrowBook(memberName, bookTitle); // ✅ auto-saves inside Library
                return "{\"message\":\"Book borrowed\"}";
            } catch (BookNotAvailableException e) {
                res.status(400);
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        });

        // --- Return Book ---
        post("/return", (req, res) -> {
            res.type("application/json");
            Map<String, String> data = gson.fromJson(req.body(), mapType);
            String memberName = data.get("memberName");
            String bookTitle = data.get("bookTitle");

            try {
                lib.returnBook(memberName, bookTitle); // ✅ auto-saves inside Library
                return "{\"message\":\"Book returned\"}";
            } catch (IllegalArgumentException e) {
                res.status(400);
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        });

        // --- List Books ---
        get("/books", (req, res) -> {
            res.type("application/json");
            return gson.toJson(lib.getBooks());
        });

        // --- List Members ---
        get("/members", (req, res) -> {
            res.type("application/json");
            return gson.toJson(lib.getMembers());
        });

        // --- List Transactions ---
        get("/transactions", (req, res) -> {
            res.type("application/json");
            return gson.toJson(lib.getTransactions());
        });

        System.out.println("✅ Server running at http://localhost:8080");
    }
}