package com.uptc.shiftmicroservice.service;

import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.Day;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import com.uptc.shiftmicroservice.mapper.ShiftMapper;
import com.uptc.shiftmicroservice.repository.ShiftInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ShiftInstanceService {

    @Autowired
    private ShiftInstanceRepository shiftInstanceRepository;

    @Autowired
    private DayAssignmentService dayAssignmentService;

    @Autowired
    private ShiftService shiftService;

    public Optional<ShiftInstance> findShiftInstanceById(Long idShiftInstance){
        return shiftInstanceRepository.findById(idShiftInstance);
    }

    public ShiftInstance addShiftInstance(LocalDate actDate, Shift shift) {
        ShiftInstance shiftInstance = new ShiftInstance();
        shiftInstance.setStartTime(shift.getStartTime());
        shiftInstance.setEndTime(shift.getEndTime());
        shiftInstance.setDate(actDate);
        shiftInstance.setPlaceAvailable(shift.getPlaceAvailable());

        return shiftInstanceRepository.save(shiftInstance);
    }

    public List<ShiftInstance> createAllInstances(int idFitnessCenter){
        LocalDate actualDay = LocalDate.now();
        List<ShiftInstance> firstDayShiftInstances = createShiftInstances(actualDay, idFitnessCenter);

        LocalDate tomorrowDay = LocalDate.now().plusDays(1);
        List<ShiftInstance> secondDayShiftInstances = createShiftInstances(tomorrowDay, idFitnessCenter);

        return firstDayShiftInstances;
    }

    private List<ShiftInstance> createShiftInstances(LocalDate actDate, int idFitnessCenter) {
        List<ShiftInstance> shiftInstancesList = new ArrayList<>();
        String day = actDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        day = removeMarkDowns(day.toUpperCase());
        Optional<DayAssignment> dayAssignment = dayAssignmentService.getDayAssignmentByDayAndFinessCenter(Day.valueOf(day), idFitnessCenter);

        if (dayAssignment.isPresent()) {
            Optional<ShiftInstance> lastShiftInstance = shiftInstanceRepository.findTopByStateTrueOrderByDateDesc();
            if(lastShiftInstance.isEmpty()){
                List<ShiftDTO> shifts = shiftService.getShiftsByDayAssignment(dayAssignment.get().getId());

                for (ShiftDTO shift : shifts) {
                    if(shift.getStartTime().isBefore(LocalDateTime.now().toLocalTime())){
                        shiftInstancesList.add(addShiftInstance(actDate, ShiftMapper.INSTANCE.shiftDTOToShift(shift)));
                    }
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
        return shiftInstancesList;
    }

    private static String removeMarkDowns(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}","");
    }
}
