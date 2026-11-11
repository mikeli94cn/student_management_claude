package com.example.student.dto;

import com.example.student.entity.Course;

public record CourseResponse(Long id, String code, String name, Integer credits) {
    public static CourseResponse from(Course c) {
        return new CourseResponse(c.getId(), c.getCode(), c.getName(), c.getCredits());
    }
}
