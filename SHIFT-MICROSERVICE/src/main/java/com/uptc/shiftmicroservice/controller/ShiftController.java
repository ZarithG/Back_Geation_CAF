package com.uptc.shiftmicroservice.controller;

import com.uptc.shiftmicroservice.dto.DayAssignmentDTO;
import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.errors.ConflictResponse;
import com.uptc.shiftmicroservice.mapper.DayAssignmentMapper;
import com.uptc.shiftmicroservice.mapper.ShiftMapper;
import com.uptc.shiftmicroservice.service.DayAssignmentService;
import com.uptc.shiftmicroservice.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    DayAssignmentService dayAssignmentService;

    @Autowired
    ShiftService shiftService;


    //Método para obtener todos los DayAssignment de un CAF
    @GetMapping("/allDayAssignments/{caf}")
    public ResponseEntity<List<DayAssignmentDTO>> getAllDayAssigmentByCAF(@PathVariable("caf") int fitnessCenterId){
        List<DayAssignmentDTO> dayAssignmentDTOList = dayAssignmentService.getDayAssignmentWithShiftByFitnessCenter(fitnessCenterId);
        if (dayAssignmentDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(dayAssignmentDTOList);
        }
    }

    @PostMapping("/saveShift")
    public ResponseEntity<?> saveShift(@RequestBody DayAssignmentDTO dayAssignmentDTO){
        if(shiftService.verifyStartAndEndTime(dayAssignmentDTO.getShifts().get(0))) {
            DayAssignment dayAssignment = dayAssignmentService.validarDayAssignment(dayAssignmentDTO);
            Optional<Shift> shiftCreated = shiftService.addShift(dayAssignment, ShiftMapper.INSTANCE.shiftDTOToShift(dayAssignmentDTO.getShifts().get(0)));
            if (shiftCreated.isPresent()) {
                ShiftDTO shiftDTO = ShiftMapper.INSTANCE.shiftToShiftDTO(shiftCreated.get());
                System.out.println("INICIO"+shiftDTO.getStartTime());
                System.out.println("FIN"+shiftDTO.getEndTime());
                return ResponseEntity.ok(shiftDTO);
            }else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: No se pudo crear el turno debido a un conflicto entre los horarios.");
            }
        }else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Error: La hora de inicio y fin son incorrectas.");
        }
    }

    @PutMapping("/editShift")
    public ResponseEntity<ShiftDTO> editShift(@RequestBody DayAssignmentDTO dayAssignmentDTO){
        Optional<Shift> shiftFind = shiftService.findShiftById(dayAssignmentDTO.getShifts().get(0));
        if(shiftFind.isPresent()) {
            Optional<Shift>  editShift = shiftService.editShift(DayAssignmentMapper.INSTANCE.mapDayAssignmentDTOToDayAssignment(dayAssignmentDTO),
                    ShiftMapper.INSTANCE.shiftDTOToShift(dayAssignmentDTO.getShifts().get(0)));
            if(editShift.isPresent()){
                return ResponseEntity.ok(ShiftMapper.INSTANCE.shiftToShiftDTO(editShift.get()));
            }else{
                System.out.println("Hay solapamiento en el turno nuevo");
                return ResponseEntity.notFound().build();
            }
        }
       return ResponseEntity.notFound().build();
    }

    @PutMapping("/deleteShift")
    public ResponseEntity<ShiftDTO> deleteShift(@RequestBody DayAssignmentDTO dayAssignmentDTO){
        if(shiftService.deleteShift(dayAssignmentDTO.getId(),dayAssignmentDTO.getShifts().get(0))){
            return ResponseEntity.ok(dayAssignmentDTO.getShifts().get(0));
        }
        return ResponseEntity.notFound().build();
    }
}
