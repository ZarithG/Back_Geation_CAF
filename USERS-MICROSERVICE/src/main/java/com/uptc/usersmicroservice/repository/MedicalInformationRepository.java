package com.uptc.usersmicroservice.repository;

import com.uptc.usersmicroservice.entity.EmergencyContact;
import com.uptc.usersmicroservice.entity.MedicalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalInformationRepository extends JpaRepository<MedicalInformation, Integer> {
}
