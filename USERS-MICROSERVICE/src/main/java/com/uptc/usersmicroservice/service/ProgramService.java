package com.uptc.usersmicroservice.service;

import com.uptc.usersmicroservice.dto.ProgramDTO;
import com.uptc.usersmicroservice.entity.Program;
import com.uptc.usersmicroservice.mapper.FacultyMapper;
import com.uptc.usersmicroservice.mapper.ProgramMapper;
import com.uptc.usersmicroservice.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgramService {
    @Autowired
    ProgramRepository programRepository;

    public List<ProgramDTO> getAllPrograms() {
        return convertToProgramDTO(programRepository.findAll());

    }

    public Program getProgramByProgramName(String programName) {
        return programRepository.findByProgramName(programName);
    }

    private List<ProgramDTO> convertToProgramDTO(List<Program> programs) {
        List<ProgramDTO> programDTOs = new ArrayList<>();
        for (Program program : programs) {
            ProgramDTO programDTO = ProgramMapper.INSTANCE.mapProgramToProgramDTO(program);
            programDTO.setFacultyDTO(FacultyMapper.INSTANCE.mapFacultyToFacultyDTO(program.getFaculty()));
            programDTOs.add(programDTO);
        }
        return programDTOs;
    }
}
