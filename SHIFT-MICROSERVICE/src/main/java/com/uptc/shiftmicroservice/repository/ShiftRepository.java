package com.uptc.shiftmicroservice.repository;

import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift,Integer> {

    //Consulata para obtener un turno específico
    Optional<Shift> findByIdAndDayAssignmentId(int shiftId,int dayAssignmentId);
    // Método para obtener los Shifts asociados a un DayAssignment específico
    List<Shift> findByDayAssignment(DayAssignment dayAssignment);

    //Consulta para listar los turnos de un Día de agendamiento
    List<Shift> findByDayAssignmentId(int dayAssignmentId);


    //Consulta para eliminar un turno
    @Transactional
    @Modifying
    @Query("DELETE FROM Shift s WHERE s.id = :shiftId AND s.dayAssignment.id = :dayAssignmentId")
    int deleteByIdAndDayAssignmentId(int shiftId, int dayAssignmentId);
}
