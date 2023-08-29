package com.microservice.organizationservice;

import com.microservice.organizationservice.dto.OrganizationDto;
import com.microservice.organizationservice.dto.OrganizationResponse;
import com.microservice.organizationservice.entity.Organization;
import com.microservice.organizationservice.repository.OrganizationRepository;
import com.microservice.organizationservice.service.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OrganizationServiceTests {
    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Mock
    private OrganizationRepository organizationRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        organizationService = new OrganizationServiceImpl(organizationRepository, modelMapper);
    }

    @Test
    public void testSaveOrganization() {
        OrganizationDto organizationDto = new OrganizationDto();
        when(organizationRepository.existsByOrganizationName(anyString())).thenReturn(false);
        when(organizationRepository.existsByOrganizationCode(anyString())).thenReturn(false);

    }

    @Test
    public void testGetOrganizationById() {
        Long id = 1L;
        Organization organization = new Organization(/* ... */);
        when(organizationRepository.findById(id)).thenReturn(Optional.of(organization));

        OrganizationDto organizationDto = organizationService.getOrganizationById(id);

        assertNotNull(organizationDto);
    }

    @Test
    public void testGetOrganizationByCode() {
        String organizationCode = "ORG123";
        Organization organization = new Organization();
        when(organizationRepository.findByOrganizationCode(organizationCode)).thenReturn(organization);

        OrganizationDto organizationDto = organizationService.getOrganizationByCode(organizationCode);

        assertNotNull(organizationDto);
    }

    @Test
    public void testDeleteOrganization() {
        Long id = 1L;
        Organization existingOrganization = new Organization();
        when(organizationRepository.findById(id)).thenReturn(Optional.of(existingOrganization));

        assertDoesNotThrow(() -> organizationService.deleteOrganization(id));
    }

}
