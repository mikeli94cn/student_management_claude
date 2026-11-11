package com.example.student.dto;

import com.example.student.entity.Student;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public record StudentResponse(
        Long id,
        String studentNo,
        String name,
        String email,
        Integer age,
        String major,
        List<CourseResponse> courses,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // must be called inside a transaction (courses are lazy-loaded)
    public static StudentResponse from(Student s) {
        List<CourseResponse> courses = s.getCourses().stream()
                .map(CourseResponse::from)
                .sorted(Comparator.comparing(CourseResponse::id))
                .toList();
        return new StudentResponse(s.getId(), s.getStudentNo(), s.getName(), s.getEmail(),
                s.getAge(), s.getMajor(), courses, s.getCreatedAt(), s.getUpdatedAt());
    }
}
