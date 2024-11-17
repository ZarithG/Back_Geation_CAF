package com.uptc.usersmicroservice.mapper;

import com.uptc.usersmicroservice.dto.FacultyDTO;
import com.uptc.usersmicroservice.dto.ProgramDTO;
import com.uptc.usersmicroservice.entity.Faculty;
import com.uptc.usersmicroservice.entity.Program;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FacultyMapper {
    FacultyMapper INSTANCE = Mappers.getMapper(FacultyMapper.class);

    Faculty mapFacultyDTOToFaculty(FacultyDTO facultyDTO);
    FacultyDTO mapFacultyToFacultyDTO(Faculty faculty);

    List<ProgramDTO> mapFacultyListToFacultyDTOList(List<Program> programList);
    List<Program> mapFacultyDTOListToFacultyList(List<ProgramDTO> programDTOList);
}
