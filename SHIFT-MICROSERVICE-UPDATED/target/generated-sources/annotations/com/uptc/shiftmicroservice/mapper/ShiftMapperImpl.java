package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-21T16:17:58-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
public class ShiftMapperImpl implements ShiftMapper {

    @Override
    public ShiftDTO shiftToShiftDTO(Shift shift) {
        if ( shift == null ) {
            return null;
        }

        ShiftDTO shiftDTO = new ShiftDTO();

        shiftDTO.setDayAssignment( shiftDayAssignmentId( shift ) );
        shiftDTO.setId( shift.getId() );
        shiftDTO.setStartTime( shift.getStartTime() );
        shiftDTO.setEndTime( shift.getEndTime() );
        shiftDTO.setMaximumPlaceAvailable( shift.getMaximumPlaceAvailable() );

        return shiftDTO;
    }

    @Override
    public Shift shiftDTOToShift(ShiftDTO shiftDTO) {
        if ( shiftDTO == null ) {
            return null;
        }

        Shift shift = new Shift();

        shift.setDayAssignment( shiftDTOToDayAssignment( shiftDTO ) );
        shift.setId( shiftDTO.getId() );
        shift.setStartTime( shiftDTO.getStartTime() );
        shift.setEndTime( shiftDTO.getEndTime() );
        shift.setMaximumPlaceAvailable( shiftDTO.getMaximumPlaceAvailable() );

        return shift;
    }

    private int shiftDayAssignmentId(Shift shift) {
        if ( shift == null ) {
            return 0;
        }
        DayAssignment dayAssignment = shift.getDayAssignment();
        if ( dayAssignment == null ) {
            return 0;
        }
        int id = dayAssignment.getId();
        return id;
    }

    protected DayAssignment shiftDTOToDayAssignment(ShiftDTO shiftDTO) {
        if ( shiftDTO == null ) {
            return null;
        }

        DayAssignment dayAssignment = new DayAssignment();

        dayAssignment.setId( shiftDTO.getDayAssignment() );

        return dayAssignment;
    }
}
