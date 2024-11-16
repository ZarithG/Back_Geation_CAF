package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.ShiftInstanceDTO;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShiftInstanceMapper {
    ShiftInstanceMapper INSTANCE = Mappers.getMapper(ShiftInstanceMapper.class);

    @Mapping(source = "dayAssignment.id", target = "dayAssignment")
    ShiftInstanceDTO shiftInstanceToShiftInstanceDTO(ShiftInstance shift);

    @Mapping(source = "dayAssignment", target = "dayAssignment.id")
    ShiftInstance shiftInstanceDTOToShiftInstance(ShiftInstanceDTO shiftDTO);
}
