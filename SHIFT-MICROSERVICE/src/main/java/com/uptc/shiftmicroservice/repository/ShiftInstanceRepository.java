package com.uptc.shiftmicroservice.repository;

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
    Optional<List<ShiftInstance>> findTopByStateTrueOrderByDateDesc();

    @Query("SELECT s FROM ShiftInstance s JOIN s.dayAssignment d WHERE d.fitnessCenter = :fitnessCenter AND s.date = :date AND s.state = TRUE")
    List<ShiftInstance> findShiftInstancesByFitnessCenterAndDate(@Param("fitnessCenter") int fitnessCenter, @Param("date") LocalDate date);

    @Query("SELECT s FROM ShiftInstance s " +
            "WHERE s.fitnessCenter = :fitnessCenter " +
            "AND s.date = CURRENT_DATE " +
            "AND CURRENT_TIME BETWEEN s.startTime AND s.endTime")
    List<ShiftInstance> findActiveShiftsByFitnessCenterAndCurrentTime(
            @Param("fitnessCenter") int fitnessCenter);
}
