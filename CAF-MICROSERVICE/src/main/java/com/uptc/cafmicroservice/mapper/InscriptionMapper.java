package com.uptc.cafmicroservice.mapper;

import com.uptc.cafmicroservice.dto.FitnessCenterDTO;
import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.entity.FitnessCenter;
import com.uptc.cafmicroservice.entity.Inscription;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InscriptionMapper {
    InscriptionMapper INSTANCE = Mappers.getMapper(InscriptionMapper.class);

    Inscription mapInscriptionDTOToInscription(InscriptionDTO inscriptionDTO);
    InscriptionDTO mapInscriptionToInscriptionDTO(Inscription inscription);

    List<FitnessCenterDTO> mapInscriptionListToInscriptionDTOList(List<FitnessCenter> userList);
    List<FitnessCenter> mapInscriptionDTOListToInscriptionList(List<FitnessCenterDTO> userDTOList);
}