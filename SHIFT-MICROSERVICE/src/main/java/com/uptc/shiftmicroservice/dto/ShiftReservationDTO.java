package com.uptc.shiftmicroservice.dto;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftReservationDTO {
    private UserBasicDTO userBasicDTO;
    private long idShiftInstance;
    private long idReservation;
}
