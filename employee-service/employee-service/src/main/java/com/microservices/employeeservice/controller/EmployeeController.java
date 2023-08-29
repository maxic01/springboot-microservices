package com.microservices.employeeservice.controller;

import com.microservices.employeeservice.dto.APIResponseDto;
import com.microservices.employeeservice.dto.EmployeeDto;
import com.microservices.employeeservice.dto.EmployeeResponse;
import com.microservices.employeeservice.service.EmployeeService;
import com.microservices.employeeservice.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Create Employee", description = "Create a employee")
    @ApiResponse(responseCode = "201", description = "CREATED")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.saveEmployee(employeeDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get All Employees", description = "Get all the employees")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<EmployeeResponse> getAllEmployees(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return new ResponseEntity<>(employeeService.getAllEmployees(page, size, sortBy, sortDir), HttpStatus.OK);
    }

    @Operation(summary = "Get Employee By Id", description = "Get a employee by the id")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponseDto> getEmployeeById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @Operation(summary = "Update Employee", description = "Update a employee")
    @ApiResponse(responseCode = "200", description = "OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@Valid @RequestBody EmployeeDto employeeDto, @PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(employeeService.updateEmployee(employeeDto, id), HttpStatus.OK);
    }

    @Operation(summary = "Patch Employee", description = "Update partially a employee")
    @ApiResponse(responseCode = "200", description = "OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDto> patchEmployee(@Valid @RequestBody EmployeeDto employeeDto, @PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(employeeService.patchEmployee(employeeDto, id), HttpStatus.OK);
    }

    @Operation(summary = "Delete Employee", description = "Delete a employee")
    @ApiResponse(responseCode = "204", description = "NO CONTENT")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@Valid @PathVariable(name = "id") long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
