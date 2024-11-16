package com.uptc.shiftmicroservice.mapper;
import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShiftMapper {
    ShiftMapper INSTANCE = Mappers.getMapper(ShiftMapper.class);

    @Mapping(source = "dayAssignment.id", target = "dayAssignment")
    ShiftDTO shiftToShiftDTO(Shift shift);

    @Mapping(source = "dayAssignment", target = "dayAssignment.id")
    Shift shiftDTOToShift(ShiftDTO shiftDTO);

}
