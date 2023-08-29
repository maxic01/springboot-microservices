package com.microservices.employeeservice.service.impl;


import com.microservices.employeeservice.dto.*;
import com.microservices.employeeservice.entity.Employee;
import com.microservices.employeeservice.exception.EmployeeApiException;
import com.microservices.employeeservice.exception.ResourceNotFoundException;
import com.microservices.employeeservice.repository.EmployeeRepository;
import com.microservices.employeeservice.service.DepartmentClient;
import com.microservices.employeeservice.service.EmployeeService;
import com.microservices.employeeservice.service.OrganizationClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentClient departmentClient;
    private final OrganizationClient organizationClient;
    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper, WebClient webClient,
                               RestTemplate restTemplate, DepartmentClient departmentClient, OrganizationClient organizationClient) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.webClient = webClient;
        this.restTemplate = restTemplate;
        this.departmentClient = departmentClient;
        this.organizationClient = organizationClient;
    }

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new EmployeeApiException(HttpStatus.BAD_REQUEST, "Employee with the same email already exists");
        }

        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/code/" + employeeDto.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

        OrganizationDto organizationDto = webClient.get()
                    .uri("http://localhost:8083/api/organizations/code/" + employeeDto.getOrganizationCode())
                    .retrieve()
                    .bodyToMono(OrganizationDto.class)
                    .block();

        Employee employee = mapToEntity(employeeDto);
        Employee newEmployee = employeeRepository.save(employee);

        return mapToDTO(newEmployee);
    }

    @Override
    public EmployeeResponse getAllEmployees(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<Employee> employeeList = employees.getContent();
        List<EmployeeDto> content = employeeList.stream().map(this::mapToDTO).toList();

        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setContent(content);
        employeeResponse.setPage(employees.getNumber());
        employeeResponse.setSize(employees.getSize());
        employeeResponse.setTotalElements(employees.getTotalElements());
        employeeResponse.setTotalPages(employees.getTotalPages());
        employeeResponse.setLast(employees.isLast());
        return employeeResponse;
    }

    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "handleFallback")
    @Retry(name = "${spring.application.name}", fallbackMethod = "handleFallback")
    @Override
    public APIResponseDto getEmployeeById(Long id) {
        logger.info("inside getEmployeeById() method");

        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Employee","id", id));

        //REST TEMPLATE

        /*ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/code/" + employee.getDepartmentCode(),
                DepartmentDto.class);

        ResponseEntity<OrganizationDto> responseEntity1 = restTemplate.getForEntity("http://localhost:8081/api/organizations/code/" + employee.getOrganizationCode(),
               OrganizationDto.class);

        DepartmentDto departmentDto = responseEntity.getBody();
        OrganizationDto organizationDto = responseEntity1.getBody();*/


        //WEB CLIENT

        /*DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/code/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

        OrganizationDto organizationDto = webClient.get()
                .uri("http://localhost:8083/api/organizations/code/" + employee.getOrganizationCode())
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();*/


        //OPEN FEIGN

        DepartmentDto departmentDto = departmentClient.getDepartmentByCode(employee.getDepartmentCode());
        OrganizationDto organizationDto = organizationClient.getOrganizationByCode(employee.getOrganizationCode());

        EmployeeDto employeeDto = mapToDTO(employee);

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);
        apiResponseDto.setOrganization(organizationDto);

        return apiResponseDto;
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto, Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee","id", id));
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setDepartmentCode(employeeDto.getDepartmentCode());

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDTO(updatedEmployee);
    }

    @Override
    public EmployeeDto patchEmployee(EmployeeDto employeeDto, Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee","id", id));
        if(employeeDto.getFirstName() != null){
            employee.setFirstName(employeeDto.getFirstName());
        }
        if(employeeDto.getLastName() != null){
            employee.setLastName(employeeDto.getLastName());
        }
        if(employeeDto.getEmail() != null){
            employee.setEmail(employeeDto.getEmail());
        }
        if(employeeDto.getDepartmentCode() != null){
            employee.setDepartmentCode(employeeDto.getDepartmentCode());
        }
        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee","id", id));
        employeeRepository.delete(employee);
    }

    public APIResponseDto handleFallback(Long id, Exception exception){

        logger.info("inside handle() method");
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Employee","id", id));

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(0L);
        departmentDto.setDepartmentName("Default Department");
        departmentDto.setDepartmentCode("T000");
        departmentDto.setDepartmentDescription("Default Department");

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setId(1L);
        organizationDto.setOrganizationName("Default Organization");
        organizationDto.setOrganizationCode("T001");
        organizationDto.setOrganizationDescription("Default Organization");
        organizationDto.setOrganizationCreatedDate(LocalDateTime.now());


        EmployeeDto employeeDto = mapToDTO(employee);

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);
        apiResponseDto.setOrganization(organizationDto);

        return apiResponseDto;
    }

    private EmployeeDto mapToDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

    private Employee mapToEntity(EmployeeDto employeeDto) {
        return modelMapper.map(employeeDto, Employee.class);
    }
}
