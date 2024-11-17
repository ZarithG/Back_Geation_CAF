package com.uptc.usersmicroservice.mapper;

import com.uptc.usersmicroservice.dto.CityDTO;
import com.uptc.usersmicroservice.dto.DepartmentDTO;
import com.uptc.usersmicroservice.entity.City;
import com.uptc.usersmicroservice.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CityMapper {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    City mapCityDTOToCity(CityDTO cityDTO);
    CityDTO mapCityToCityDTO(City city);

    List<CityDTO> mapCityListToCityDTOList(List<City> departmentList);
    List<City> mapCityDTOListToCityList(List<CityDTO> departmentDTOList);
}
