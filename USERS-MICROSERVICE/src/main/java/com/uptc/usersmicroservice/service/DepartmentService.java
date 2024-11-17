package com.uptc.usersmicroservice.service;

import com.uptc.usersmicroservice.dto.DepartmentDTO;
import com.uptc.usersmicroservice.entity.City;
import com.uptc.usersmicroservice.entity.Department;
import com.uptc.usersmicroservice.mapper.CityMapper;
import com.uptc.usersmicroservice.mapper.DepartmentMapper;
import com.uptc.usersmicroservice.repository.CityRepository;
import com.uptc.usersmicroservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CityService cityService;

    public List<DepartmentDTO> getAllDepartments() {
        return convertDepartmentsToDepartmentsDTO(departmentRepository.findAll());
    }

    private List<DepartmentDTO> convertDepartmentsToDepartmentsDTO(List<Department> departments) {
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();
        for (Department department : departments) {
            DepartmentDTO departmentDTO = DepartmentMapper.INSTANCE.mapDepartmentToDepartmentDTO(department);
            departmentDTO.setCities(CityMapper.INSTANCE.mapCityListToCityDTOList(cityService.findAllCitiesFromDepartment(department)));
            departmentDTOS.add(departmentDTO);
        }
        return departmentDTOS;
    }
}