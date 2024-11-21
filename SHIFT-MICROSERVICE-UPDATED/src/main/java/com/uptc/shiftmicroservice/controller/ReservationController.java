package com.uptc.shiftmicroservice.controller;

import com.uptc.shiftmicroservice.dto.ReservationDTO;
import com.uptc.shiftmicroservice.dto.ShiftReservationDTO;
import com.uptc.shiftmicroservice.entity.*;
import com.uptc.shiftmicroservice.service.DayAssignmentService;
import com.uptc.shiftmicroservice.service.ReservationService;
import com.uptc.shiftmicroservice.service.ShiftInstanceService;
import com.uptc.shiftmicroservice.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shift/reserve")
public class ReservationController {

    /**
     * Servicio para manejar las asignaciones de días (DayAssignment) en un centro de actividad física.
     */
    @Autowired
    DayAssignmentService dayAssignmentService;

    /**
     * Servicio para manejar las instancias de turno (ShiftInstance).
     */
    @Autowired
    ShiftInstanceService shiftInstanceService;

    /**
     * Servicio para manejar los turnos (Shift).
     */
    @Autowired
    ShiftService shiftService;

    /**
     * Servicio para manejar las reservas (Reservation).
     */
    @Autowired
    ReservationService reservationService;

    /**
     * Endpoint para reservar un turno para un usuario.
     * Se recibe un DTO de reserva, y si se puede realizar la reserva, se devuelve el objeto `Reservation` creado.
     *
     * @param reservationDTO El objeto DTO que contiene los datos de la reserva.
     * @return ResponseEntity con el objeto de la reserva si se creó exitosamente, o un código de estado 204 (sin contenido) si no se pudo reservar.
     */
    @PostMapping("/reserve-shift-user")
    public ResponseEntity<Reservation> reserveShiftForUser(@RequestBody ReservationDTO reservationDTO) {
        // Llamar al servicio para realizar la reserva del turno.
        Optional<Reservation> saveReservation = reservationService.reserveShiftForUser(reservationDTO);

        // Si la reserva fue exitosa, devolverla; de lo contrario, devolver 204 No Content.
        return saveReservation.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(saveReservation.get());
    }

    /**
     * Endpoint para crear todas las instancias de turno para un día específico en un centro de actividad física (CAF).
     * Se genera una lista de instancias de turno para un CAF.
     *
     * @param idFitnessCenter El ID del centro de actividad física (CAF).
     * @return ResponseEntity con la lista de las instancias de turno creadas.
     */
    @PostMapping("/create-shift-instances/{idFitnessCenter}")
    public ResponseEntity<List<ShiftInstance>> createAllInstances(@PathVariable("idFitnessCenter") int idFitnessCenter) {
        // Crear las instancias de turno para el CAF especificado.
        List<ShiftInstance> shiftInstances = shiftInstanceService.createAllInstances(idFitnessCenter);

        // Devolver la lista de instancias creadas.
        return ResponseEntity.ok(shiftInstances);
    }

    /**
     * Endpoint para obtener todas las instancias de turno disponibles para un CAF específico.
     * Este método devuelve los turnos disponibles visibles para un usuario.
     *
     * @param idCaf El ID del CAF.
     * @return ResponseEntity con la lista de instancias de turno disponibles.
     */
    @GetMapping("/shift-instances-caf/{idCaf}")
    public ResponseEntity<List<ShiftInstance>> allShiftInstancesByCaf(@PathVariable("idCaf") int idCaf) {
        // Obtener todas las instancias de turno disponibles para el CAF especificado.
        List<ShiftInstance> shiftInstancesAvailable = shiftInstanceService.findAllShiftInstancesByCaf(idCaf);
        if(shiftInstancesAvailable.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Devolver las instancias de turno disponibles.
        return ResponseEntity.ok(shiftInstancesAvailable);
    }

    /**
     * Endpoint para obtener todas las reservas asociadas a una instancia de turno específica.
     *
     * @param shiftId El ID de la instancia de turno.
     * @return ResponseEntity con la lista de reservas asociadas a la instancia de turno.
     */
    @GetMapping("/all-reservations-by-shift-instance/{shiftId}")
    public ResponseEntity<List<ShiftReservationDTO>> getAllReservationForActualShift(@PathVariable("shiftId") long shiftId) {
        // Obtener todas las reservas para la instancia de turno especificada.
        List<ShiftReservationDTO> reservations = reservationService.getAllReservationsByActualShiftInstanceId(shiftId);

        // Si no hay reservas, devolver 204 No Content.
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Devolver la lista de reservas.
        return ResponseEntity.ok(reservations);
    }

    /**
     * Endpoint para registrar la asistencia de un usuario a una reserva de turno.
     *
     * @param reservationDTO El DTO con los datos de la reserva para registrar la asistencia.
     * @return ResponseEntity con la reserva registrada si la instancia de turno está activa, o un código de estado 204 (sin contenido) si la instancia no está activa.
     */
    @PostMapping("/registry-attended-reserve")
    public ResponseEntity<Reservation> registryReservationUser(@RequestBody ReservationDTO reservationDTO) {
        Optional<Reservation> reservationRegistry;

        // Verificar si la instancia de turno está activa.
        if (shiftInstanceService.isActiveShiftInstance(reservationDTO.getIdShiftInstance())) {
            // Registrar la asistencia de la reserva.
            reservationRegistry = reservationService.registryReservation(reservationDTO.getId());
            return ResponseEntity.ok(reservationRegistry.get());
        }

        // Si la instancia no está activa, devolver 204 No Content.
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para obtener todas las reservas agendadas de un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return ResponseEntity con la lista de reservas del usuario.
     */
    @GetMapping("/allReservationForUser/{userid}")
    public ResponseEntity<List<Reservation>> allReservationForUser(@PathVariable("userid") int userId) {
        // Obtener todas las reservas del usuario especificado.
        List<Reservation> reservations = reservationService.getAllReservationForUser(userId);

        // Si no hay reservas, devolver 204 No Content.
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Devolver la lista de reservas.
        return ResponseEntity.ok(reservations);
    }

    /**
     * Endpoint para cancelar una reservación de un turno por un usuario.
     * @param reservationDTO El DTO con los datos de la reserva que se desea cancelar.
     * @return ResponseEntity con la reserva cancelada si la instancia de turno está activa, o un código de estado 204 (sin contenido) si no se pudo cancelar correctamente este
     */
    @PostMapping("/cancelReservationForUser")
    public ResponseEntity<ReservationDTO> cancelReservationForUser(@RequestBody ReservationDTO reservationDTO) {
        // Eliminar la reserva para el turno especificado.
        Optional<Reservation> reservationDeleted = reservationService.deleteReservation(reservationDTO);

        // Si la reserva se eliminó correctamente, devolver el DTO de la reserva.
        if (reservationDeleted.isPresent()) {
            return ResponseEntity.ok(reservationDTO);
        }

        // Si no se encontró la reserva para eliminar, devolver 204 No Content.
        return ResponseEntity.noContent().build();
    }
}

