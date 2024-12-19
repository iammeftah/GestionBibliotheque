package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService(Connection connection) {
        this.studentDAO = new StudentDAO(connection);
    }

    public StudentService() {
        try {
            this.studentDAO = new StudentDAO(DbConnection.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize StudentService", e);
        }
    }

    // Constructeur
    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    

    // Ajouter un étudiant
    public void addStudent(Student student) {
        studentDAO.addStudent(student);
    }

    // Afficher tous les étudiants
    public void displayStudents() {
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            System.out.println("ID: " + student.getId() + " | Nom: " + student.getName());
        }
    }

    // Trouver un étudiant par ID
    public Student findStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    public void updateStudent(Student student) {
        studentDAO.updateStudent(student);
    }
}
