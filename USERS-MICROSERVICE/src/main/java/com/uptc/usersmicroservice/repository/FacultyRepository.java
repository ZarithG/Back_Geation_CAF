package com.uptc.usersmicroservice.repository;

import com.uptc.usersmicroservice.entity.Faculty;
import com.uptc.usersmicroservice.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

}
