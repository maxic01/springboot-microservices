package com.microservices.departmentservice.dto;

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
public class DepartmentDto {
    private Long id;
    @NotBlank(message = "Department name is required")
    @Size(max = 30, message = "Department name must be at most 30 characters")
    private String departmentName;
    private String departmentDescription;
    @NotBlank(message = "Department code is required")
    @Size(max = 10, message = "Department code must be at most 10 characters")
    private String departmentCode;
}
