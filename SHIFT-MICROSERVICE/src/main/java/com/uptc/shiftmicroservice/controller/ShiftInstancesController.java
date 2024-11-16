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
@RequestMapping("/shift-instances")
public class ShiftInstancesController {

    @Autowired
    private ShiftInstanceService shiftInstanceService;

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/finishShift/{actShift}")
    public ResponseEntity<?> finishShiftByCAF(@PathVariable("actShift") long idActShift){
        Optional<ShiftInstance> actShiftInstance = shiftInstanceService.finishShift(idActShift);
        if(actShiftInstance.isPresent()){
            reservationService.registryAttendedAllReservationShift(actShiftInstance.get().getId());
            return ResponseEntity.ok().body("Creado");
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/actShift/{act}")
    public ResponseEntity<ShiftInstanceDTO> getActualShiftByCAF(@PathVariable("caf") int idFitnessCenter){
        Optional<ShiftInstance> actShift = shiftInstanceService.obtainActShiftInstance(idFitnessCenter);
        if(actShift.isPresent()){
            return ResponseEntity.ok(ShiftInstanceMapper.INSTANCE.shiftInstanceToShiftInstanceDTO(actShift.get()));
            //return ResponseEntity.ok(actShift.get());
        }else{
            return ResponseEntity.noContent().build();
        }
    }
}
