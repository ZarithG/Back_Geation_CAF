package com.uptc.cafmicroservice.repository;

import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.entity.Sectional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionalRepository extends JpaRepository<Sectional, Integer> {
}
