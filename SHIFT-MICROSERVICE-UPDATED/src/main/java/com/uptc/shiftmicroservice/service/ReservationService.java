package com.uptc.shiftmicroservice.service;

import com.uptc.shiftmicroservice.dto.ReservationDTO;
import com.uptc.shiftmicroservice.entity.Reservation;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import com.uptc.shiftmicroservice.enums.ReservationEnum;
import com.uptc.shiftmicroservice.repository.ReservationRepository;
import com.uptc.shiftmicroservice.repository.ShiftInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
/**
 * Servicio encargado de gestionar las operaciones relacionadas con las reservas de turnos (Reservation).
 * Incluye funciones para crear, actualizar, validar, y eliminar reservas.
 */
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ShiftInstanceService shiftInstanceService;

    @Autowired
    private ShiftService shiftService;
    @Autowired
    private ShiftInstanceRepository shiftInstanceRepository;

    /**
     * Realiza una reserva para un usuario en una instancia de turno específica.
     *
     * @param reservationDTO Datos de la reserva, incluyendo el ID del turno y el ID del usuario.
     * @return Una reserva creada si es posible, o un Optional vacío si no se puede reservar.
     */
    public Optional<Reservation> reserveShiftForUser(ReservationDTO reservationDTO) {
        Optional<ShiftInstance> shiftInstance = shiftInstanceService.findShiftInstanceById(reservationDTO.getIdShiftInstance());
        if (shiftInstance.isPresent()) {
            ShiftInstance actShiftInstance = shiftInstanceService.findShiftInstanceShiftId(shiftInstance.get().getShift().getId());
            if (actShiftInstance != null){
                if (actShiftInstance.getPlaceAvailable() > 0) {
                    List<Reservation> reservations = reservationRepository.findAllByReservationEnumScheduledAndShiftInstanceDateIsTodayForUser(reservationDTO.getUserId());
                    if (reservations.isEmpty()) {
                        Reservation newReservation = new Reservation();
                        newReservation.setDateReservation(LocalDateTime.now());
                        newReservation.setShiftInstance(actShiftInstance);
                        newReservation.setUserId(reservationDTO.getUserId());
                        newReservation.setReservationEnum(ReservationEnum.SCHEDULED);

                        ShiftInstance newShiftInstance = new ShiftInstance();
                        newShiftInstance.setShift(actShiftInstance.getShift());
                        newShiftInstance.setDate(actShiftInstance.getDate());
                        newShiftInstance.setPlaceAvailable(actShiftInstance.getPlaceAvailable() - 1);

                        if(newShiftInstance.getPlaceAvailable() == 0){
                            newShiftInstance.setState(false);
                        }else{
                            newShiftInstance.setState(true);
                        }

                        ShiftInstance saveShiftInstance = shiftInstanceRepository.save(newShiftInstance);
                        if(saveShiftInstance.getId() != 0){
                            return Optional.of(reservationRepository.save(newReservation));
                        }
                        return Optional.empty();
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Valida que la fecha y hora de reserva sea anterior a la hora de finalización del turno.
     *
     * @param reserveDate Fecha y hora de la reserva.
     * @param endDateTime Fecha y hora de finalización del turno.
     * @return Diferencia en horas si es válida; de lo contrario, -1.
     */
    public int isReserveDateLowEndTimeShiftInstance(LocalDateTime reserveDate, LocalDateTime endDateTime) {
        if (reserveDate.isBefore(endDateTime)) {
            return (int) ChronoUnit.HOURS.between(reserveDate, endDateTime);
        } else {
            return -1;
        }
    }

    /**
     * Registra una reserva como atendida.
     *
     * @param idShiftInstance ID de la instancia de turno.
     * @return La reserva actualizada si existe, o un Optional vacío.
     */
    public Optional<Reservation> registryReservation(long idShiftInstance) {
        Optional<Reservation> reservation = reservationRepository.findById(idShiftInstance);
        if (reservation.isPresent()) {
            reservation.get().setReservationEnum(ReservationEnum.ATTENDED);
            return Optional.of(reservationRepository.save(reservation.get()));
        }
        return Optional.empty();
    }

    /**
     * Registra todas las reservas de un turno como no atendidas si no fueron completadas.
     *
     * @param idFinishShiftInstance ID de la instancia de turno finalizado.
     * @return `true` si todas las reservas se actualizaron; `false` si no hay reservas.
     */
    public boolean registryAttendedAllReservationShift(long idFinishShiftInstance) {
        List<Reservation> reservationList = reservationRepository.findByShiftInstance_Id(idFinishShiftInstance);
        if (!reservationList.isEmpty()) {
            for (Reservation reservation : reservationList) {
                if (reservation.getReservationEnum().equals(ReservationEnum.SCHEDULED)) {
                    reservation.setReservationEnum(ReservationEnum.NOT_ATTENDED);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Obtiene todas las reservas asociadas a un usuario.
     *
     * @param userId ID del usuario.
     * @return Lista de reservas realizadas por el usuario.
     */
    public List<Reservation> getAllReservationForUser(int userId) {
        return reservationRepository.findAllByReservationEnumScheduled(userId);
    }

    /**
     * Elimina una reserva por su ID.
     *
     * @param idReservation ID de la reserva.
     * @return La reserva eliminada, o `null` si no existe.
     */
    public Optional<Reservation> deleteReservation(ReservationDTO reservationDTO) {
        Optional<ShiftInstance> shiftInstance = shiftInstanceService.findShiftInstanceById(reservationDTO.getIdShiftInstance());
        if (shiftInstance.isPresent()) {
            ShiftInstance actShiftInstance = shiftInstanceService.findShiftInstanceShiftId(shiftInstance.get().getShift().getId());

            if (actShiftInstance != null) {
                ShiftInstance newShiftInstance = new ShiftInstance();
                newShiftInstance.setShift(actShiftInstance.getShift());
                newShiftInstance.setDate(actShiftInstance.getDate());
                newShiftInstance.setPlaceAvailable(actShiftInstance.getPlaceAvailable() + 1);

                if(!actShiftInstance.getState()){
                    newShiftInstance.setState(true);
                }

                ShiftInstance saveShiftInstance = shiftInstanceRepository.save(newShiftInstance);

                if(saveShiftInstance.getId() != 0){
                    return reservationRepository.findById(reservationDTO.getId());
                }

                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    /**
     * Obtiene todas las reservas asociadas a una instancia de turno específica.
     *
     * @param actualInstanceId ID de la instancia de turno.
     * @return Lista de reservas asociadas.
     */
    public List<Reservation> getAllReservationsByActualShiftInstanceId(long actualInstanceId) {
        return reservationRepository.findAllByShiftInstance_Id(actualInstanceId);
    }
}

