package com.microservices.employeeservice.service;

import com.microservices.employeeservice.dto.DepartmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DEPARTMENT-SERVICE")
public interface DepartmentClient {
    @Operation(summary = "Get Department By Code", description = "Get a department by the code")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/api/departments/code/{departmentCode}")
    DepartmentDto getDepartmentByCode(@PathVariable(name = "departmentCode") String departmentCode);
}
