package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.DayAssignmentDTO;
import com.uptc.shiftmicroservice.entity.Day;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-21T16:06:48-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
public class DayAssignmentMapperImpl implements DayAssignmentMapper {

    @Override
    public DayAssignmentDTO mapDayAssignmentToDayAssignmentDTO(DayAssignment dayAssignment) {
        if ( dayAssignment == null ) {
            return null;
        }

        DayAssignmentDTO dayAssignmentDTO = new DayAssignmentDTO();

        dayAssignmentDTO.setId( dayAssignment.getId() );
        dayAssignmentDTO.setFitnessCenter( dayAssignment.getFitnessCenter() );
        if ( dayAssignment.getDay() != null ) {
            dayAssignmentDTO.setDay( Enum.valueOf( Day.class, dayToString( dayAssignment.getDay() ) ) );
        }

        return dayAssignmentDTO;
    }

    @Override
    public DayAssignment mapDayAssignmentDTOToDayAssignment(DayAssignmentDTO dayAssignmentDTO) {
        if ( dayAssignmentDTO == null ) {
            return null;
        }

        DayAssignment dayAssignment = new DayAssignment();

        dayAssignment.setId( dayAssignmentDTO.getId() );
        dayAssignment.setFitnessCenter( dayAssignmentDTO.getFitnessCenter() );
        if ( dayAssignmentDTO.getDay() != null ) {
            dayAssignment.setDay( stringToDay( dayAssignmentDTO.getDay().name() ) );
        }

        return dayAssignment;
    }
}
