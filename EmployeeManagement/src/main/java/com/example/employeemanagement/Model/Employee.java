package com.example.employeemanagement.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {
    @NotEmpty(message = "ID cannot be empty")
    @Size(min = 3, message = "ID must be more than 2 characters")
    private String id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 5, message = "Name must be more than 4 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only letters")
    private String name;

    @Email(message = "Email must be in a valid email format")
    private String email;

    @Pattern(regexp = "^05\\d{8}$",
            message = "Phone number must start with '05' and consist of exactly 10 digits")
    private String phoneNumber;

    @NotNull(message = "Age cannot be empty")
    @Min(value = 26, message = "Age must be more than 25")
    @Digits(integer = 10, fraction = 0, message = "Age must be valid number")
    private int age;

    @NotEmpty(message = "Position cannot be empty")
    @Pattern(regexp = "^(Supervisor|Coordinator)$",
            message = "Position must be either 'Supervisor' or 'Coordinator'")
    private String position;

    @AssertFalse(message = "onLeave must be initially false")
    private boolean onLeave;

    @NotNull(message = "Hire date cannot be empty")
    @PastOrPresent(message = "Hire Date must be in the present or the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    @NotNull(message = "Annual leave cannot be empty")
    @Positive(message = "Annual leave must be positive number")
    private int annualLeave;
}
