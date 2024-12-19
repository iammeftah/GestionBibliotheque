package com.library.dao;

import com.library.model.Borrow;
import com.library.model.Book;
import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowDAO {
    private BookDAO bookDAO;
    private StudentDAO studentDAO;

    public BorrowDAO(BookDAO bookDAO, StudentDAO studentDAO) {
        this.bookDAO = bookDAO;
        this.studentDAO = studentDAO;
    }

    public Optional<Borrow> findById(int id) {
        String query = "SELECT * FROM borrows WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(buildBorrowFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return Optional.empty();
    }

    public void save(Borrow borrow) {
        String query = "INSERT INTO borrows (student_id, book_id, borrow_date, return_date, is_returned, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());
            stmt.setTimestamp(3, new Timestamp(borrow.getBorrowDate().getTime()));
            stmt.setTimestamp(4, new Timestamp(borrow.getReturnDate().getTime()));
            stmt.setBoolean(5, borrow.isReturned());
            stmt.setString(6, borrow.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating borrow failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    borrow.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating borrow failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save borrow", e);
        }
    }

    private Borrow buildBorrowFromResultSet(ResultSet rs) throws SQLException {
        Student student = studentDAO.getStudentById(rs.getInt("student_id"));
        Book book = bookDAO.getBookById(rs.getInt("book_id"));

        return new Borrow(
                rs.getInt("id"),
                student,
                book,
                rs.getTimestamp("borrow_date"),
                rs.getTimestamp("return_date"),
                rs.getBoolean("is_returned"),
                rs.getString("status")
        );
    }

    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM borrows";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                borrows.add(buildBorrowFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all borrows", e);
        }

        return borrows;
    }
}