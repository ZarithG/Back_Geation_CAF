package com.uptc.shiftmicroservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int idDayAssignment;

    private Day day;
    private LocalDate date;

    //@Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    //@Temporal(TemporalType.TIME)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private int placeAvailable;

    private int state;
}
