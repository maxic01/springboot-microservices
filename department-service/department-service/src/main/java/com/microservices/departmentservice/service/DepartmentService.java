package com.microservices.departmentservice.service;

import com.microservices.departmentservice.dto.DepartmentDto;
import com.microservices.departmentservice.dto.DepartmentResponse;


public interface DepartmentService {
    DepartmentDto saveDepartment(DepartmentDto departmentDto);
    DepartmentResponse getAllDepartments(int page, int size, String sortBy, String sortDir);
    DepartmentDto getDepartmentById(Long id);
    DepartmentDto getDepartmentByCode(String departmentCode);
    DepartmentDto updateDepartment(DepartmentDto departmentDto, Long id);
    DepartmentDto patchDepartment(DepartmentDto departmentDto, Long id);
    void deleteDepartment(Long id);

}
