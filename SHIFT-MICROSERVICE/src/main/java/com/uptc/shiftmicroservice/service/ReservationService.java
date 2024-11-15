package com.uptc.shiftmicroservice.service;

import com.uptc.shiftmicroservice.dto.ReservationDTO;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Reservation;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import com.uptc.shiftmicroservice.enums.ReservationEnum;
import com.uptc.shiftmicroservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ShiftInstanceService shiftInstanceService;

    @Autowired
    private ShiftService shiftService;

    public void reserveShiftForUser(ReservationDTO reservationDTO){
        Optional<ShiftInstance> shiftInstance = shiftInstanceService.findShiftInstanceById(reservationDTO.getIdShiftInstance());
        int totalReserves = countReversesToShiftIntance(shiftInstance.get());
        //Verificar los cupos disponibles
        if((shiftInstance.get().getPlaceAvailable() - totalReserves) > 0){
            List<Reservation> reservation = reservationRepository.findAllByReservationEnumNotAttendedAndShiftInstanceDateIsTodayForUser(reservationDTO.getUserId());

            if(reservation.isEmpty()){


            }
        }else{

            System.out.println("Turnos llenos");
        }
    }

    //Verificar el número de reservas para un shiftInstance específico
    public int countReversesToShiftIntance(ShiftInstance shiftInstance){
        return reservationRepository.countByShiftInstance(shiftInstance);
    }

    //Método para validar que las fechas y hora para la reservación sea menor a la fecha y hora de finalización del turno
    public int isReserveDateLowEndTimeShiftInstance(LocalDateTime reserveDate, LocalDateTime endDateTime){
        if (reserveDate.isBefore(endDateTime)) {
            // Calcula la diferencia en horas entre reserveDate y endDateTime
            return (int) ChronoUnit.HOURS.between(reserveDate, endDateTime);
        } else {
            // Si la fecha de reserva no es anterior, retorna -1 o un valor indicando que no es válida
            return -1;
        }
    }

    public Reservation reserveShift(ReservationDTO newReservationDTO) {
        Reservation reservation = new Reservation();
        reservation.setReservationEnum(ReservationEnum.NOT_ATTENDED);

        return reservationRepository.save(reservation);
    }

    public Boolean existReservationToSameDay(int idUser, int idShift) {
        Boolean exist = false;

        return exist;
    }


}
