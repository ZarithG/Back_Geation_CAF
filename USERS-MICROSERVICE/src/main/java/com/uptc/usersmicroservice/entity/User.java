package com.uptc.usersmicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;
    private String documentType;
    private String documentNumber;
    private String universityCode;
    private Date birthDate;
    private String phoneNumber;
    private String residenceAddress;
    private String department;
    private String city;
}
