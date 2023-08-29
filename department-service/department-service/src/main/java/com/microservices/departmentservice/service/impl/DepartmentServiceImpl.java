package com.microservices.departmentservice.service.impl;

import com.microservices.departmentservice.dto.DepartmentDto;
import com.microservices.departmentservice.dto.DepartmentResponse;
import com.microservices.departmentservice.entity.Department;
import com.microservices.departmentservice.exception.DepartmentApiException;
import com.microservices.departmentservice.exception.ResourceNotFoundException;
import com.microservices.departmentservice.repository.DepartmentRepository;
import com.microservices.departmentservice.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {
        if (departmentRepository.existsByDepartmentName(departmentDto.getDepartmentName())) {
            throw new DepartmentApiException(HttpStatus.BAD_REQUEST, "Department with the same name already exists");
        }
        if (departmentRepository.existsByDepartmentCode(departmentDto.getDepartmentCode())) {
            throw new DepartmentApiException(HttpStatus.BAD_REQUEST, "Department with the same code already exists");
        }

        Department department = mapToEntity(departmentDto);
        Department newDepartment = departmentRepository.save(department);

        return mapToDTO(newDepartment);
    }

    @Override
    public DepartmentResponse getAllDepartments(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Department> departments = departmentRepository.findAll(pageable);
        List<Department> departmentList = departments.getContent();
        List<DepartmentDto> content = departmentList.stream().map(this::mapToDTO).toList();

        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setContent(content);
        departmentResponse.setPage(departments.getNumber());
        departmentResponse.setSize(departments.getSize());
        departmentResponse.setTotalElements(departments.getTotalElements());
        departmentResponse.setTotalPages(departments.getTotalPages());
        departmentResponse.setLast(departments.isLast());
        return departmentResponse;
    }

    @Override
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department","id", id));
        return mapToDTO(department);
    }

    @Override
    public DepartmentDto getDepartmentByCode(String departmentCode) {
        Department department = departmentRepository.findByDepartmentCode(departmentCode);
        return mapToDTO(department);
    }

    @Override
    public DepartmentDto updateDepartment(DepartmentDto departmentDto, Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department","id", id));;
        department.setDepartmentName(departmentDto.getDepartmentName());
        department.setDepartmentDescription(departmentDto.getDepartmentDescription());
        department.setDepartmentCode(departmentDto.getDepartmentCode());

        Department updatedDepartment = departmentRepository.save(department);
        return mapToDTO(updatedDepartment);
    }

    @Override
    public DepartmentDto patchDepartment(DepartmentDto departmentDto, Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department","id", id));
        if(departmentDto.getDepartmentName() != null){
            department.setDepartmentName(departmentDto.getDepartmentName());
        }
        if(departmentDto.getDepartmentDescription() != null){
            department.setDepartmentDescription(departmentDto.getDepartmentDescription());
        }
        if(departmentDto.getDepartmentCode() != null){
            department.setDepartmentCode(departmentDto.getDepartmentCode());
        }
        Department updatedDepartment = departmentRepository.save(department);
        return mapToDTO(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department","id", id));
        departmentRepository.delete(department);
    }

    private DepartmentDto mapToDTO(Department department) {
        return modelMapper.map(department, DepartmentDto.class);
    }

    private Department mapToEntity(DepartmentDto departmentDto) {
        return modelMapper.map(departmentDto, Department.class);
    }
}
