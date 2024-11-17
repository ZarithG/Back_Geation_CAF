package com.uptc.usersmicroservice.dto;

import com.uptc.usersmicroservice.entity.EmergencyContact;
import com.uptc.usersmicroservice.entity.MedicalInformation;
import com.uptc.usersmicroservice.entity.UniversityInformation;
import com.uptc.usersmicroservice.enums.UserTypeEnum;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private int id;
    private String name;
    private String email;
    private String documentType;
    private String documentNumber;
    private String universityCode;
    private UserTypeEnum userType;
    private Date birthDate;
    private String phoneNumber;
    private String residenceAddress;
    private int cityId;

    private EmergencyContact emergencyContact;
    private UniversityInformation universityInformation;
    private MedicalInformation medicalInformation;
}