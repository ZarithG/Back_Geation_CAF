package com.uptc.usersmicroservice.service;

import com.uptc.usersmicroservice.entity.Program;
import com.uptc.usersmicroservice.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {
    @Autowired
    ProgramRepository programRepository;

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    public Program getProgramByProgramName(String programName) {
        return programRepository.findByProgramName(programName);
    }
}
