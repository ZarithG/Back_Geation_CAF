package com.uptc.cafmicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FitnessCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    @OneToOne
    @JoinColumn(name = "sectional_id", referencedColumnName = "id")
    private Sectional sectional;
    private String coordinatorEmail;

    //Revisar
    @OneToMany(mappedBy = "fitnessCenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Inscription> inscriptions = new HashSet<>();
}
