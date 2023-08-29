package com.microservice.organizationservice.service.impl;

import com.microservice.organizationservice.dto.OrganizationDto;
import com.microservice.organizationservice.dto.OrganizationResponse;
import com.microservice.organizationservice.entity.Organization;
import com.microservice.organizationservice.exception.OrganizationApiException;
import com.microservice.organizationservice.exception.ResourceNotFoundException;
import com.microservice.organizationservice.repository.OrganizationRepository;
import com.microservice.organizationservice.service.OrganizationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final ModelMapper modelMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, ModelMapper modelMapper) {
        this.organizationRepository = organizationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto) {
        if (organizationRepository.existsByOrganizationName(organizationDto.getOrganizationName())) {
            throw new OrganizationApiException(HttpStatus.BAD_REQUEST, "Organization with the same name already exists");
        }
        if (organizationRepository.existsByOrganizationCode(organizationDto.getOrganizationCode())) {
            throw new OrganizationApiException(HttpStatus.BAD_REQUEST, "Organization with the same code already exists");
        }

        Organization organization = mapToEntity(organizationDto);
        Organization newOrganization = organizationRepository.save(organization);

        return mapToDTO(newOrganization);
    }

    @Override
    public OrganizationResponse getAllOrganizations(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Organization> organizations = organizationRepository.findAll(pageable);
        List<Organization> organizationList = organizations.getContent();
        List<OrganizationDto> content = organizationList.stream().map(this::mapToDTO).toList();

        OrganizationResponse organizationResponse = new OrganizationResponse();
        organizationResponse.setContent(content);
        organizationResponse.setPage(organizations.getNumber());
        organizationResponse.setSize(organizations.getSize());
        organizationResponse.setTotalElements(organizations.getTotalElements());
        organizationResponse.setTotalPages(organizations.getTotalPages());
        organizationResponse.setLast(organizations.isLast());
        return organizationResponse;
    }

    @Override
    public OrganizationDto getOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organization","id", id));
        return mapToDTO(organization);
    }

    @Override
    public OrganizationDto getOrganizationByCode(String organizationCode) {
        Organization organization = organizationRepository.findByOrganizationCode(organizationCode);
        return mapToDTO(organization);
    }

    @Override
    public OrganizationDto updateOrganization(OrganizationDto organizationDto, Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organization","id", id));
        organization.setOrganizationName(organizationDto.getOrganizationName());
        organization.setOrganizationDescription(organizationDto.getOrganizationDescription());
        organization.setOrganizationCode(organizationDto.getOrganizationCode());

        Organization updatedOrganization = organizationRepository.save(organization);
        return mapToDTO(updatedOrganization);
    }

    @Override
    public OrganizationDto patchOrganization(OrganizationDto organizationDto, Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organization","id", id));
        if(organizationDto.getOrganizationName() != null){
            organization.setOrganizationName(organizationDto.getOrganizationName());
        }
        if(organizationDto.getOrganizationDescription() != null){
            organization.setOrganizationDescription(organizationDto.getOrganizationDescription());
        }
        if(organizationDto.getOrganizationCode() != null){
            organization.setOrganizationCode(organizationDto.getOrganizationCode());
        }
        if(organizationDto.getOrganizationCreatedDate() != null){
            organization.setOrganizationCreatedDate(organizationDto.getOrganizationCreatedDate());
        }
        Organization updatedOrganization = organizationRepository.save(organization);
        return mapToDTO(updatedOrganization);
    }

    @Override
    public void deleteOrganization(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organization","id", id));
        organizationRepository.delete(organization);
    }

    private OrganizationDto mapToDTO(Organization organization) {
        return modelMapper.map(organization, OrganizationDto.class);
    }

    private Organization mapToEntity(OrganizationDto organizationDto) {
        return modelMapper.map(organizationDto, Organization.class);
    }
}
