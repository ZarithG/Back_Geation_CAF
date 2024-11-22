package com.uptc.cafmicroservice.dto;

import com.uptc.cafmicroservice.enums.InscriptionStatusEnum;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInscriptionDTO {
    private int inscriptionId;
    private Date inscriptionDate;
    private InscriptionStatusEnum inscriptionStatus;
    private UserAllDataDTO userAllDataDTO;
}
