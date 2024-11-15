package com.uptc.shiftmicroservice.controller;

import com.uptc.shiftmicroservice.dto.DayAssignmentDTO;
import com.uptc.shiftmicroservice.dto.ReservationDTO;
import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.Day;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import com.uptc.shiftmicroservice.mapper.ShiftMapper;
import com.uptc.shiftmicroservice.service.DayAssignmentService;
import com.uptc.shiftmicroservice.service.ReservationService;
import com.uptc.shiftmicroservice.service.ShiftInstanceService;
import com.uptc.shiftmicroservice.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reserves")
public class ReservationController {
    @Autowired
    DayAssignmentService dayAssignmentService;

    @Autowired
    ShiftInstanceService shiftInstanceService;

    @Autowired
    ShiftService shiftService;

    @Autowired
    ReservationService reservationService;

    @GetMapping("/reserve-shift")
    public ResponseEntity<ReservationDTO> reserveShiftForUser(@RequestBody ReservationDTO reservationDTO){
        Optional<ShiftInstance> shiftInstance = shiftInstanceService.findShiftInstanceById(reservationDTO.getIdShiftInstance());
        //Cupos ocupados
        int totalReserves = reservationService.countReversesToShiftIntance(shiftInstance.get());
        //Verificar los cupos disponibles
        if((shiftInstance.get().getPlaceAvailable() - totalReserves) > 0){
            //Crear reservación
            //Validar que la fecha actual de la reserva sea menor a la fecha de fin del turno
            LocalDateTime endDateTime = LocalDateTime.of(shiftInstance.get().getDate(), shiftInstance.get().getEndTime());
            if(reservationService.isReserveDateLowEndTimeShiftInstance(reservationDTO.getDateReservation(),endDateTime ) > 0){

                //Validar reservaciones en el mismo día

            }else{
                System.out.println("Ya no puede agendar, el turno terminó");
            }
        }else{

            System.out.println("Turnos llenos");
        }
        return ResponseEntity.ok(reservationDTO);
    }

    // Day day = Day.valueOf("LUNES");
    //Metodo que se ejecuta para indicar que se creen las instancias de SHiftInstance de un día específico
    public ResponseEntity<?> createShiftInstance(Day day, int idFitnessCenter, LocalDate date){

        return ResponseEntity.ok("");
    }
}
