package com.microservices.departmentservice.repository;

import com.microservices.departmentservice.dto.DepartmentDto;
import com.microservices.departmentservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByDepartmentCode(String departmentCode);
    boolean existsByDepartmentName(String departmentName);
    boolean existsByDepartmentCode(String departmentName);
}
