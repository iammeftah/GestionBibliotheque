package com.library.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private String isbn;
    private int publishedYear;

    // Default constructor
    public Book() {
    }

    // Constructor for adding new books
    public Book(String title, String author, String publisher, int year, String isbn, int publishedYear) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
    }

    // Constructor with ID (for database retrieval)
    public Book(int id, String title, String author, String publisher, int year, String isbn, int publishedYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
    }

    // Add getters and setters (existing ones are correct)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }
}