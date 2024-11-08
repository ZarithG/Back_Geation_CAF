package com.uptc.usersmicroservice.service;

import com.uptc.usersmicroservice.entity.EmergencyContact;
import com.uptc.usersmicroservice.entity.Program;
import com.uptc.usersmicroservice.entity.UniversityInformation;
import com.uptc.usersmicroservice.repository.EmergencyContactRepository;
import com.uptc.usersmicroservice.repository.UniversityInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmergencyContactService {
    @Autowired
    EmergencyContactRepository emergencyContactRepository;

    public EmergencyContact save(EmergencyContact emergencyContact) {
       return emergencyContactRepository.save(emergencyContact);
    }

}
