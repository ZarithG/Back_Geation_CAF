package com.uptc.cafmicroservice.repository;

import com.uptc.cafmicroservice.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {
}
