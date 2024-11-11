package com.uptc.cafmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponseDTO {
    private int id;
    private PARQQuestionDTO parqQuestionDTO;
    private boolean questionAnswer;
}
