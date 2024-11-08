package com.uptc.usersmicroservice.service;

import com.uptc.usersmicroservice.entity.EmergencyContact;
import com.uptc.usersmicroservice.entity.MedicalInformation;
import com.uptc.usersmicroservice.repository.EmergencyContactRepository;
import com.uptc.usersmicroservice.repository.MedicalInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalInformationService {
    @Autowired
    MedicalInformationRepository medicalInformationRepository;

    public MedicalInformation save(MedicalInformation medicalInformation) {
       return medicalInformationRepository.save(medicalInformation);
    }

}
