package com.microservice.organizationservice.service;

import com.microservice.organizationservice.dto.OrganizationDto;
import com.microservice.organizationservice.dto.OrganizationResponse;

public interface OrganizationService {
    OrganizationDto saveOrganization(OrganizationDto organizationDto);
    OrganizationResponse getAllOrganizations(int page, int size, String sortBy, String sortDir);
    OrganizationDto getOrganizationById(Long id);
    OrganizationDto getOrganizationByCode(String organizationCode);
    OrganizationDto updateOrganization(OrganizationDto organizationDto, Long id);
    OrganizationDto patchOrganization(OrganizationDto organizationDto, Long id);
    void deleteOrganization(Long id);

}
