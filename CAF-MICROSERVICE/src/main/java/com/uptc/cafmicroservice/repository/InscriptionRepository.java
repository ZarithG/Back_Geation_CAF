package com.uptc.cafmicroservice.repository;

import com.uptc.cafmicroservice.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {
    Inscription findInscriptionById(int id);

    @Query("SELECT i FROM Inscription i WHERE i.fitnessCenter.coordinatorEmail = :coordinatorEmail AND i.inscriptionStatus = 'PENDING'")
    List<Inscription> findFitnessCenterInscriptions(String coordinatorEmail);

    @Query("SELECT i FROM Inscription i WHERE i.fitnessCenter.coordinatorEmail = :coordinatorEmail AND i.inscriptionStatus = 'ACCEPTED' OR i.inscriptionStatus = 'INACTIVE'")
    List<Inscription> findFitnessCenterActiveInscriptions(String coordinatorEmail);

    @Query("SELECT i FROM Inscription i WHERE i.userId= :userId")
    List<Inscription> findAllUserInscriptions(int userId);

    @Query("SELECT i FROM Inscription i WHERE i.userId= :userId AND i.inscriptionStatus = 'ACCEPTED'")
    List<Inscription> findAllUserActiveInscriptions(int userId);

    @Query("SELECT i FROM Inscription i WHERE i.userId= :userId AND i.fitnessCenter.id= :fitnessCenterId AND i.inscriptionStatus = 'ACCEPTED' OR i.inscriptionStatus = 'INACTIVE' OR i.inscriptionStatus = 'PENDING'")
    List<Inscription> findAllUserInscriptionsToCaf(int userId, int fitnessCenterId);

    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.fitnessCenter.id= :fitnessCenterId AND i.inscriptionStatus = 'ACCEPTED'")
    int countInscriptionsByFitnessCenter(int fitnessCenterId);
}
