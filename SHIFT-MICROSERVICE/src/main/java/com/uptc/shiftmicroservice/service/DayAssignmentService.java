package com.uptc.shiftmicroservice.service;

import com.uptc.shiftmicroservice.dto.DayAssignmentDTO;
import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.Day;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.mapper.DayAssignmentMapper;
import com.uptc.shiftmicroservice.mapper.ShiftMapper;
import com.uptc.shiftmicroservice.repository.DayAssignmentRepository;
import com.uptc.shiftmicroservice.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
/**
 * Servicio encargado de gestionar las asignaciones de días (DayAssignment) y sus turnos asociados (Shift).
 * Proporciona métodos para consultar, validar, crear y convertir entidades relacionadas.
 */
public class DayAssignmentService {

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    DayAssignmentRepository dayAssignmentRepository;

    /**
     * Obtiene las asignaciones de días (DayAssignment) de un centro de acondicionamiento físico (CAF) específico,
     * incluyendo los turnos (Shift) asociados a cada asignación.
     *
     * @param fitnessCenterId ID del centro de acondicionamiento físico.
     * @return Lista de objetos DayAssignmentDTO con información de los turnos asociados.
     */
    public List<DayAssignmentDTO> getDayAssignmentWithShiftByFitnessCenter(int fitnessCenterId) {
        List<DayAssignment> dayAssignments = dayAssignmentRepository.findByFitnessCenter(fitnessCenterId);

        return dayAssignments.stream().map(dayAssignment -> {
            // Convertir DayAssignment a DayAssignmentDTO
            DayAssignmentDTO dayAssignmentDto = DayAssignmentMapper.INSTANCE.mapDayAssignmentToDayAssignmentDTO(dayAssignment);

            // Consultar los turnos (Shift) asociados al DayAssignment
            List<Shift> shifts = shiftRepository.findByDayAssignment(dayAssignment);
            List<ShiftDTO> shiftDTOS = shifts.stream()
                    .map(ShiftMapper.INSTANCE::shiftToShiftDTO)
                    .collect(Collectors.toList());

            // Asignar los turnos al DTO
            dayAssignmentDto.setShifts(shiftDTOS);
            return dayAssignmentDto;
        }).collect(Collectors.toList());
    }

    /**
     * Valida si existe un DayAssignment para el día y el centro de acondicionamiento físico especificados.
     * Si no existe, crea un nuevo DayAssignment.
     *
     * @param dayAssignmentDTO Datos del DayAssignment a validar.
     * @return El DayAssignment existente o uno nuevo si no existía.
     */
    public DayAssignment validarDayAssignment(DayAssignmentDTO dayAssignmentDTO) {
        Optional<DayAssignment> dayAssignmentFind = this.getDayAssignmentByDayAndFinessCenter(
                dayAssignmentDTO.getDay(), dayAssignmentDTO.getFitnessCenter());

        DayAssignment dayAssignment;
        if (dayAssignmentFind.isEmpty()) {
            System.out.println("Crear nuevo DayAssignment");
            dayAssignment = this.saveDayAssignment(dayAssignmentDTO);
        } else {
            dayAssignment = dayAssignmentFind.get();
            System.out.println("Existe DayAssignment: " + dayAssignment.getId());
        }
        return dayAssignment;
    }

    /**
     * Guarda un nuevo DayAssignment en la base de datos.
     *
     * @param dayAssignmentDTO Datos del DayAssignment a guardar.
     * @return El DayAssignment creado.
     */
    public DayAssignment saveDayAssignment(DayAssignmentDTO dayAssignmentDTO) {
        DayAssignment day = DayAssignmentMapper.INSTANCE.mapDayAssignmentDTOToDayAssignment(dayAssignmentDTO);
        return dayAssignmentRepository.save(day);
    }

    /**
     * Busca un DayAssignment por el día y el ID del centro de acondicionamiento físico.
     *
     * @param day Día del DayAssignment.
     * @param fitnessCenterId ID del centro de acondicionamiento físico.
     * @return Un Optional con el DayAssignment si existe, o vacío si no se encuentra.
     */
    public Optional<DayAssignment> getDayAssignmentByDayAndFinessCenter(Day day, int fitnessCenterId) {
        return dayAssignmentRepository.findDayAssignmentByDayAndFitnessCenter(day, fitnessCenterId);
    }

    /**
     * Convierte un DayAssignment a un DayAssignmentDTO incluyendo los turnos asociados.
     *
     * @param dayAssignment DayAssignment a convertir.
     * @param shifts Lista de turnos asociados al DayAssignment.
     * @return Un DayAssignmentDTO con la información del DayAssignment y sus turnos.
     */
    public DayAssignmentDTO mapDayAssignmenToDTOWithShifts(DayAssignment dayAssignment, List<Shift> shifts) {
        DayAssignmentDTO dto = DayAssignmentMapper.INSTANCE.mapDayAssignmentToDayAssignmentDTO(dayAssignment);
        List<ShiftDTO> shiftDTOs = shifts.stream()
                .map(ShiftMapper.INSTANCE::shiftToShiftDTO)
                .collect(Collectors.toList());
        dto.setShifts(shiftDTOs);
        return dto;
    }
}

