package com.uptc.usersmicroservice.repository;

import com.uptc.usersmicroservice.entity.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Integer> {
}
