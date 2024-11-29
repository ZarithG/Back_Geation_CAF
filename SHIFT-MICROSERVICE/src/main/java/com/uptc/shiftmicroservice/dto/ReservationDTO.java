package com.uptc.shiftmicroservice.dto;

import com.uptc.shiftmicroservice.enums.ReservationEnum;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private long id;

    private long idShiftInstance;

    private int idDayAssignment;

    private int userId;

    private LocalDateTime dateReservation;

    private ReservationEnum reservationEnum;
}
