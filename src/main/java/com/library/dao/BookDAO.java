package com.library.dao;

import com.library.model.Book;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // Ajouter un nouveau livre dans la base de données
    public void add(Book book) {
        String sql = "INSERT INTO books (title, author, publisher, year, isbn, published_year) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getYear());
            statement.setString(5, book.getIsbn());  // Make sure this line exists
            statement.setInt(6, book.getPublishedYear());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }


    // Récupérer un livre par son ISBN
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        Book book = null;
        
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                book = new Book(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("year"),
                        resultSet.getString("isbn"),
                        resultSet.getInt("published_year")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du livre : " + e.getMessage());
        }
        
        return book;
    }

    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        Book book = null;

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("year"),
                        resultSet.getString("isbn"),
                        resultSet.getInt("published_year")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving book: " + e.getMessage());
        }

        return book;
    }

    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, year = ?, isbn = ?, published_year = ? WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getYear());
            statement.setString(5, book.getIsbn());
            statement.setInt(6, book.getPublishedYear());
            statement.setInt(7, book.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
    }
    
    // Récupérer tous les livres
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        
        try (Connection connection = DbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
             
            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("year"),
                        resultSet.getString("isbn"),
                        resultSet.getInt("published_year")
                );

                /*
                private int id;
                private String title;
                private String author;
                private String publisher;
                private int year;
                private String isbn;
                private int publishedYear;
                * */


                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livres : " + e.getMessage());
        }
        
        return books;
    }
}
