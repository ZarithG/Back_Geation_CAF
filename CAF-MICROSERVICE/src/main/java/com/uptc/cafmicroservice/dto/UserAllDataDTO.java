package com.uptc.cafmicroservice.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAllDataDTO {
    private int id;
    private String name;
    private String email;
    private String documentType;
    private String documentNumber;
    private String universityCode;
    private String userType;
    private Date birthDate;
    private String phoneNumber;
    private String residenceAddress;
    private String department;
    private String city;

    private String eps;
    private String bloodGroup;
    private String allergies;

    private String contactName;
    private String contactLastname;
    private String contactPhone;
    private String contactEmail;
    private String contactRelationship;
    private String contactResidenceAddress;
}
