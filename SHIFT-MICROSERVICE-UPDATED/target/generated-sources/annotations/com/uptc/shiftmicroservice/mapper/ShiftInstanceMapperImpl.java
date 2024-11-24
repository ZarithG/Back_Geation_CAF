package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.ShiftInstanceDTO;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-21T16:06:48-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
public class ShiftInstanceMapperImpl implements ShiftInstanceMapper {

    @Override
    public ShiftInstanceDTO shiftInstanceToShiftInstanceDTO(ShiftInstance shiftInstance) {
        if ( shiftInstance == null ) {
            return null;
        }

        ShiftInstanceDTO shiftInstanceDTO = new ShiftInstanceDTO();

        shiftInstanceDTO.setShift( shiftInstanceShiftId( shiftInstance ) );
        shiftInstanceDTO.setId( shiftInstance.getId() );
        shiftInstanceDTO.setState( shiftInstance.getState() );
        shiftInstanceDTO.setDate( shiftInstance.getDate() );
        shiftInstanceDTO.setPlaceAvailable( shiftInstance.getPlaceAvailable() );

        return shiftInstanceDTO;
    }

    @Override
    public ShiftInstance shiftInstanceDTOToShiftInstance(ShiftInstanceDTO shiftInstanceDTO) {
        if ( shiftInstanceDTO == null ) {
            return null;
        }

        ShiftInstance shiftInstance = new ShiftInstance();

        shiftInstance.setShift( shiftInstanceDTOToShift( shiftInstanceDTO ) );
        shiftInstance.setId( shiftInstanceDTO.getId() );
        shiftInstance.setState( shiftInstanceDTO.isState() );
        shiftInstance.setDate( shiftInstanceDTO.getDate() );
        shiftInstance.setPlaceAvailable( shiftInstanceDTO.getPlaceAvailable() );

        return shiftInstance;
    }

    private int shiftInstanceShiftId(ShiftInstance shiftInstance) {
        if ( shiftInstance == null ) {
            return 0;
        }
        Shift shift = shiftInstance.getShift();
        if ( shift == null ) {
            return 0;
        }
        int id = shift.getId();
        return id;
    }

    protected Shift shiftInstanceDTOToShift(ShiftInstanceDTO shiftInstanceDTO) {
        if ( shiftInstanceDTO == null ) {
            return null;
        }

        Shift shift = new Shift();

        shift.setId( (int) shiftInstanceDTO.getShift() );

        return shift;
    }
}
