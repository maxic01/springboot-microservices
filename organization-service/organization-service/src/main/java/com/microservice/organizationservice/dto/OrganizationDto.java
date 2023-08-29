package com.microservice.organizationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {
    private Long id;
    @NotBlank(message = "Organization name is required")
    @Size(max = 30, message = "Organization name must be at most 30 characters")
    private String organizationName;
    private String organizationDescription;
    @NotBlank(message = "Organization code is required")
    @Size(max = 30, message = "Organization code must be at most 30 characters")
    private String organizationCode;
    private LocalDateTime organizationCreatedDate;
}
