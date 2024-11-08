package com.uptc.usersmicroservice.repository;

import com.uptc.usersmicroservice.entity.Program;
import com.uptc.usersmicroservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
    Program findByProgramName(String name);
}
