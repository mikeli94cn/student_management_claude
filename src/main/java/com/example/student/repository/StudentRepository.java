package com.example.student.repository;

import com.example.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByStudentNo(String studentNo);
    boolean existsByEmail(String email);
    boolean existsByStudentNoAndIdNot(String studentNo, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    // Derived query: Spring Data builds the SQL from the method name
    Page<Student> findByNameContainingIgnoreCaseOrStudentNoContainingIgnoreCase(
            String name, String studentNo, Pageable pageable);
}
