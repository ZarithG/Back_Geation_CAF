package com.uptc.shiftmicroservice.controller;

import com.uptc.shiftmicroservice.dto.ShiftInstanceDTO;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import com.uptc.shiftmicroservice.mapper.ShiftInstanceMapper;
import com.uptc.shiftmicroservice.service.ReservationService;
import com.uptc.shiftmicroservice.service.ShiftInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/shift/shift-instances")
public class ShiftInstancesController {

    /**
     * Servicio para manejar las operaciones relacionadas con las instancias de turno (ShiftInstance).
     */
    @Autowired
    private ShiftInstanceService shiftInstanceService;

    /**
     * Servicio para manejar las reservas asociadas a los turnos.
     */
    @Autowired
    private ReservationService reservationService;

    /**
     * Endpoint para finalizar un turno activo de un CAF específico.
     * Al finalizar el turno, se registran todas las reservas atendidas asociadas al turno.
     *
     * @param idActShift El ID de la instancia de turno que se desea finalizar.
     * @return ResponseEntity con un mensaje de éxito si el turno se finaliza correctamente,
     * o un código de estado 204 (sin contenido) si no se encuentra la instancia de turno.
     */
    @PostMapping("/finishShift/{actShift}")
    public ResponseEntity<?> finishShiftByCAF(@PathVariable("actShift") long idActShift) {
        // Intentar finalizar el turno activo con el ID proporcionado.
        Optional<ShiftInstance> actShiftInstance = shiftInstanceService.finishShift(idActShift);

        // Si se ha encontrado y finalizado el turno, se registran todas las reservas atendidas.
        if (actShiftInstance.isPresent()) {
            reservationService.registryAttendedAllReservationShift(actShiftInstance.get().getId());
            if(shiftInstanceService.isLastShiftInstanceToday(actShiftInstance.get())){ //Verifica si es el último turno del día
                //Crea las instancias del turno de mañana
                shiftInstanceService.createAllInstances(actShiftInstance.get().getShift().getDayAssignment().getFitnessCenter());
            }
            return ResponseEntity.ok().body("Creado");
        } else {
            // Si no se encuentra el turno, devolver un código 204 (sin contenido).
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Endpoint para obtener la instancia del turno actual que se está atendiendo en un CAF específico.
     *
     * @param idFitnessCenter El ID del Centro de Actividad Física (CAF).
     * @return ResponseEntity con el objeto ShiftInstanceDTO si hay un turno activo,
     * o un código de estado 204 (sin contenido) si no hay turno activo en ese momento.
     */
    @PostMapping("/actShift/{idCaf}")
    public ResponseEntity<ShiftInstanceDTO> getActualShiftByCAF(@PathVariable("idCaf") int idFitnessCenter) {
        // Obtener la instancia del turno actual.
        Optional<ShiftInstance> actShift = shiftInstanceService.obtainActShiftInstance(idFitnessCenter);

        // Si se encuentra un turno activo, devolver su DTO.
        if (actShift.isPresent()) {
            return ResponseEntity.ok(ShiftInstanceMapper.INSTANCE.shiftInstanceToShiftInstanceDTO(actShift.get()));
        } else {
            // Si no se encuentra un turno activo, devolver un código 204 (sin contenido).
            return ResponseEntity.noContent().build();
        }
    }
}
