package com.uptc.shiftmicroservice.entity;

import com.uptc.shiftmicroservice.entity.Day;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DayAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int fitnessCenter;
    @Enumerated(EnumType.STRING)
    private Day day;
}
