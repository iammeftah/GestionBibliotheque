package com.library.model;

import java.time.LocalDate;
import java.util.Date;

public class Borrow {
    private int id;
    private Student student;
    private Book book;
    private Date borrowDate;
    private Date returnDate;
    private boolean isReturned;
    private String status; // ACTIVE, RETURNED, OVERDUE

    // Constructeur pour nouveau prêt
    public Borrow(Student student, Book book) {
        this.student = student;
        this.book = book;
        this.borrowDate = new Date();
        // Par défaut, 14 jours de prêt
        this.returnDate = new Date(System.currentTimeMillis() + (14 * 24 * 60 * 60 * 1000));
        this.isReturned = false;
        this.status = "ACTIVE";
    }

    // Constructeur pour les données de la BD
    public Borrow(int id, Student student, Book book, Date borrowDate, Date returnDate, boolean isReturned, String status) {
        this.id = id;
        this.student = student;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.isReturned = isReturned;
        this.status = status;
    }


    // Getters et setters existants...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Méthodes utilitaires
    public boolean isOverdue() {
        return !isReturned && new Date().after(returnDate);
    }
}