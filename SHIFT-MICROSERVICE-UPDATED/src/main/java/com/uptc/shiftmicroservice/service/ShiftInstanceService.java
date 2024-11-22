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
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
/**
 * Servicio encargado de manejar las operaciones relacionadas con las instancias de turnos (ShiftInstance).
 * Este servicio interactúa con repositorios y otros servicios para gestionar turnos, asignaciones y reservas.
 */
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

    /**
     * Busca una instancia de turno (ShiftInstance) por su ID.
     *
     * @param idShiftInstance ID de la instancia de turno.
     * @return Optional con la instancia encontrada, o vacío si no existe.
     */
    public Optional<ShiftInstance> findShiftInstanceById(Long idShiftInstance) {
        return shiftInstanceRepository.findById(idShiftInstance);
    }

    /**
     * Crea y guarda una nueva instancia de turno para una fecha, turno y centro fitness específicos.
     *
     * @param actDate Fecha para la instancia.
     * @param shift Objeto Shift que define las propiedades del turno.
     * @param fitnessCenter ID del centro fitness asociado.
     * @return La instancia de turno creada.
     * @throws IllegalArgumentException Si faltan datos esenciales en el turno.
     */
    public ShiftInstance addShiftInstance(LocalDate actDate, Shift shift, int fitnessCenter) {
        ShiftInstance shiftInstance = new ShiftInstance();
        shiftInstance.setShift(shift);
        shiftInstance.setDate(actDate);
        shiftInstance.setState(true);
        shiftInstance.setPlaceAvailable(shift.getMaximumPlaceAvailable());
        return shiftInstanceRepository.save(shiftInstance);
    }

    /**
     * Crea todas las instancias de turno necesarias para hoy y mañana en un centro fitness específico.
     *
     * @param idFitnessCenter ID del centro fitness.
     * @return Lista de instancias de turnos creadas.
     */
    public List<ShiftInstance> createAllInstances(int idFitnessCenter) {
        LocalDate actualDay = LocalDate.now();
        LocalDate tomorrowDay = actualDay.plusDays(1);
        return createShiftInstances(tomorrowDay, idFitnessCenter);
    }

    /**
     * Crea instancias de turno para una fecha y centro fitness específicos.
     *
     * @param actDate Fecha para las instancias.
     * @param idFitnessCenter ID del centro fitness.
     * @return Lista de instancias de turnos creadas.
     */
    private List<ShiftInstance> createShiftInstances(LocalDate actDate, int idFitnessCenter) {
        List<ShiftInstance> shiftInstancesList = new ArrayList<>();
        String day = actDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        day = removeMarkDowns(day);

        if(day.equals("SABADO")){
            actDate = LocalDate.now().plusDays(3);
            day = "LUNES";
        }

        Optional<DayAssignment> dayAssignment = dayAssignmentService.getDayAssignmentByDayAndFinessCenter(Day.valueOf(day), idFitnessCenter);

        if (dayAssignment.isPresent()) {
            List<ShiftDTO> shifts = shiftService.getShiftsByDayAssignment(dayAssignment.get().getId());
            for (ShiftDTO shift : shifts) {
                shiftInstancesList.add(addShiftInstance(actDate, ShiftMapper.INSTANCE.shiftDTOToShift(shift), dayAssignment.get().getFitnessCenter()));
            }
        }
        return shiftInstancesList;
    }

    /**
     * Elimina los acentos y marcas diacríticas de un texto.
     *
     * @param texto Texto a normalizar.
     * @return Texto sin acentos ni marcas.
     */
    private static String removeMarkDowns(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    /**
     * Encuentra todas las instancias de turno disponibles para hoy y mañana en un centro fitness específico.
     *
     * @param idFitnessCenter ID del centro fitness.
     * @return Lista de instancias de turnos disponibles.
     */
    public List<ShiftInstance> findAllShiftInstancesByCaf(int idFitnessCenter) {
        LocalDate actualDay = LocalDate.now();
        List<ShiftInstance> todayInstances = findShiftInstancesByCaf(actualDay, idFitnessCenter);
        LocalDate tomorrowDay = actualDay.plusDays(1);
        todayInstances.addAll(findShiftInstancesByCaf(tomorrowDay, idFitnessCenter));
        return todayInstances;
    }

    /**
     * Busca las instancias de turno para una fecha y centro fitness específicos.
     *
     * @param actDate Fecha de las instancias.
     * @param idFitnessCenter ID del centro fitness.
     * @return Lista de instancias de turnos encontradas.
     */
    private List<ShiftInstance> findShiftInstancesByCaf(LocalDate actDate, int idFitnessCenter) {
        String day = actDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        day = removeMarkDowns(day);
        Optional<DayAssignment> dayAssignment = dayAssignmentService.getDayAssignmentByDayAndFinessCenter(Day.valueOf(day), idFitnessCenter);

        if(dayAssignment.isPresent()){
            List<ShiftDTO> shiftDTOList = shiftService.getShiftsByDayAssignment(dayAssignment.get().getId());
            List<ShiftInstance> shiftInstancesAvailable = new ArrayList<>();
            for (ShiftDTO shiftDTO : shiftDTOList) {
                List<ShiftInstance> shiftInstances = shiftInstanceRepository.findShiftInstanceByDateAndShiftId(actDate, shiftDTO.getId());
                if(!shiftInstances.isEmpty()){
                    ShiftInstance actShiftInstance = shiftInstances.get(0);
                    if(actShiftInstance.getPlaceAvailable() > 0){
                        shiftInstancesAvailable.add(actShiftInstance);
                    }
                }
            }
            return shiftInstancesAvailable;
        }
        return new ArrayList<>();
    }

    public ShiftInstance findShiftInstanceShiftId(int shiftId){
        List<ShiftInstance> actShiftInstance = shiftInstanceRepository.findShiftInstanceByDateAndShiftId(LocalDate.now(), shiftId);
        if(actShiftInstance.isEmpty()){
            return null;
        }
        return actShiftInstance.get(0);
    }

    /**
     * Verifica si una instancia de turno tiene lugares disponibles.
     *
     * @param actDate Fecha de la instancia.
     * @param shiftInstance Instancia de turno.
     * @return Número de lugares disponibles.
     */
    public int hasPlaceAvailable(LocalDate actDate, ShiftInstance shiftInstance) {
        return shiftInstance.getPlaceAvailable() - reservationRepository.countReservationsByShiftInstance(shiftInstance);
    }

    /**
     * Verifica si una instancia de turno está activa.
     *
     * @param idShiftInstance ID de la instancia de turno.
     * @return true si la instancia está activa, false en caso contrario.
     */
    public boolean isActiveShiftInstance(long idShiftInstance) {
        ShiftInstance actShiftInstance = findShiftInstanceById(idShiftInstance).orElseThrow();
        return LocalTime.now().isBefore(actShiftInstance.getShift().getEndTime()) && actShiftInstance.getState();
    }

    /**
     * Finaliza una instancia de turno cambiando su estado.
     *
     * @param idShiftInstance ID de la instancia de turno.
     * @return Optional con la instancia actualizada, o vacío si no existe.
     */
    public Optional<ShiftInstance> finishShift(long idShiftInstance) {
        Optional<ShiftInstance> actShiftInstance = findShiftInstanceById(idShiftInstance);
        actShiftInstance.ifPresent(instance -> {
            instance.setState(false);
            shiftInstanceRepository.save(instance);
        });
        return actShiftInstance;
    }

    /**
     * Método para verificar que si el turno actual es el último del día actual
     * @param shiftInstanceActual
     * @return
     */
    public boolean isLastShiftInstanceToday(ShiftInstance shiftInstanceActual) {
        return shiftInstanceRepository.countShiftInstanceByDateAndShiftId(shiftInstanceActual.getId(), shiftInstanceActual.getDate()) == 0;
    }

    /**
     *
     * Método para obtenr la instancia del turno actual en un CAF
     * @param fitnessCenter
     * @return  Optional con la instancia actual ShiftInstance, o vacío si no existe.
     */
    public Optional<ShiftInstance> obtainActShiftInstance(int fitnessCenter){
        List<ShiftInstance> shiftInstances = shiftInstanceRepository.findActiveShiftsByFitnessCenterAndCurrentTime(fitnessCenter, LocalTime.now(), LocalDate.now());
        if(!shiftInstances.isEmpty()){
            return Optional.of(shiftInstances.get(0));
        }
        return Optional.empty();
    }
}

