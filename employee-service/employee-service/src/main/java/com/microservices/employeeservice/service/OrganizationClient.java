package com.microservices.employeeservice.service;

import com.microservices.employeeservice.dto.OrganizationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ORGANIZATION-SERVICE")
public interface OrganizationClient {
    @Operation(summary = "Get Organization By Code", description = "Get a organization by the code")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/api/organizations/code/{organizationCode}")
    OrganizationDto getOrganizationByCode(@PathVariable(name = "organizationCode") String organizationCode);
}
