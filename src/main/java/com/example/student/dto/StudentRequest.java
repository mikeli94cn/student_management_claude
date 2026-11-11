package com.example.student.dto;

import jakarta.validation.constraints.*;

public record StudentRequest(
        @NotBlank @Size(max = 20) String studentNo,
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Email @Size(max = 100) String email,
        @Min(1) @Max(120) Integer age,
        @Size(max = 50) String major
) {}
