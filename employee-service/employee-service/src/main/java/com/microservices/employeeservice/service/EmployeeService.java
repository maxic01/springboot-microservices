package com.microservices.employeeservice.service;

import com.microservices.employeeservice.dto.APIResponseDto;
import com.microservices.employeeservice.dto.EmployeeDto;
import com.microservices.employeeservice.dto.EmployeeResponse;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    EmployeeResponse getAllEmployees(int page, int size, String sortBy, String sortDir);
    APIResponseDto getEmployeeById(Long id);
    EmployeeDto updateEmployee(EmployeeDto employeeDto, Long id);
    EmployeeDto patchEmployee(EmployeeDto employeeDto, Long id);
    void deleteEmployee(Long id);

}
