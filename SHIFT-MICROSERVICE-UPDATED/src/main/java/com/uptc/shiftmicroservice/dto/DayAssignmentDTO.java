package com.uptc.shiftmicroservice.dto;

import com.uptc.shiftmicroservice.entity.Day;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DayAssignmentDTO {
    private int id;
    private int fitnessCenter;
    private Day day;
    private List<ShiftDTO> shifts;
}
