package com.uptc.cafmicroservice.dto;

import com.uptc.cafmicroservice.enums.ConsentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsentDTO {
    private int id;
    private ConsentTypeEnum consentType;
}
