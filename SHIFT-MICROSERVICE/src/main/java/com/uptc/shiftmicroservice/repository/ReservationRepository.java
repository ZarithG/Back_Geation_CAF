package com.uptc.shiftmicroservice.repository;

import com.uptc.shiftmicroservice.entity.Reservation;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    //Método para obtener el número de cupos disponibles en un horario determinado de un turno
    int countByShiftInstance(ShiftInstance shiftInstance);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.shiftInstance = :shiftInstance")
    int countReservationsByShiftInstance(@Param("shiftInstance") ShiftInstance shiftInstance);

    @Query("SELECT r FROM Reservation r WHERE r.reservationEnum = 'SCHEDULED' AND r.shiftInstance.date = CURRENT_DATE AND r.userId = :userId")
    List<Reservation> findAllByReservationEnumScheduledAndShiftInstanceDateIsTodayForUser(@Param("userId") int userId);

    List<Reservation> findByShiftInstance_Id(long idShift);

    @Query("SELECT r FROM Reservation r WHERE r.reservationEnum = 'SCHEDULED' AND r.userId = :userId")
    List<Reservation> findAllByReservationEnumScheduled(int userId);
}
