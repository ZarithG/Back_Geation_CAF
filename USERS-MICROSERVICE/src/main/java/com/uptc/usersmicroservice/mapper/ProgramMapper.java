package com.uptc.usersmicroservice.mapper;

import com.uptc.usersmicroservice.dto.DepartmentDTO;
import com.uptc.usersmicroservice.dto.ProgramDTO;
import com.uptc.usersmicroservice.entity.Department;
import com.uptc.usersmicroservice.entity.Program;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProgramMapper {
    ProgramMapper INSTANCE = Mappers.getMapper(ProgramMapper.class);

    Program mapProgramDTOToProgram(ProgramDTO programDTO);
    ProgramDTO mapProgramToProgramDTO(Program program);

    List<ProgramDTO> mapProgramListToProgramDTOList(List<Program> programList);
    List<Program> mapProgramDTOListToProgramList(List<ProgramDTO> programDTOList);
}
