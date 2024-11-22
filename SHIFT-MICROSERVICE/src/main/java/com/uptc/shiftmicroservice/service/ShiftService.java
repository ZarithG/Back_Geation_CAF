package com.uptc.shiftmicroservice.service;

import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.mapper.ShiftMapper;
import com.uptc.shiftmicroservice.repository.DayAssignmentRepository;
import com.uptc.shiftmicroservice.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    DayAssignmentRepository dayAssignmentRepository;

    /**
     * Método que busca un turno (Shift) en la base de datos por su ID y el ID de DayAssignment.
     *
     * @param shiftDTO Datos del turno a buscar.
     * @return Un Optional con el turno encontrado o vacío si no existe.
     */
    public Optional<Shift> findShiftById(ShiftDTO shiftDTO) {
        Optional<Shift> shift = shiftRepository.findByIdAndDayAssignmentId(shiftDTO.getId(), shiftDTO.getDayAssignment());
        return shift;
    }

    /**
     * Método para obtener la lista de turnos (Shift) asociados a un DayAssignment específico.
     *
     * @param dayAssignmentId ID del DayAssignment.
     * @return Lista de turnos en formato DTO.
     */
    public List<ShiftDTO> getShiftsByDayAssignment(int dayAssignmentId) {
        List<Shift> shiftByDayAssignment = shiftRepository.findByDayAssignmentId(dayAssignmentId);
        return shiftByDayAssignment.stream().map(ShiftMapper.INSTANCE::shiftToShiftDTO).collect(Collectors.toList());
    }

    /**
     * Obtiene los turnos asociados a un DayAssignment ordenados por hora de inicio.
     *
     * @param dayAssignment ID del DayAssignment.
     * @return Un conjunto ordenado de turnos (NavigableSet).
     */
    public NavigableSet<Shift> getOrderedShiftsByDayAssignment(int dayAssignment) {
        List<Shift> shiftByDayAssignment = shiftRepository.findByDayAssignmentId(dayAssignment);
        NavigableSet<Shift> orderedShifts = new TreeSet<>(Comparator.comparing(Shift::getStartTime));
        orderedShifts.addAll(shiftByDayAssignment);
        return orderedShifts;
    }

    /**
     * Verifica si la hora de inicio de un turno es anterior a su hora de fin.
     *
     * @param shiftDTO Turno a verificar.
     * @return true si la hora de inicio es anterior a la de fin, false en caso contrario.
     */
    public Boolean verifyStartAndEndTime(ShiftDTO shiftDTO) {
        Shift shift = ShiftMapper.INSTANCE.shiftDTOToShift(shiftDTO);
        System.out.println(shift.getStartTime() + " " + shift.getEndTime());
        return shift.getStartTime().isBefore(shift.getEndTime());
    }

    /**
     * Agrega un nuevo turno (Shift) a un DayAssignment validando que no haya solapamientos.
     *
     * @param dayAssignment El DayAssignment asociado al turno.
     * @param newShift El nuevo turno a agregar.
     * @return Optional con el turno guardado si no hay solapamientos, vacío en caso contrario.
     */
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

        // Validaciones de solapamiento según los turnos adyacentes
        if (previousShift == null && nextShift == null) {
            newShift.setDayAssignment(dayAssignment);
            Shift savedShift = shiftRepository.save(newShift);
            return Optional.of(savedShift);
        }
        if (previousShift != null && nextShift == null && newShift.getStartTime().isBefore(previousShift.getEndTime())) {
            System.out.println("Error: El nuevo turno se solapa con el turno anterior.");
            return Optional.empty();
        }
        if (nextShift != null && previousShift == null && newShift.getEndTime().isAfter(nextShift.getStartTime())) {
            System.out.println("Error: El nuevo turno se solapa con el turno siguiente.");
            return Optional.empty();
        }
        if (previousShift != null && nextShift != null &&
                (newShift.getStartTime().isBefore(previousShift.getEndTime()) ||
                        newShift.getEndTime().isAfter(nextShift.getStartTime()))) {
            System.out.println("Error: El nuevo turno se solapa con turnos adyacentes.");
            return Optional.empty();
        }

        // Si no hay conflictos, guardar el nuevo turno
        newShift.setDayAssignment(dayAssignment);
        Shift savedShift = shiftRepository.save(newShift);
        return Optional.of(savedShift);
    }

    /**
     * Edita un turno existente validando que no haya solapamientos.
     *
     * @param dayAssignment El DayAssignment asociado al turno.
     * @param newShift El turno modificado.
     * @return Optional con el turno guardado si no hay solapamientos, vacío en caso contrario.
     */
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

        // Validaciones de solapamiento similares al método addShift
        if (previousShift == null && nextShift == null) {
            Shift savedShift = shiftRepository.save(newShift);
            return Optional.of(savedShift);
        }
        if (previousShift != null && nextShift == null && newShift.getStartTime().isBefore(previousShift.getEndTime())) {
            System.out.println("Error: El nuevo turno se solapa con el turno anterior.");
            return Optional.empty();
        }
        if (nextShift != null && previousShift == null && newShift.getEndTime().isAfter(nextShift.getStartTime())) {
            System.out.println("Error: El nuevo turno se solapa con el turno siguiente.");
            return Optional.empty();
        }
        if (previousShift != null && nextShift != null &&
                (newShift.getStartTime().isBefore(previousShift.getEndTime()) ||
                        newShift.getEndTime().isAfter(nextShift.getStartTime()))) {
            System.out.println("Error: El nuevo turno se solapa con turnos adyacentes.");
            return Optional.empty();
        }

        // Si no hay conflictos, guardar el turno editado
        Shift savedShift = shiftRepository.save(newShift);
        return Optional.of(savedShift);
    }


    public Boolean deleteShift(int dayAssignment, ShiftDTO shiftToDelete){
        return (shiftRepository.deleteByIdAndDayAssignmentId(shiftToDelete.getId(),dayAssignment)) > 0;
    }
}
