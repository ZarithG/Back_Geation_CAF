package com.uptc.shiftmicroservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Shift implements Comparable<com.uptc.shiftmicroservice.entity.Shift>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "day_assignment_id", nullable = false)
    private DayAssignment dayAssignment;

    //@Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private int maximumPlaceAvailable;

    @Override
    public int compareTo(com.uptc.shiftmicroservice.entity.Shift other) {
        return this.startTime.compareTo(other.startTime);
    }
}
