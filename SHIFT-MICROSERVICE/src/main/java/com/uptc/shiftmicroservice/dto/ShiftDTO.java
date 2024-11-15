package com.uptc.shiftmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uptc.shiftmicroservice.entity.DayAssignment;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDTO {
    private int id;
    private int dayAssignment;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    //@Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    //@Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private int placeAvailable;
}
