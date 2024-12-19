package com.library;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {
    private BookService bookService;

    @Mock
    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookDAO);
    }

    @Test
    void testAddBook() {
        Book book = new Book("Java Programming", "John Doe", "Example Publisher", 2024, "978-1234567890", 2024);
        bookService.addBook(book);
        verify(bookDAO).add(book);
    }

    @Test
    void testUpdateBook() {
        Book book = new Book(1, "Advanced Java", "Jane Doe", "New Publisher", 2025, "978-9876543210", 2025);
        bookService.updateBook(book);
        verify(bookDAO).update(book);
    }

    @Test
    void testDeleteBook() {
        bookService.deleteBook(1);
        verify(bookDAO).delete(1);
    }

    @Test
    void testFindBookById() {
        Book book = new Book(1, "Java Programming", "John Doe", "Example Publisher", 2024, "978-1234567890", 2024);
        when(bookDAO.getBookById(1)).thenReturn(book);

        Book result = bookService.findBookById(1);
        assertEquals(book, result);
    }

    @Test
    void testDisplayBooks() {
        List<Book> books = Arrays.asList(
                new Book(1, "Java Programming", "John Doe", "Example Publisher", 2024, "978-1234567890", 2024),
                new Book(2, "Advanced Java", "Jane Doe", "New Publisher", 2025, "978-9876543210", 2025)
        );
        when(bookDAO.getAllBooks()).thenReturn(books);

        bookService.displayBooks(); // This method prints to console, so we can't easily test its output
        verify(bookDAO).getAllBooks();
    }
}