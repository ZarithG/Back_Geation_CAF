package com.uptc.cafmicroservice.repository;

import com.uptc.cafmicroservice.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentRepository extends JpaRepository<Consent, Integer> {
}
