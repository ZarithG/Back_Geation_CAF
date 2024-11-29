package com.uptc.cafmicroservice.mapper;

import com.uptc.cafmicroservice.dto.FitnessCenterDTO;
import com.uptc.cafmicroservice.entity.FitnessCenter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FitnessCenterMapper {
    FitnessCenterMapper INSTANCE = Mappers.getMapper(FitnessCenterMapper.class);

    FitnessCenter mapFitnessCenterDTOToFitnessCenter(FitnessCenterDTO userDTO);
    FitnessCenterDTO mapFitnessCenterToFitnessCenterDTO(FitnessCenter user);

    List<FitnessCenterDTO> mapFitnessCenterListToFitnessCenterDTOList(List<FitnessCenter> userList);
    List<FitnessCenter> mapFitnessCenterDTOListToFitnessCenterList(List<FitnessCenterDTO> userDTOList);
}
