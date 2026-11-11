package com.example.student.dto;

import jakarta.validation.constraints.*;

public record CourseRequest(
        @NotBlank @Size(max = 20) String code,
        @NotBlank @Size(max = 100) String name,
        @NotNull @Min(1) @Max(10) Integer credits
) {}
