package com.library;

import com.library.service.BorrowService;
import com.library.service.BookService;
import com.library.service.StudentService;
import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Borrow;
import com.library.dao.BorrowDAO;
import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize services with proper dependencies
        BookService bookService = new BookService();
        StudentService studentService = new StudentService(); // Using new default constructor
        BookDAO bookDAO = new BookDAO();
        StudentDAO studentDAO;
        try {
            studentDAO = new StudentDAO(DbConnection.getConnection());
        } catch (SQLException e) {
            System.err.println("Failed to initialize database connection: " + e.getMessage());
            return;
        }
        BorrowDAO borrowDAO = new BorrowDAO(bookDAO, studentDAO);
        BorrowService borrowService = new BorrowService(borrowDAO);

        boolean running = true;

        while (running) {
            System.out.println("\n===== Menu =====");
            System.out.println("1. Ajouter un livre");
            System.out.println("2. Afficher les livres");
            System.out.println("3. Ajouter un étudiant");
            System.out.println("4. Afficher les étudiants");
            System.out.println("5. Emprunter un livre");
            System.out.println("6. Afficher les emprunts");
            System.out.println("7. Quitter");

            System.out.print("Choisir une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consommer la ligne restante après l'entier

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter publisher: ");
                    String publisher = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Enter published year: ");
                    int publishedYear = scanner.nextInt();

                    Book book = new Book(title, author, publisher, year, isbn, publishedYear);
                    bookService.addBook(book);
                    break;

                case 2:
                    bookService.displayBooks();
                    break;

                case 3:
                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();
                    Student student = new Student(studentName);
                    studentService.addStudent(student);
                    break;

                case 4:
                    studentService.displayStudents();
                    break;

                case 5:
                    System.out.print("Enter student ID: ");
                    int studentId = scanner.nextInt();
                    System.out.print("Enter book ID: ");
                    int bookId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Student studentForBorrow = studentService.findStudentById(studentId);
                    Book bookForBorrow = bookService.findBookById(bookId);

                    if (studentForBorrow != null && bookForBorrow != null) {
                        Borrow borrow = new Borrow(studentForBorrow, bookForBorrow);
                        borrowService.borrowBook(borrow);
                        System.out.println("Book borrowed successfully!");
                    } else {
                        System.out.println("Student or book not found.");
                    }
                    break;

                case 6:
                    borrowService.displayBorrows();
                    break;

                case 7:
                    running = false;
                    System.out.println("Au revoir!");
                    break;

                default:
                    System.out.println("Option invalide.");
            }
        }

        scanner.close();
    }
}
