package com.uptc.authmicroservice.dto;

import com.uptc.authmicroservice.entity.Role;
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
public class AuthUserDTO {
    private String userName;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private boolean isUserVerified = false;
    private String pictureUrl;
}
