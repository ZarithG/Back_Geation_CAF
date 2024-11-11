package com.uptc.authmicroservice.dto;

import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.enums.RoleEnum;
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
