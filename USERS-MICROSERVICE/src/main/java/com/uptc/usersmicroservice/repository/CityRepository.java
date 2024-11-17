package com.uptc.usersmicroservice.repository;

import com.uptc.usersmicroservice.entity.City;
import com.uptc.usersmicroservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    List<City> findCitiesByDepartment(Department department);
}

