package com.uptc.authmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthUserCompleteDTO {
    private int id;
    private String name;
    private String userName;
    private boolean isUserVerified;
    private boolean isActive;
}
