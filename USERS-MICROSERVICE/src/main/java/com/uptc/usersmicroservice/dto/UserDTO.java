package com.uptc.usersmicroservice.dto;

import lombok.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String name;
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
