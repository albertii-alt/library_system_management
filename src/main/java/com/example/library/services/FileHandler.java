package com.example.library.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.example.library.models.Member;
import com.example.library.models.Student;
import com.example.library.models.Teacher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileHandler {
    private static final String LIBRARY_FILE = "library.json";

    // ✅ RuntimeTypeAdapterFactory to preserve Student/Teacher info
    private static final RuntimeTypeAdapterFactory<Member> memberAdapterFactory =
            RuntimeTypeAdapterFactory.of(Member.class, "type")
                    .registerSubtype(Student.class, "Student")
                    .registerSubtype(Teacher.class, "Teacher");

    // ✅ Gson with adapter
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(memberAdapterFactory)
            .setPrettyPrinting()
            .create();

    public static Gson getGson() {
        return gson;
    }

    // ------------------- SAVE -------------------
    public static void saveLibrary(Library library) {
        try (FileWriter writer = new FileWriter(LIBRARY_FILE)) {
            gson.toJson(library, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------- LOAD -------------------
    public static Library loadLibrary() {
        try (FileReader reader = new FileReader(LIBRARY_FILE)) {
            return gson.fromJson(reader, Library.class);
        } catch (IOException e) {
            return new Library(); // fallback empty if no file exists yet
        }
    }
}