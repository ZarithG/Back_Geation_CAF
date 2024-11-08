package com.uptc.usersmicroservice.repository;

import com.uptc.usersmicroservice.entity.UniversityInformation;
import com.uptc.usersmicroservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityInformationRepository extends JpaRepository<UniversityInformation, Integer> {
}
