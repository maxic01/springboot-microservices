package com.microservices.employeeservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    @NotBlank(message = "First name is required")
    @Size(max = 30, message = "First name must be at most 30 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(max = 30, message = "Last name must be at most 30 characters")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Size(max = 30, message = "Email must be at most 30 characters")
    @Email(message = "Incorrect type")
    private String email;
    @NotBlank(message = "Department code is required")
    @Size(max = 30, message = "Department code must be at most 30 characters")
    private String departmentCode;
    @NotBlank(message = "Organization code is required")
    @Size(max = 30, message = "Organization code must be at most 30 characters")
    private String organizationCode;
}
