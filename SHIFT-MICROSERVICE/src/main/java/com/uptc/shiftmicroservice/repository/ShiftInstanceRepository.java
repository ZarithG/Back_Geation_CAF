package com.uptc.shiftmicroservice.repository;

import com.uptc.shiftmicroservice.entity.Reservation;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShiftInstanceRepository extends JpaRepository<ShiftInstance, Long> {
    //ShiftInstance findByShiftAndDate(Shift shift, LocalDate fecha);

    @Query(value = "SELECT r. FROM Reservation LEFT JOIN ShiftInstance s ON r.shift_id = s.id WHERE r.user_id = :idUser AND s.idDayAssignment = :idDayAssignmen AND s.state = 1", nativeQuery = true)
    List<Reservation> findReservationActiveByDay(@Param("idDayAssignment") int idDayAssignment, @Param("idUser") int idUser,@Param("id") int idShift);
}
