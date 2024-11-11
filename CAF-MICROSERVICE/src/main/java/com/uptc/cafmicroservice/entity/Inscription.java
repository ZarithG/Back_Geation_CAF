package com.uptc.cafmicroservice.entity;

import com.uptc.cafmicroservice.enums.InscriptionStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;
    private Date inscriptionDate;

    @Enumerated(EnumType.STRING)
    private InscriptionStatusEnum inscriptionStatus;

    @ManyToOne
    @JoinColumn(name = "fitness_center_id", nullable = false)
    private FitnessCenter fitnessCenter;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserResponse> userResponseList;
}
