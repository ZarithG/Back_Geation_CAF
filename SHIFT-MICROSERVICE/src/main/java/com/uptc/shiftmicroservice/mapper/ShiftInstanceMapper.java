package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.ShiftInstanceDTO;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShiftInstanceMapper {
    ShiftInstanceMapper INSTANCE = Mappers.getMapper(ShiftInstanceMapper.class);

    // Mapping from ShiftInstance to ShiftInstanceDTO
    @Mapping(source = "shift.id", target = "shift")  // Mapear solo el ID del shift
    ShiftInstanceDTO shiftInstanceToShiftInstanceDTO(ShiftInstance shiftInstance);

    // Mapping from ShiftInstanceDTO to ShiftInstance
    @Mapping(source = "shift", target = "shift.id")  // Mapear el ID del shift de ShiftInstanceDTO a ShiftInstance
    ShiftInstance shiftInstanceDTOToShiftInstance(ShiftInstanceDTO shiftInstanceDTO);
}
