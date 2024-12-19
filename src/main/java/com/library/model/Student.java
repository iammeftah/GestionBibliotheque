package com.library.model;

public class Student {
    private int id;
    private String name;

    // Default constructor
    public Student() {
    }

    // Constructor for adding new students
    public Student(String name) {
        this.name = name;
    }

    // Constructor with ID (for database retrieval)
    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and setters remain the same
}