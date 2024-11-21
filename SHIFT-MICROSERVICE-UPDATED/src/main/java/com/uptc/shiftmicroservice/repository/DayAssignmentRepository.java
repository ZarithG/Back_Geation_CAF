package com.uptc.shiftmicroservice.repository;

import com.uptc.shiftmicroservice.entity.Day;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface DayAssignmentRepository extends JpaRepository<DayAssignment, Integer> {

    // Método para obtener los DayAssignment de un fitnessCenter específico
    List<DayAssignment> findByFitnessCenter(int fitnessCenter);
//    Optional<DayAssignment> findDayAssignmentByDayAndFitnessCenter(Day day, int fitnessCenter);

    //Método para listar los DayAssignment de un CAF espefíco
    @Query("SELECT d FROM DayAssignment d WHERE d.day = :day AND d.fitnessCenter = :fitnessCenterId")
    Optional<DayAssignment> findDayAssignmentByDayAndFitnessCenter(@Param("day")Day day, @Param("fitnessCenterId")int fitnessCenterId);



}
