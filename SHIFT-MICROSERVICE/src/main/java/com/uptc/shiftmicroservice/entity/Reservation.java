package com.uptc.shiftmicroservice.entity;

import com.uptc.shiftmicroservice.enums.ReservationEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    private ShiftInstance shiftInstance;

    private Long userId;

    private LocalDateTime dateReservation;

    @Enumerated(EnumType.STRING)
    private ReservationEnum reservationEnum;
}
