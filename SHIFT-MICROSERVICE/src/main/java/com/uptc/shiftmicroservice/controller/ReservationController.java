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
@RequestMapping("/reserve")
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

        //List<ShiftDTO> shiftDTOS = shiftsInstances.stream().map(ShiftMapper.INSTANCE::shift).collect(Collectors.toList());
        return ResponseEntity.ok(shiftsInstances);
    }

    @GetMapping("/shift-instances-caf/{idCaf}")
    public ResponseEntity<List<ShiftInstance>> allShiftInstancesByCaf(@PathVariable("idCaf") int idCaf ){
        List<ShiftInstance> shiftInstancesAvailable = shiftInstanceService.findAllShiftInstancesByCaf(idCaf);
        return ResponseEntity.ok(shiftInstancesAvailable);
    }

}
