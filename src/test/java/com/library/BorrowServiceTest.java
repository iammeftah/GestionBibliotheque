package com.library;

import com.library.dao.BorrowDAO;
import com.library.model.Borrow;
import com.library.model.Book;
import com.library.model.Student;
import com.library.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowServiceTest {
    private BorrowService borrowService;

    @Mock
    private BorrowDAO borrowDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        borrowService = new BorrowService(borrowDAO);
    }

    @Test
    void testBorrowBook() {
        Student student = new Student(1, "Alice");
        Book book = new Book(1, "Java Programming", "John Doe", "Example Publisher", 2024, "978-1234567890", 2024);
        Borrow borrow = new Borrow(student, book);

        borrowService.borrowBook(borrow);
        verify(borrowDAO).save(borrow);
    }

    @Test
    void testDisplayBorrows() {
        Student student1 = new Student(1, "Alice");
        Student student2 = new Student(2, "Bob");
        Book book1 = new Book(1, "Java Programming", "John Doe", "Example Publisher", 2024, "978-1234567890", 2024);
        Book book2 = new Book(2, "Advanced Java", "Jane Doe", "New Publisher", 2025, "978-9876543210", 2025);

        List<Borrow> borrows = Arrays.asList(
                new Borrow(1, student1, book1, new Date(), new Date(), false, "ACTIVE"),
                new Borrow(2, student2, book2, new Date(), new Date(), false, "ACTIVE")
        );
        when(borrowDAO.getAllBorrows()).thenReturn(borrows);

        borrowService.displayBorrows(); // This method prints to console, so we can't easily test its output
        verify(borrowDAO).getAllBorrows();
    }
}