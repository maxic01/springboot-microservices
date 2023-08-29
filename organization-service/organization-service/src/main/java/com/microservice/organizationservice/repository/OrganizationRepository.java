package com.microservice.organizationservice.repository;

import com.microservice.organizationservice.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Organization findByOrganizationCode(String organizationCode);
    boolean existsByOrganizationName(String organizationName);
    boolean existsByOrganizationCode(String organizationCode);
}
