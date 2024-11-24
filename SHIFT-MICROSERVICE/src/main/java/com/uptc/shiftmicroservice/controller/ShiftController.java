package com.uptc.shiftmicroservice.controller;

import com.uptc.shiftmicroservice.dto.ConsultShiftReportDTO;
import com.uptc.shiftmicroservice.dto.DayAssignmentDTO;
import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.dto.ShiftsReportDTO;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.mapper.DayAssignmentMapper;
import com.uptc.shiftmicroservice.mapper.ShiftMapper;
import com.uptc.shiftmicroservice.service.DayAssignmentService;
import com.uptc.shiftmicroservice.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    /**
     * Servicio para manejar la asignación de días en un CAF.
     */
    @Autowired
    DayAssignmentService dayAssignmentService;

    /**
     * Servicio para manejar las operaciones relacionadas con los turnos (Shift).
     */
    @Autowired
    ShiftService shiftService;

    /**
     * Endpoint para obtener todos los DayAssignments asociados a un CAF específico.
     *
     * @param fitnessCenterId El ID del Centro de Actividad Física (CAF).
     * @return ResponseEntity con una lista de DayAssignmentDTO si existen, o un código de estado 204 (sin contenido) si no se encuentran registros.
     */
    @GetMapping("/allDayAssignments/{caf}")
    public ResponseEntity<List<DayAssignmentDTO>> getAllDayAssigmentByCAF(@PathVariable("caf") int fitnessCenterId) {
        List<DayAssignmentDTO> dayAssignmentDTOList = dayAssignmentService.getDayAssignmentWithShiftByFitnessCenter(fitnessCenterId);

        // Si la lista está vacía, devuelve un código 204 sin contenido.
        if (dayAssignmentDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            // Si existen DayAssignments, los devuelve con un código 200 OK.
            return ResponseEntity.ok(dayAssignmentDTOList);
        }
    }

    /**
     * Endpoint para guardar un nuevo turno (Shift) para un DayAssignment específico.
     * Valida los horarios de inicio y fin, y crea el turno si los horarios son válidos.
     *
     * @param dayAssignmentDTO El objeto DayAssignmentDTO que contiene la información del turno.
     * @return ResponseEntity con el objeto ShiftDTO si el turno se crea correctamente, o un código de estado 409 si hay un conflicto de horarios.
     */
    @PostMapping("/saveShift")
    public ResponseEntity<?> saveShift(@RequestBody DayAssignmentDTO dayAssignmentDTO) {
        // Verificar si los horarios de inicio y fin son válidos.
        if (shiftService.verifyStartAndEndTime(dayAssignmentDTO.getShifts().get(0))) {
            // Validar o crear el DayAssignment.
            DayAssignment dayAssignment = dayAssignmentService.validarDayAssignment(dayAssignmentDTO);

            // Crear el turno si no hay conflictos de horarios.
            Optional<Shift> shiftCreated = shiftService.addShift(dayAssignment, ShiftMapper.INSTANCE.shiftDTOToShift(dayAssignmentDTO.getShifts().get(0)));
            if (shiftCreated.isPresent()) {
                // Convertir el turno a un DTO y devolverlo.
                ShiftDTO shiftDTO = ShiftMapper.INSTANCE.shiftToShiftDTO(shiftCreated.get());
                System.out.println("INICIO: " + shiftDTO.getStartTime());
                System.out.println("FIN: " + shiftDTO.getEndTime());
                return ResponseEntity.ok(shiftDTO);
            } else {
                // Si no se pudo crear el turno debido a un conflicto de horarios, devolver un error 409.
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: No se pudo crear el turno debido a un conflicto entre los horarios.");
            }
        } else {
            // Si los horarios son incorrectos, devolver un error 422.
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Error: La hora de inicio y fin son incorrectas.");
        }
    }

    /**
     * Endpoint para editar un turno existente.
     * Verifica si el turno existe, y si es posible editarlo (sin conflictos de horarios).
     *
     * @param dayAssignmentDTO El objeto DayAssignmentDTO que contiene la información actualizada del turno.
     * @return ResponseEntity con el objeto ShiftDTO editado si la edición es exitosa, o un código de estado 404 si no se encuentra el turno o si hay solapamiento de horarios.
     */
    @PutMapping("/editShift")
    public ResponseEntity<ShiftDTO> editShift(@RequestBody DayAssignmentDTO dayAssignmentDTO) {
        Optional<Shift> shiftFind = shiftService.findShiftById(dayAssignmentDTO.getShifts().get(0));

        // Verificar si el turno existe.
        if (shiftFind.isPresent()) {
            Optional<Shift> editShift = shiftService.editShift(
                    DayAssignmentMapper.INSTANCE.mapDayAssignmentDTOToDayAssignment(dayAssignmentDTO),
                    ShiftMapper.INSTANCE.shiftDTOToShift(dayAssignmentDTO.getShifts().get(0))
            );

            // Si el turno se edita correctamente, devolver el nuevo ShiftDTO.
            if (editShift.isPresent()) {
                return ResponseEntity.ok(ShiftMapper.INSTANCE.shiftToShiftDTO(editShift.get()));
            } else {
                // Si hay un solapamiento en los horarios, devolver un error 404.
                System.out.println("Hay solapamiento en el turno nuevo");
                return ResponseEntity.notFound().build();
            }
        }

        // Si no se encuentra el turno, devolver un error 404.
        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint para eliminar un turno específico de un DayAssignment.
     *
     * @param dayAssignmentDTO El objeto DayAssignmentDTO que contiene la información del turno a eliminar.
     * @return ResponseEntity con el ShiftDTO eliminado si la eliminación es exitosa, o un código de estado 404 si no se encuentra el turno.
     */
    @PutMapping("/deleteShift")
    public ResponseEntity<ShiftDTO> deleteShift(@RequestBody DayAssignmentDTO dayAssignmentDTO) {
        // Intentar eliminar el turno.
        if (shiftService.deleteShift(dayAssignmentDTO.getId(), dayAssignmentDTO.getShifts().get(0))) {
            return ResponseEntity.ok(dayAssignmentDTO.getShifts().get(0));
        }

        // Si no se encuentra el turno, devolver un error 404.
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/shiftsReportAttended")
    public ResponseEntity<List<ShiftsReportDTO>> getShiftReportAttendedByCAF(@RequestBody ConsultShiftReportDTO consultShiftReportDTO) {
        System.out.println("LLEGO"+ consultShiftReportDTO.getStartDate() + " " + consultShiftReportDTO.getEndDate());
        List<ShiftsReportDTO> shiftsReportDTOS = shiftService.shiftsReportAttendedByCAF(consultShiftReportDTO);

        // Si la lista está vacía, devuelve un código 204 sin contenido.
        if (shiftsReportDTOS.isEmpty()) {
            System.out.println("PROBLEMA VACIO");
            return ResponseEntity.noContent().build();
        } else {
            // Si existen DayAssignments, los devuelve con un código 200 OK.
            return ResponseEntity.ok(shiftsReportDTOS);
        }
    }
}
