package com.uptc.shiftmicroservice.repository;

import com.uptc.shiftmicroservice.entity.Reservation;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftInstanceRepository extends JpaRepository<ShiftInstance, Long> {

    @Query("SELECT s FROM ShiftInstance s WHERE s.state = true ORDER BY s.date DESC")
    Optional<ShiftInstance> findTopByStateTrueOrderByDateDesc();

    @Query("SELECT s FROM ShiftInstance s JOIN s.dayAssignment d WHERE d.fitnessCenter = :fitnessCenter AND s.date = :date AND s.state = TRUE")
    List<ShiftInstance> findShiftInstancesByFitnessCenterAndDate(@Param("fitnessCenter") int fitnessCenter, @Param("date") LocalDate date);

}
