package com.uptc.shiftmicroservice.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultShiftReportDTO {
    private int fitnessCenter;
    private String day;
    private String startDate;
    private String endDate;

}
