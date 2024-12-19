
package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.dao.BorrowDAO;
import com.library.model.Borrow;

import java.util.List;

public class BorrowService {

    private BorrowDAO borrowDAO;

    // Constructeur avec BorrowDAO
    public BorrowService(BorrowDAO borrowDAO) {
        this.borrowDAO = borrowDAO;
    }

    public void borrowBook(Borrow borrow) {
        // Add validation before saving
        if (borrow.getStudent() == null || borrow.getBook() == null) {
            throw new IllegalArgumentException("Student and Book must not be null");
        }
        borrowDAO.save(borrow);
    }

    // Afficher les emprunts (méthode fictive, à adapter)
    public void displayBorrows() {
        List<Borrow> borrows = borrowDAO.getAllBorrows(); // You'll need to add this method to BorrowDAO
        for (Borrow borrow : borrows) {
            System.out.println("Borrow ID: " + borrow.getId() +
                    " | Student: " + borrow.getStudent().getName() +
                    " | Book: " + borrow.getBook().getTitle() +
                    " | Status: " + borrow.getStatus() +
                    " | Borrowed: " + borrow.getBorrowDate() +
                    " | Return Due: " + borrow.getReturnDate());
        }
    }
}
