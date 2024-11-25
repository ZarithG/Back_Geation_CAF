package com.uptc.cafmicroservice.repository;

import com.uptc.cafmicroservice.entity.Consent;
import com.uptc.cafmicroservice.enums.ConsentTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsentRepository extends JpaRepository<Consent, Integer> {
    @Query("SELECT c.filePath FROM Consent c WHERE c.inscription.id = :inscriptionId AND c.consentType = :consentTypeEnum")
    String findFilePathByInscriptionIdAndConsentType(@Param("inscriptionId") int inscriptionId, @Param("consentTypeEnum") ConsentTypeEnum consentTypeEnum);

    List<Consent> findByInscriptionId(int inscriptionId);
}
