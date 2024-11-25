package com.uptc.shiftmicroservice.repository;

import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift,Integer> {

    //Consulata para obtener un turno específico
    @Query("SELECT s from Shift s WHERE s.id = :shiftId AND s.status = true AND s.dayAssignment = :dayAssignment")
    Optional<Shift> findByIdAndDayAssignmentId(@Param("shiftId")int shiftId,@Param("dayAssignment")int dayAssignmentId);
    // Método para obtener los Shifts asociados a un DayAssignment específico
    @Query("SELECT s from Shift s WHERE s.status = true AND s.dayAssignment = :dayAssignment")
    List<Shift> findByDayAssignment(@Param("dayAssignment") DayAssignment dayAssignment);

    //Consulta para listar los turnos de un Día de agendamiento
    List<Shift> findByDayAssignmentId(int dayAssignmentId);


        //Consulta para los reportes sobre la asistencia a los CAF en turnos específicos
    @Query(value = """
        SELECT 
            s.id AS shiftId,
            s.start_time AS startTime,
            s.end_time AS endTime,
            s.maximum_place_available AS maximumPlaceAvailable,
            da.day AS day,
            COUNT(CASE WHEN r.reservation_enum = 'ATTENDED' THEN 1 END) AS attendedCount,
            COUNT(CASE WHEN r.reservation_enum = 'NO_ATTENDED' THEN 1 END) AS noAttendedCount
        FROM 
            shift s
        JOIN 
            day_assignment da ON s.day_assignment_id = da.id
        JOIN 
            shift_instance si ON si.shift_id = s.id
        LEFT JOIN 
            reservation r ON r.shift_id = si.id
        WHERE 
            da.fitness_center = :fitnessCenter
            AND da.day = :day
            AND si.date BETWEEN :startDate AND :endDate
        GROUP BY 
            s.id, s.start_time, s.end_time, s.maximum_place_available, da.day
        ORDER BY 
            s.start_time
        """, nativeQuery = true)
    List<Object[]> getShiftDetailsWithReservationCounts(
            @Param("fitnessCenter") int fitnessCenter,
            @Param("day") String day,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}
