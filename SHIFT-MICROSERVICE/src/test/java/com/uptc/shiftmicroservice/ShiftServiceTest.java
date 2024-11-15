package com.uptc.shiftmicroservice;
import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import com.uptc.shiftmicroservice.entity.Shift;
import com.uptc.shiftmicroservice.repository.ShiftRepository;
import com.uptc.shiftmicroservice.service.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShiftServiceTest {
    @InjectMocks
    private ShiftService shiftService;

    @Mock
    private ShiftRepository shiftRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addShift_NewShiftWithoutConflicts_ReturnsSavedShift() {
        // Configuración del DayAssignment y del Shift
        DayAssignment dayAssignment = new DayAssignment();
        dayAssignment.setId(1);

        Shift newShift = new Shift();
        newShift.setStartTime(LocalTime.of(9, 0));
        newShift.setEndTime(LocalTime.of(11, 0));

        // Simulación del repositorio
        when(shiftRepository.save(any(Shift.class))).thenReturn(newShift);

        // Llamada al método a probar
        Optional<Shift> result = shiftService.addShift(dayAssignment, newShift);

        // Verificación del resultado
        assertTrue(result.isPresent());
        assertEquals(newShift, result.get());
        verify(shiftRepository, times(1)).save(newShift);
    }

    @Test
    void addShift_ShiftOverlapsWithExisting_ReturnsEmptyOptional() {
        // Configuración del DayAssignment y de los turnos existentes
        DayAssignment dayAssignment = new DayAssignment();
        dayAssignment.setId(1);

        Shift existingShift = new Shift();
        existingShift.setStartTime(LocalTime.of(10, 0));
        existingShift.setEndTime(LocalTime.of(12, 0));

        TreeSet<Shift> shifts = new TreeSet<>();
        shifts.add(existingShift);

        Shift newShift = new Shift();
        newShift.setStartTime(LocalTime.of(11, 0));
        newShift.setEndTime(LocalTime.of(13, 0));

        // Configura la lista de turnos y las condiciones de prueba
        when(shiftService.getOrderedShiftsByDayAssignment(dayAssignment.getId())).thenReturn(shifts);

        // Llamada al método a probar
        Optional<Shift> result = shiftService.addShift(dayAssignment, newShift);

        // Verificación del resultado
        assertTrue(result.isEmpty());
        verify(shiftRepository, never()).save(newShift);
    }
    //Revisar
    @Test
    void addShift_ShiftOverlapsWithExisting_Previuos_Next_ReturnsEmptyOptional() {
        // Configuración del DayAssignment y de los turnos existentes
        DayAssignment dayAssignment = new DayAssignment();
        dayAssignment.setId(1);

        Shift existingShift = new Shift();
        existingShift.setStartTime(LocalTime.of(7, 30));
        existingShift.setEndTime(LocalTime.of(8, 30));

        Shift existingShift2 = new Shift();
        existingShift2.setStartTime(LocalTime.of(9, 30));
        existingShift2.setEndTime(LocalTime.of(10, 30));

        NavigableSet<Shift> shifts = new TreeSet<>(Comparator.comparing(Shift::getStartTime));
        shifts.add(existingShift);
        shifts.add(existingShift2);

        Shift newShift = new Shift();
        newShift.setStartTime(LocalTime.of(8, 20));
        newShift.setEndTime(LocalTime.of(9, 50));

        // Configura la lista de turnos y las condiciones de prueba
        when(shiftService.getOrderedShiftsByDayAssignment(dayAssignment.getId())).thenReturn(shifts);

        // Llamada al método a probar
        Optional<Shift> result = shiftService.addShift(dayAssignment, newShift);

        // Verificación del resultado
        assertTrue(result.isEmpty());
        verify(shiftRepository, never()).save(newShift);
    }
}
