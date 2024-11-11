package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.DayAssignmentDTO;
import com.uptc.shiftmicroservice.entity.Day;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = ShiftMapper.class)
public interface DayAssignmentMapper {
    DayAssignmentMapper INSTANCE = Mappers.getMapper(DayAssignmentMapper.class);

    @Mappings({
            @Mapping( source = "id", target = "id"),
            @Mapping(source = "fitnessCenter", target = "fitnessCenter"),
            @Mapping(source = "day", target = "day", qualifiedByName = "dayToString"),
            @Mapping(target = "shifts", ignore = true) // Ignorar shifts en el mapeo inicial
    })
    DayAssignmentDTO mapDayAssignmentToDayAssignmentDTO(DayAssignment dayAssignment);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "fitnessCenter", target = "fitnessCenter"),
            @Mapping(source = "day", target = "day", qualifiedByName = "stringToDay")
    })
    DayAssignment mapDayAssignmentDTOToDayAssignment(DayAssignmentDTO dayAssignmentDTO);

    // Métodos de conversión personalizados entre Day y String

    @Named("dayToString")
    default String dayToString(Day day) {
        return day != null ? day.name() : null;
    }

    @Named("stringToDay")
    default Day stringToDay(String day) {
        return day != null ? Day.valueOf(day) : null;
    }

}
