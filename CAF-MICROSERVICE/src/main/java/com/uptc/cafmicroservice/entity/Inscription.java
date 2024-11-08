package com.uptc.cafmicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private String informedConsentPath;
    private String medicalConsentPath;
    private String tutorConsentPath;

    @ManyToOne
    @JoinColumn(name = "fitness_center_id", nullable = false)
    private FitnessCenter fitnessCenter;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<UserResponse> userResponseList = new HashSet<>();
}
