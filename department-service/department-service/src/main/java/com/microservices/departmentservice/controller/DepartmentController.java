package com.microservices.departmentservice.controller;

import com.microservices.departmentservice.dto.DepartmentDto;
import com.microservices.departmentservice.dto.DepartmentResponse;
import com.microservices.departmentservice.service.DepartmentService;
import com.microservices.departmentservice.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "Create Department", description = "Create a department")
    @ApiResponse(responseCode = "201", description = "CREATED")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        return new ResponseEntity<>(departmentService.saveDepartment(departmentDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get All Departments", description = "Get all the departments")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<DepartmentResponse> getAllDepartments(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return new ResponseEntity<>(departmentService.getAllDepartments(page, size, sortBy, sortDir), HttpStatus.OK);
    }

    @Operation(summary = "Get Department By Id", description = "Get a department by the id")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(departmentService.getDepartmentById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get Department By Code", description = "Get a department by the code")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/code/{departmentCode}")
    public ResponseEntity<DepartmentDto> getDepartmentByCode(@PathVariable(name = "departmentCode") String departmentCode) {
        return new ResponseEntity<>(departmentService.getDepartmentByCode(departmentCode), HttpStatus.OK);
    }

    @Operation(summary = "Update Department", description = "Update a department")
    @ApiResponse(responseCode = "200", description = "OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@Valid @RequestBody DepartmentDto departmentDto, @PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(departmentService.updateDepartment(departmentDto, id), HttpStatus.OK);
    }

    @Operation(summary = "Patch Department", description = "Update partially a department")
    @ApiResponse(responseCode = "200", description = "OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentDto> patchDepartment(@Valid @RequestBody DepartmentDto departmentDto, @PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(departmentService.patchDepartment(departmentDto, id), HttpStatus.OK);
    }

    @Operation(summary = "Delete Department", description = "Delete a department")
    @ApiResponse(responseCode = "204", description = "NO CONTENT")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@Valid @PathVariable(name = "id") long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
