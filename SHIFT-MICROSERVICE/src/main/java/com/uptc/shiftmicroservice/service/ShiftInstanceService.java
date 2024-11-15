package com.uptc.shiftmicroservice.service;

import com.netflix.discovery.converters.Auto;
import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.Day;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import com.uptc.shiftmicroservice.mapper.ShiftMapper;
import com.uptc.shiftmicroservice.repository.ShiftInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftInstanceService {

    @Autowired
    private ShiftInstanceRepository shiftInstanceRepository;

    @Autowired
    private DayAssignmentService dayAssignmentService;

    @Autowired
    private ShiftService shiftService;

    //Buscar un ShiftInstance
    public Optional<ShiftInstance> findShiftInstanceById(Long idShiftInstance){
        return shiftInstanceRepository.findById(idShiftInstance);
    }

    public ShiftInstance addShiftInstance(Shift shift, LocalDate date) {
        ShiftInstance shiftInstance = new ShiftInstance();
        shiftInstance.setStartTime(shift.getStartTime());
        shiftInstance.setEndTime(shift.getEndTime());
        shiftInstance.setDate(date);
        shiftInstance.setPlaceAvailable(shift.getPlaceAvailable());

        return shiftInstanceRepository.save(shiftInstance);
    }

    public void createShiftInstances(Day day, int idFitnessCenter, LocalDate date) {
        //por cada caf registrado recorrer los dayAssigment
        List<ShiftInstance> shiftInstancesCreate = new ArrayList<>();

        Optional<DayAssignment> dayAssignment = dayAssignmentService.getDayAssignmentByDayAndFinessCenter(day, idFitnessCenter);
        //Obtener los shift de un DayAssignment para crear los ShiftInstance
        //SOLO DEBE SER HOY Y MAÑANA
        if (dayAssignment.isPresent()) {
            List<ShiftDTO> shifts = shiftService.getShiftsByDayAssignment(dayAssignment.get().getId());
            //Por cada shift crear una ShiftInstance
            for (ShiftDTO shift : shifts) {
                shiftInstancesCreate.add(addShiftInstance(ShiftMapper.INSTANCE.shiftDTOToShift(shift), date));
            }
        }
    }
}
