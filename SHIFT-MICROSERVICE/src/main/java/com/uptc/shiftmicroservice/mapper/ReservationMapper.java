package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.ReservationDTO;
import com.uptc.shiftmicroservice.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface ReservationMapper {
    ShiftMapper INSTANCE = Mappers.getMapper(ShiftMapper.class);

    ReservationDTO mapReservationToDTO(Reservation reservation);
    Reservation mapReservationDTOToReservation(ReservationDTO reservationDTO);
}
