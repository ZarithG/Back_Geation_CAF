package com.uptc.cafmicroservice.repository;

import com.uptc.cafmicroservice.entity.FitnessCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessCenterRepository extends JpaRepository<FitnessCenter, Integer> {
    FitnessCenter findByCoordinatorEmail(String coordinatorEmail);
}
