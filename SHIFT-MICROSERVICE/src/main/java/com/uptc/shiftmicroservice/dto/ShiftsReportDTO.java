package com.uptc.shiftmicroservice.dto;

import lombok.*;

import java.time.LocalTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftsReportDTO {
    private String dayName;
    private LocalTime startTime;
    private LocalTime endTime;
    private int placeAvailable;
    private long attended;
    private long noAttended;
    private long total;
}
