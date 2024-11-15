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
public class DayAssignmentService {
    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    DayAssignmentRepository dayAssignmentRepository;

    //Método para obtener los DayAssigment de un CAF específico
    public List<DayAssignmentDTO> getDayAssignmentWithShiftByFitnessCenter(int finessCenterId){
        List<DayAssignment> dayAssignments = dayAssignmentRepository.findByFitnessCenter(finessCenterId);

        return dayAssignments.stream().map(dayAssignment -> {
          //Convertir a DayAssignmentDTO
          DayAssignmentDTO dayAssignmentDto = DayAssignmentMapper.INSTANCE.mapDayAssignmentToDayAssignmentDTO(dayAssignment);

          //Consultar los Shift asociados a el DayAssignment
          List<Shift> shifts = shiftRepository.findByDayAssignment(dayAssignment);
          List<ShiftDTO> shiftDTOS = shifts.stream().map(ShiftMapper.INSTANCE::shiftToShiftDTO).collect(Collectors.toList());
          //Setear el valor de los shift asociados
          dayAssignmentDto.setShifts(shiftDTOS);
          return dayAssignmentDto;
        }).collect(Collectors.toList());
    }
    public DayAssignment validarDayAssignment(DayAssignmentDTO dayAssignmentDTO) {
        Optional<DayAssignment> dayAssignmentFind = this.getDayAssignmentByDayAndFinessCenter(dayAssignmentDTO.getDay(), dayAssignmentDTO.getFitnessCenter());
        DayAssignment dayAssignment;
        //Verificar  si existe el DayAssignment
        if (dayAssignmentFind.isEmpty()) {
            System.out.println("Crear nuevo Day");
            dayAssignment = this.saveDayAssignment(dayAssignmentDTO);
        } else {
            dayAssignment = dayAssignmentFind.get();
            System.out.println("Existe Day: "+ dayAssignment.getId());
        }
        return dayAssignment;
    }

    public DayAssignment saveDayAssignment(DayAssignmentDTO dayAssignmentDTO){
        DayAssignment day =  DayAssignmentMapper.INSTANCE.mapDayAssignmentDTOToDayAssignment(dayAssignmentDTO);
        return dayAssignmentRepository.save(day);
    }

    public Optional<DayAssignment> getDayAssignmentByDayAndFinessCenter(Day day, int fitnessCenterId) {
        return dayAssignmentRepository.findDayAssignmentByDayAndFitnessCenter(day, fitnessCenterId);
    }



    public DayAssignmentDTO mapDayAssignmenToDTOWithShifts(DayAssignment dayAssignment, List<Shift> shifts) {
        DayAssignmentDTO dto = DayAssignmentMapper.INSTANCE.mapDayAssignmentToDayAssignmentDTO(dayAssignment);
        List<ShiftDTO> shiftDTOs = shifts.stream()
                .map(ShiftMapper.INSTANCE::shiftToShiftDTO)
                .collect(Collectors.toList());
        dto.setShifts(shiftDTOs);
        return dto;
    }

}
