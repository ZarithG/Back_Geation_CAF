package com.uptc.shiftmicroservice.repository;

import com.uptc.shiftmicroservice.entity.Reservation;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    //Método para obtener el número de cupos disponibles en un horario determinado de un turno
    int countByshiftInstance(ShiftInstance shiftInstance);

    //Consulta para verificar que el usuario no tenga un turno agendado en esta fecha

//    @Query("SELECT r FROM Reservation r " +
//            "LEFT JOIN r.shiftInstance s " +
//            "WHERE r.userId = :idUser " +
//            "AND s.idDayAssignment = :idDayAssignment " +
//            "AND s.state = 1")
//    List<Reservation> findReservationsByUserAndDayAssignmentAndState(Long idUser, int idDayAssignment);

    //Consulta de las reservaciones activas para un día
    Reservation findByUserIdAndShiftInstance_IdDayAssignmentAndShiftInstance_State(
            Long userId, int idDayAssignment, int state
    );
}
