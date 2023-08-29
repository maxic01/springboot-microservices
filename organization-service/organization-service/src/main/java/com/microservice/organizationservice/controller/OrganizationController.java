package com.microservice.organizationservice.controller;

import com.microservice.organizationservice.dto.OrganizationDto;
import com.microservice.organizationservice.dto.OrganizationResponse;
import com.microservice.organizationservice.service.OrganizationService;
import com.microservice.organizationservice.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Operation(summary = "Create Organization", description = "Create a organization")
    @ApiResponse(responseCode = "201", description = "CREATED")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<OrganizationDto> createOrganization(@Valid @RequestBody OrganizationDto organizationDto) {
        return new ResponseEntity<>(organizationService.saveOrganization(organizationDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get All Organizations", description = "Get all the organizations")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<OrganizationResponse> getAllOrganizations(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return new ResponseEntity<>(organizationService.getAllOrganizations(page, size, sortBy, sortDir), HttpStatus.OK);
    }

    @Operation(summary = "Get Organization By Id", description = "Get a organization by the id")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOrganizationById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(organizationService.getOrganizationById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get Organization By Code", description = "Get a organization by the code")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/code/{organizationCode}")
    public ResponseEntity<OrganizationDto> getOrganizationByCode(@PathVariable(name = "organizationCode") String organizationCode) {
        return new ResponseEntity<>(organizationService.getOrganizationByCode(organizationCode), HttpStatus.OK);
    }

    @Operation(summary = "Update Organization", description = "Update a organization")
    @ApiResponse(responseCode = "200", description = "OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(@Valid @RequestBody OrganizationDto organizationDto, @PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(organizationService.updateOrganization(organizationDto, id), HttpStatus.OK);
    }

    @Operation(summary = "Patch Organization", description = "Update partially a organization")
    @ApiResponse(responseCode = "200", description = "OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<OrganizationDto> patchOrganization(@Valid @RequestBody OrganizationDto organizationDto, @PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(organizationService.patchOrganization(organizationDto, id), HttpStatus.OK);
    }

    @Operation(summary = "Delete Organization", description = "Delete a organization")
    @ApiResponse(responseCode = "204", description = "NO CONTENT")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@Valid @PathVariable(name = "id") long id) {
        organizationService.deleteOrganization(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
