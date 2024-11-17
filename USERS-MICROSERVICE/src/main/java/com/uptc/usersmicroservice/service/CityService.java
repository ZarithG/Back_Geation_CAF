package com.uptc.usersmicroservice.service;

import com.uptc.usersmicroservice.entity.City;
import com.uptc.usersmicroservice.entity.Department;
import com.uptc.usersmicroservice.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    CityRepository cityRepository;

    public City findCityById(int id) {
        return cityRepository.findCityById(id);
    }

    public List<City> findAllCitiesFromDepartment(Department department) {
        return cityRepository.findCitiesByDepartment(department);
    }
}
