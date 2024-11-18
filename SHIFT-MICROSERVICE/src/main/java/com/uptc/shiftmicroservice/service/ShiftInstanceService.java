package com.uptc.shiftmicroservice.service;

import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.Day;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import com.uptc.shiftmicroservice.mapper.ShiftMapper;
import com.uptc.shiftmicroservice.repository.ReservationRepository;
import com.uptc.shiftmicroservice.repository.ShiftInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ShiftInstanceService {

    private static final Logger log = LoggerFactory.getLogger(ShiftInstanceService.class);
    @Autowired
    private ShiftInstanceRepository shiftInstanceRepository;

    @Autowired
    private DayAssignmentService dayAssignmentService;

    @Autowired
    private ShiftService shiftService;
    @Autowired
    private ReservationRepository reservationRepository;


    public Optional<ShiftInstance> findShiftInstanceById(Long idShiftInstance){
        return shiftInstanceRepository.findById(idShiftInstance);
    }

    public ShiftInstance addShiftInstance(LocalDate actDate, Shift shift, int fitnessCenter) {

        ShiftInstance shiftInstance = new ShiftInstance();

        // Establecer las propiedades desde el objeto Shift
        shiftInstance.setStartTime(shift.getStartTime());
        shiftInstance.setEndTime(shift.getEndTime());
        shiftInstance.setDate(actDate);
        shiftInstance.setState(true);
        shiftInstance.setFitnessCenter(fitnessCenter);

        // Asegurar que las propiedades esenciales no sean nulas
        if (shift.getPlaceAvailable() == 0) {
            throw new IllegalArgumentException("El lugar disponible no puede ser 0.");
        }
        shiftInstance.setPlaceAvailable(shift.getPlaceAvailable());

        if (shift.getDayAssignment() == null) {
            throw new IllegalArgumentException("El DayAssignment asociado no puede ser nulo.");
        }
        shiftInstance.setDayAssignment(shift.getDayAssignment());

        // Establecer el estado por defecto
        shiftInstance.setState(true);

        // Imprimir las propiedades para depuración
        System.out.println("Creando instancia de ShiftInstance con:");
        System.out.println("Hora de inicio: " + shiftInstance.getStartTime());
        System.out.println("Hora de fin: " + shiftInstance.getEndTime());
        System.out.println("Fecha: " + shiftInstance.getDate());
        System.out.println("Lugar disponible: " + shiftInstance.getPlaceAvailable());
        System.out.println("DayAssignment: " + shiftInstance.getDayAssignment());
        System.out.println("Estado: " + shiftInstance.getState());

        // Guardar y retornar la instancia
        return shiftInstanceRepository.save(shiftInstance);
    }

    public List<ShiftInstance> createAllInstances(int idFitnessCenter){
        LocalDate actualDay = LocalDate.now();
        System.out.println("FECHA ACTUAL " + actualDay);

        // Crear las instancias para el día actual
        List<ShiftInstance> firstDayShiftInstances = createShiftInstances(actualDay, idFitnessCenter);
        if (firstDayShiftInstances == null) {
            firstDayShiftInstances = new ArrayList<>();
        }

        // Crear las instancias para el día siguiente
        LocalDate tomorrowDay = actualDay.plusDays(1);
        List<ShiftInstance> secondDayShiftInstances = createShiftInstances(tomorrowDay, idFitnessCenter);
        if (secondDayShiftInstances == null) {
            secondDayShiftInstances = new ArrayList<>();
        }

        // Combinar ambas listas
        firstDayShiftInstances.addAll(secondDayShiftInstances);

        return firstDayShiftInstances;
    }

    private List<ShiftInstance> createShiftInstances(LocalDate actDate, int idFitnessCenter) {
        List<ShiftInstance> shiftInstancesList = new ArrayList<>();
        String day = actDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        day = removeMarkDowns(day.toUpperCase());
        System.out.println("CREAR INSTANCIA: DAY "+day);
        Optional<DayAssignment> dayAssignment = dayAssignmentService.getDayAssignmentByDayAndFinessCenter(Day.valueOf(day), idFitnessCenter);

        if (dayAssignment.isPresent()) {
            System.out.println("ENTRÓ A INSTANCIA SIN CREAR PARA ESE DÍA");
            Optional<List<ShiftInstance>> lastShiftInstance = shiftInstanceRepository.findTopByStateTrueOrderByDateDesc();
            System.out.println("LAST "+lastShiftInstance.isPresent());
            if(lastShiftInstance.isPresent()){
                List<ShiftDTO> shifts = shiftService.getShiftsByDayAssignment(dayAssignment.get().getId());
                for (ShiftDTO shift : shifts) {
                    System.out.println("VERIFICANDO SHIFTS");
                    if(shift.getStartTime().isAfter(LocalDateTime.now().toLocalTime())){
                        shiftInstancesList.add(addShiftInstance(actDate, ShiftMapper.INSTANCE.shiftDTOToShift(shift),dayAssignment.get().getFitnessCenter()));
                    }
                }
//                for(ShiftInstance instance : lastShiftInstance.get()){
//                    if(instance == null){
//
//                    }
//                }
            }
        }
        return shiftInstancesList != null ? shiftInstancesList : new ArrayList<>();
    }

    private static String removeMarkDowns(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}","");
    }

    public List<ShiftInstance> findAllShiftInstancesByCaf(int idFitnessCenter) {
        LocalDate actualDay = LocalDate.now();
        List<ShiftInstance> shiftInstanceListToday =  findShiftInstancesByCaf(actualDay,idFitnessCenter);
        LocalDate tomorrowDay = LocalDate.now().plusDays(1);
        List<ShiftInstance> shiftInstanceListTumorrow =  findShiftInstancesByCaf(tomorrowDay,idFitnessCenter);
        shiftInstanceListToday.addAll(shiftInstanceListTumorrow);
        return shiftInstanceListToday;
    }

    private List<ShiftInstance> findShiftInstancesByCaf(LocalDate actDate, int idFitnessCenter) {
        List<ShiftInstance> shiftInstanceList = shiftInstanceRepository.findShiftInstancesByFitnessCenterAndDate(idFitnessCenter,actDate);
        List<ShiftInstance> shiftInstancesAvailable = new ArrayList<>();

        for(ShiftInstance shift: shiftInstanceList){

            if(hasPlaceAvailable(actDate, shift) > 0 && isActiveShiftInstance(shift.getId())){
                shiftInstancesAvailable.add(shift);
            }
        }
        return shiftInstancesAvailable;
    }

    public int hasPlaceAvailable(LocalDate actDate, ShiftInstance shiftInstance) {
        return shiftInstance.getPlaceAvailable() - reservationRepository.countReservationsByShiftInstance(shiftInstance);
    }

    public boolean isActiveShiftInstance(long idShiftInstance){
        ShiftInstance actShiftInstance = findShiftInstanceById(idShiftInstance).get();
        return LocalTime.now().isBefore(actShiftInstance.getEndTime()) && actShiftInstance.getState();
    }

    public Optional<ShiftInstance> finishShift(long idShiftInstance){
        Optional<ShiftInstance> actShiftInstance = findShiftInstanceById(idShiftInstance);
        if(actShiftInstance.isPresent()){
            actShiftInstance.get().setState(false);
            return Optional.of(shiftInstanceRepository.save(actShiftInstance.get()));
        }else {
            return Optional.empty();
        }
    }

    public Optional<ShiftInstance> obtainActShiftInstance(int fitnessCenter){
        List<ShiftInstance> shiftInstances = shiftInstanceRepository.findActiveShiftsByFitnessCenterAndCurrentTime(fitnessCenter);
        if(shiftInstances.size() > 0){
            return Optional.of(shiftInstances.get(0));
        }
        return Optional.empty();

    }
}
