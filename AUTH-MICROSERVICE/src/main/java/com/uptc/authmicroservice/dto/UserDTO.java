package com.uptc.authmicroservice.dto;

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
    private String userType;
    private Date birthDate;
    private String phoneNumber;
    private String residenceAddress;
    private int department;
    private int city;
}
