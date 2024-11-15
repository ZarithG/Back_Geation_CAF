package com.uptc.shiftmicroservice.repository;

import com.uptc.shiftmicroservice.entity.Reservation;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    //Método para obtener el número de cupos disponibles en un horario determinado de un turno
    int countByShiftInstance(ShiftInstance shiftInstance);

    @Query("SELECT r FROM Reservation r WHERE r.reservationEnum = 'NOT_ATTENDED' AND r.shiftInstance.date = CURRENT_DATE AND r.userId = :userId")
    List<Reservation> findAllByReservationEnumNotAttendedAndShiftInstanceDateIsTodayForUser(@Param("userId") int userId);

}
