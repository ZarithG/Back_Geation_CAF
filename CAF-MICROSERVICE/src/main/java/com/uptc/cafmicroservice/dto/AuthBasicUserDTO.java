package com.uptc.cafmicroservice.dto;

import com.uptc.cafmicroservice.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthBasicUserDTO {
    private int id;
    private String userName;
    private Set<RoleEnum> roles = new HashSet<>();
}
