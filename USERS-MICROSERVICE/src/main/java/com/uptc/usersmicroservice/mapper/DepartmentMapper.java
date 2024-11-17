package com.uptc.usersmicroservice.mapper;

import com.uptc.usersmicroservice.dto.DepartmentDTO;
import com.uptc.usersmicroservice.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = CityMapper.class)
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    Department mapDepartmentDTOToDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO mapDepartmentToDepartmentDTO(Department department);

    List<DepartmentDTO> mapDepartmentListToDepartmentDTOList(List<Department> departmentList);
    List<Department> mapDepartmentDTOListToDepartmentList(List<DepartmentDTO> departmentDTOList);
}