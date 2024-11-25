package com.uptc.shiftmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftActualDTO {
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
    private int maximumPlaceAvailable;
    private int status;

    private long shiftInstanceId;
}
