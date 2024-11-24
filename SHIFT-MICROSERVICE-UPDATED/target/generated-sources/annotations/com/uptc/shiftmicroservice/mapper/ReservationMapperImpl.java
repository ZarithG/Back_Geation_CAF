package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.ReservationDTO;
import com.uptc.shiftmicroservice.entity.Reservation;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-21T16:06:48-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public ReservationDTO mapReservationToDTO(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationDTO reservationDTO = new ReservationDTO();

        if ( reservation.getId() != null ) {
            reservationDTO.setId( reservation.getId() );
        }
        reservationDTO.setUserId( reservation.getUserId() );
        reservationDTO.setDateReservation( reservation.getDateReservation() );
        reservationDTO.setReservationEnum( reservation.getReservationEnum() );

        return reservationDTO;
    }

    @Override
    public Reservation mapReservationDTOToReservation(ReservationDTO reservationDTO) {
        if ( reservationDTO == null ) {
            return null;
        }

        Reservation reservation = new Reservation();

        reservation.setId( reservationDTO.getId() );
        reservation.setUserId( reservationDTO.getUserId() );
        reservation.setDateReservation( reservationDTO.getDateReservation() );
        reservation.setReservationEnum( reservationDTO.getReservationEnum() );

        return reservation;
    }
}
