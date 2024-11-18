package com.uptc.shiftmicroservice.service;

import com.uptc.shiftmicroservice.dto.ReservationDTO;
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

    public Optional<Reservation> reserveShiftForUser(ReservationDTO reservationDTO){
        Optional<ShiftInstance> shiftInstance = shiftInstanceService.findShiftInstanceById(reservationDTO.getIdShiftInstance());
        System.out.println("ID INSTANCE "+shiftInstance.get().getId());
        if(shiftInstance.isPresent()){
            int totalReserves = countReversesToShiftIntance(shiftInstance.get());
            //Verificar los cupos disponibles
            System.out.println("CUPOS reservados:" + totalReserves);
            if(( shiftInstance.get().getPlaceAvailable() - totalReserves) > 0){
                List<Reservation> reservation = reservationRepository.findAllByReservationEnumScheduledAndShiftInstanceDateIsTodayForUser(reservationDTO.getUserId());
                System.out.println("VERIFICANDO OTROS TURNOS");
                if(reservation.isEmpty()){
                    Reservation newReservation = new Reservation();
                    newReservation.setDateReservation(reservationDTO.getDateReservation());
                    newReservation.setShiftInstance(shiftInstance.get());
                    newReservation.setDateReservation(reservationDTO.getDateReservation());
                    newReservation.setUserId(reservationDTO.getUserId());
                    newReservation.setReservationEnum(ReservationEnum.NOT_ATTENDED);
                    return Optional.of(reservationRepository.save(newReservation));
                } else{
                    return Optional.empty();
                }
            }else{
                return Optional.empty();
            }
        }

        return Optional.empty();
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


    public Optional<Reservation> registryReservation(long idShiftInstance){
        Optional<Reservation> reservation = reservationRepository.findById(idShiftInstance);
        if (reservation.isPresent()) {
            reservation.get().setReservationEnum(ReservationEnum.ATTENDED);
            return Optional.of(reservationRepository.save(reservation.get()));
            //Verificar que si es el último turno asignado para ese horario el horario ya pase a estado false

        }
        return Optional.empty();
    }

    public boolean registryAttendedAllReservationShift(long idFinishShiftInstance){
        List<Reservation> reservationList = reservationRepository.findByShiftInstance_Id(idFinishShiftInstance);
        if(reservationList.isEmpty()){
            for(Reservation reservation : reservationList){
                if(reservation.getReservationEnum().name().equals("SCHEDULED")){
                    reservation.setReservationEnum(ReservationEnum.NOT_ATTENDED);
                }
            }
            return true;
        }else
            return false;
    }

    public List<Reservation> getAllReservationForUser(int userId){
        return reservationRepository.findAllByReservationEnumScheduled(userId);
    }

    public Reservation deleteReservation(long idReservation){
        return reservationRepository.findById(idReservation).orElse(null);
    }
}
