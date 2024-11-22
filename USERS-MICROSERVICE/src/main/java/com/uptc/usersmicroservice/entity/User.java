package com.uptc.usersmicroservice.entity;

import com.uptc.usersmicroservice.enums.UserTypeEnum;
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

    @Column(nullable = false, unique = true)
    private String email;
    private String documentType;
    private String documentNumber;
    private String universityCode;
    private Date birthDate;
    private String phoneNumber;
    private UserTypeEnum userType;
    private String residenceAddress;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @OneToOne
    @JoinColumn(name = "emergency_contact_id", referencedColumnName = "id")
    private EmergencyContact emergencyContact;

    @OneToOne
    @JoinColumn(name = "medical_information_id", referencedColumnName = "id")
    private MedicalInformation medicalInformation;

    @OneToOne
    @JoinColumn(name = "university_information_id", referencedColumnName = "id")
    private UniversityInformation universityInformation;
}