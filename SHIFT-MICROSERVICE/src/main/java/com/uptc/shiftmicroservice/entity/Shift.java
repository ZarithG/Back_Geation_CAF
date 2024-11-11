package com.uptc.shiftmicroservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Shift implements Comparable<Shift>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "day_assignment_id", nullable = false)
    private DayAssignment dayAssignment;

    //@Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    //@Temporal(TemporalType.TIME)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private int placeAvailable;

    @Override
    public int compareTo(Shift other) {
        return this.startTime.compareTo(other.startTime);
    }
}
