package com.uptc.authmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthUserCompleteDTO {
    private int id;
    private String name;
    private List<String> roles;
    private String userName;
    private boolean isUserVerified;
    private boolean isActive;
}
