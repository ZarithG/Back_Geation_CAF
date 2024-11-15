package com.uptc.cafmicroservice.mapper;

import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.entity.Inscription;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InscriptionMapper {
    InscriptionMapper INSTANCE = Mappers.getMapper(InscriptionMapper.class);

    Inscription mapInscriptionDTOToInscription(InscriptionDTO inscriptionDTO);
    InscriptionDTO mapInscriptionToInscriptionDTO(Inscription inscription);

    List<InscriptionDTO> mapInscriptionListToInscriptionDTOList(List<Inscription> inscriptionList);
    List<Inscription> mapInscriptionDTOListToInscriptionList(List<InscriptionDTO> inscriptionDTOList);
}
