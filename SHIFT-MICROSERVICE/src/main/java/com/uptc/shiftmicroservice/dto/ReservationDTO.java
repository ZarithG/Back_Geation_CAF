package com.uptc.shiftmicroservice.dto;

import com.uptc.shiftmicroservice.entity.Shift;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReservationDTO {

        private long id;

        private long idShiftInstance;

        private int idDayAssignment;

        private int userId;

        private LocalDateTime dateReservation;

        private int attendance;
}
