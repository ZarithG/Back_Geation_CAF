package com.uptc.shiftmicroservice.errors;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ConflictResponse {
    private int dayAssignmentId;
    private int shiftId;
    private String errorMessage;
}
