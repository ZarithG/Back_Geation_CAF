package com.uptc.shiftmicroservice.controller;

import com.uptc.shiftmicroservice.dto.ReservationDTO;
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
    @Autowired
    DayAssignmentService dayAssignmentService;

    @Autowired
    ShiftInstanceService shiftInstanceService;

    @Autowired
    ShiftService shiftService;

    @Autowired
    ReservationService reservationService;

    @PostMapping("/reserve-shift-user")
    public ResponseEntity<Reservation> reserveShiftForUser(@RequestBody ReservationDTO reservationDTO){
        Optional<Reservation> saveReservetion = reservationService.reserveShiftForUser(reservationDTO);
        return  saveReservetion.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(saveReservetion.get()) ;
    }

    //Metodo que se ejecuta para indicar que se creen las instancias de SHiftInstance de un día específico
    @PostMapping("/create-shift-instances/{idFitnessCenter}")
    public ResponseEntity<List<ShiftInstance> > createAllInstances(@PathVariable("idFitnessCenter") int idFitnessCenter ){
        List<ShiftInstance> shiftsInstances = shiftInstanceService.createAllInstances( idFitnessCenter);


        return ResponseEntity.ok(shiftsInstances);
    }

    //Metodo para listar las instancias de turnso disponibles para cda CAF,
    // Listar los turnos disponibles visibles para un usuario
    @GetMapping("/shift-instances-caf/{idCaf}")
    public ResponseEntity<List<ShiftInstance>> allShiftInstancesByCaf(@PathVariable("idCaf") int idCaf ){
        List<ShiftInstance> shiftInstancesAvailable = shiftInstanceService.findAllShiftInstancesByCaf(idCaf);
        return ResponseEntity.ok(shiftInstancesAvailable);
    }

    //Metodo para registrar asistencia
    @PostMapping("/registry-attended-reserve")
    public ResponseEntity<?> registryReservationUser(@RequestBody ReservationDTO reservationDTO){
        Optional<Reservation> reservationRegistry;
        if(shiftInstanceService.isActiveShiftInstance(reservationDTO.getIdShiftInstance())){
            reservationRegistry = reservationService.registryReservation(reservationDTO.getId());
            return ResponseEntity.ok(reservationRegistry);
        }
        return ResponseEntity.noContent().build();
    }

    //Método para listar las reservaciones agendadas de un usuario
    @GetMapping("/allReservationForUser/{userid}")
    public ResponseEntity<List<Reservation>> allReservationForUser(@PathVariable("userid") int userId){
        List<Reservation> reservations = reservationService.getAllReservationForUser(userId);
        if(reservations.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/cancelReservationForUser/{shiftId}")
    public ResponseEntity<ReservationDTO> cancelReservationForUser(@PathVariable("shiftId") long shiftId){
        Reservation reservationDeleted = reservationService.deleteReservation(shiftId);
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservationDeleted.getId());
        if(reservationDeleted == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservationDTO);
    }

}
