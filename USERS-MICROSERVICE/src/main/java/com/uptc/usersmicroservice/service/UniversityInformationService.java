package com.uptc.usersmicroservice.service;

import com.uptc.usersmicroservice.entity.Program;
import com.uptc.usersmicroservice.entity.UniversityInformation;
import com.uptc.usersmicroservice.repository.UniversityInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UniversityInformationService {
    @Autowired
    UniversityInformationRepository repository;

    @Autowired
    ProgramService programService;

    public UniversityInformation save(UniversityInformation actUniversityInformation) {
        Program program = programService.getProgramByProgramId(actUniversityInformation.getProgram().getId());
        actUniversityInformation.setProgram(program);
        return repository.save(actUniversityInformation);
    }
}
