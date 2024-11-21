package com.uptc.shiftmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uptc.shiftmicroservice.entity.Day;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ShiftInstanceDTO {
    private long id;

    private boolean state;

    private int dayAssignment;

    private Day day;

    private LocalDate date;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private int placeAvailable;

    private int fitnessCenter;
}
