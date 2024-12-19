package com.library;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    private StudentService studentService;

    @Mock
    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentDAO);
    }

    @Test
    void testAddStudent() {
        Student student = new Student("Alice");
        studentService.addStudent(student);
        verify(studentDAO).addStudent(student);
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student(1, "Alice Smith");
        studentService.updateStudent(student);
        verify(studentDAO).updateStudent(student);
    }

    @Test
    void testFindStudentById() {
        Student student = new Student(1, "Alice");
        when(studentDAO.getStudentById(1)).thenReturn(student);

        Student result = studentService.findStudentById(1);
        assertEquals(student, result);
    }

    @Test
    void testDisplayStudents() {
        List<Student> students = Arrays.asList(
                new Student(1, "Alice"),
                new Student(2, "Bob")
        );
        when(studentDAO.getAllStudents()).thenReturn(students);

        studentService.displayStudents(); // This method prints to console, so we can't easily test its output
        verify(studentDAO).getAllStudents();
    }
}