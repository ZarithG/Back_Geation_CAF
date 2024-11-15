package com.uptc.shiftmicroservice.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.uptc.shiftmicroservice.dto.DayAssignmentDTO;
import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.Day;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import com.uptc.shiftmicroservice.mapper.ShiftMapper;
import com.uptc.shiftmicroservice.repository.DayAssignmentRepository;
import com.uptc.shiftmicroservice.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    DayAssignmentRepository dayAssignmentRepository;


    //Método que retorna un Shift de la Base de datos
    public  Optional<Shift> findShiftById(ShiftDTO shiftDTO){
        Optional<Shift> shift = shiftRepository.findByIdAndDayAssignmentId(shiftDTO.getId(),shiftDTO.getDayAssignment());
//        Optional<Shift> shift = shiftRepository.findById(shiftDTO.getId());
        return shift;
    }


    //Método para listar los Shift de un DayAssignment
    public List<ShiftDTO> getShiftsByDayAssignment(int dayAssignmentId) {
        List<Shift> shiftByDayAssignment = shiftRepository.findByDayAssignmentId(dayAssignmentId);
        return shiftByDayAssignment.stream().map(ShiftMapper.INSTANCE::shiftToShiftDTO).collect(Collectors.toList());
    }

    //Método que obtiene una estructura de datos con los shift ordenados según la hora de inicio
    public NavigableSet<Shift> getOrderedShiftsByDayAssignment(int dayAssignment){
        List<Shift> shiftByDayAssignment = shiftRepository.findByDayAssignmentId(dayAssignment);
        NavigableSet<Shift> orderedShifts = new TreeSet<>(Comparator.comparing(Shift::getStartTime));
        orderedShifts.addAll(shiftByDayAssignment);
        return orderedShifts;
    }
    public Boolean verifyStartAndEndTime(ShiftDTO shiftDTO) {
        Shift shift = ShiftMapper.INSTANCE.shiftDTOToShift(shiftDTO);
        System.out.println(shift.getStartTime()+" "+shift.getEndTime());
        return shift.getStartTime().isBefore(shift.getEndTime());
    }
    public Optional<Shift> addShift(DayAssignment dayAssignment, Shift newShift) {
        NavigableSet<Shift> shifts = getOrderedShiftsByDayAssignment(dayAssignment.getId());
        Shift previousShift = shifts.lower(newShift);
        Shift nextShift = shifts.higher(newShift);

        if (previousShift != null) {
            System.out.println("Previous Shift: " + previousShift.getStartTime() + " - " + previousShift.getEndTime());
        }
        if (nextShift != null) {
            System.out.println("Next Shift: " + nextShift.getStartTime() + " - " + nextShift.getEndTime());
        }

        // Caso 1: No hay turnos adyacentes (el nuevo turno es el único)
        if (previousShift == null && nextShift == null) {
            newShift.setDayAssignment(dayAssignment);
            Shift savedShift = shiftRepository.save(newShift);
            return Optional.of(savedShift);
        }

        // Caso 2: Hay solo un turno previo
        if (previousShift != null && nextShift == null) {
            // Validar que el nuevo turno comience después del turno anterior
            if (newShift.getStartTime().isBefore(previousShift.getEndTime())) {
                System.out.println("Error: El nuevo turno se solapa con el turno anterior.");
                return Optional.empty();
            }
        }

        // Caso 3: Hay solo un turno siguiente
        if (nextShift != null && previousShift == null) {
            // Validar que el nuevo turno termine antes del turno siguiente
            if (newShift.getEndTime().isAfter(nextShift.getStartTime())) {
                System.out.println("Error: El nuevo turno se solapa con el turno siguiente.");
                return Optional.empty();
            }
        }

        // Caso 4: Hay turnos previos y siguientes
        if (previousShift != null && nextShift != null) {
            // Validar que no se solape ni con el turno anterior ni con el siguiente
            if (newShift.getStartTime().isBefore(previousShift.getEndTime()) ||
                    newShift.getEndTime().isAfter(nextShift.getStartTime())) {
                System.out.println("Error: El nuevo turno se solapa con turnos adyacentes.");
                return Optional.empty();
            }
        }

        // Si no hay conflictos, asignar y guardar el turno
        newShift.setDayAssignment(dayAssignment);
        Shift savedShift = shiftRepository.save(newShift);
        return Optional.of(savedShift);

    }

    public void saveAllShifts(DayAssignment dayAssignment,List<Shift> shifts){

    }
    public Optional<Shift> editShift(DayAssignment dayAssignment, Shift newShift) {
        NavigableSet<Shift> shifts = getOrderedShiftsByDayAssignment(dayAssignment.getId());
        Shift previousShift = shifts.lower(newShift);
        Shift nextShift = shifts.higher(newShift);

        if (previousShift != null) {
            System.out.println("Previous Shift: " + previousShift.getStartTime() + " - " + previousShift.getEndTime());
        }
        if (nextShift != null) {
            System.out.println("Next Shift: " + nextShift.getStartTime() + " - " + nextShift.getEndTime());
        }

        // Caso 1: No hay turnos adyacentes (el nuevo turno es el único)
        if (previousShift == null && nextShift == null) {
            Shift savedShift = shiftRepository.save(newShift);
            return Optional.of(savedShift);
        }

        // Caso 2: Hay solo un turno previo
        if (previousShift != null && nextShift == null) {
            // Validar que el nuevo turno comience después del turno anterior
            if (newShift.getStartTime().isBefore(previousShift.getEndTime())) {
                System.out.println("Error: El nuevo turno se solapa con el turno anterior.");
                return Optional.empty();
            }
        }

        // Caso 3: Hay solo un turno siguiente
        if (nextShift != null && previousShift == null) {
            // Validar que el nuevo turno termine antes del turno siguiente
            if (newShift.getEndTime().isAfter(nextShift.getStartTime())) {
                System.out.println("Error: El nuevo turno se solapa con el turno siguiente.");
                return Optional.empty();
            }
        }

        // Caso 4: Hay turnos previos y siguientes
        if (previousShift != null && nextShift != null) {
            // Validar que no se solape ni con el turno anterior ni con el siguiente
            if (newShift.getStartTime().isBefore(previousShift.getEndTime()) ||
                    newShift.getEndTime().isAfter(nextShift.getStartTime())) {
                System.out.println("Error: El nuevo turno se solapa con turnos adyacentes.");
                return Optional.empty();
            }
        }
        Shift savedShift = shiftRepository.save(newShift);
        return Optional.of(savedShift);
    }

    public Boolean deleteShift(int dayAssignment, ShiftDTO shiftToDelete){
        return (shiftRepository.deleteByIdAndDayAssignmentId(shiftToDelete.getId(),dayAssignment)) > 0;
    }

    //Método que retorna la lista de turnos disponibles para un día en un CAF
    public List<ShiftDTO> getAvailableShiftsForTodayByFitnessCenter(){
        List<ShiftDTO> shiftDTOs = new ArrayList<>();


        return shiftDTOs;
    }


}