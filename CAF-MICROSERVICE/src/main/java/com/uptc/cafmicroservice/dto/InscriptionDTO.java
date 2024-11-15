package com.uptc.cafmicroservice.dto;

import com.uptc.cafmicroservice.enums.ConsentTypeEnum;
import com.uptc.cafmicroservice.enums.InscriptionStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InscriptionDTO {
    private int id;
    private int userId;
    private Date inscriptionDate;
    private InscriptionStatusEnum inscriptionStatus;

    private FitnessCenterDTO fitnessCenterDTO;
    private List<UserResponseDTO> userResponseDTOList;
}
