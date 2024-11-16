package com.uptc.authmicroservice.security;

import com.uptc.authmicroservice.dto.RequestDTO;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.enums.RoleEnum;
import com.uptc.authmicroservice.validator.AdminRouteValidator;
import com.uptc.authmicroservice.validator.UserRouteValidator;
import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    @Autowired
    AdminRouteValidator adminRouteValidator;

    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private UserRouteValidator userRouteValidator;

    public String createToken(AuthUser user){
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(new Date().getTime() + 3600000);

        return Jwts.builder()
                .header().type("JWT")
                .and()
                .subject(user.getUserName())
                .claim("Authorities", user.getRoles())
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(generateSigningKey())
                .compact();
    }

    private SecretKey generateSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public Claims extractClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(generateSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public boolean validate(String token, RequestDTO requestDTO) {
        try {
            if(!new Date().before(extractExpiration(token))) {
                return false;
            }
        }catch (Exception e){
            return false;
        }

        ArrayList<Integer> pathValidatorNumbers = obtainRouteValidatorFromPath(requestDTO);
        boolean isRoleAssociatedWithpath = false;
        for (int i = 0; i < pathValidatorNumbers.size(); i++){
            if(verifyRoleInRouteValidator(token, pathValidatorNumbers.get(i))){
                isRoleAssociatedWithpath = true;
                break;
            }
        }

        return isRoleAssociatedWithpath;
    }

    private ArrayList<Integer> obtainRouteValidatorFromPath(RequestDTO requestDTO){
        ArrayList<Integer> routeValidators = new ArrayList<>();
        if (adminRouteValidator.isAdminPath(requestDTO)) {
            routeValidators.add(0);
        }

        if (userRouteValidator.isUserPath(requestDTO)) {
            routeValidators.add(1);
        } else {
            routeValidators.add(2);
        }
        return routeValidators;
    }

    private boolean verifyRoleInRouteValidator(String token, int pathValidatorNumber){
        switch (pathValidatorNumber){
            case 0:
                if(!isUserWithRole(token, RoleEnum.ROLE_ADMIN)){
                    return false;
                }
                return true;
            case 1:
                if(!isUserWithRole(token, RoleEnum.ROLE_USER)) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    private boolean isUserWithRole(String token, RoleEnum roleToValidate){
        Set<Role> roles = getAuthoritiesFromToken(token);
        for(Role role : roles){
            if (role.getRoleName().equals(roleToValidate))
                return true;
        }
        return false;
    }

    public String getUserNameFromToken(String token){
        try {
            return extractClaims(token).getSubject();
        }catch (Exception e) {
            return "bad token";
        }
    }

    public Set<Role> getAuthoritiesFromToken(String token) {
        try {
            Claims claims = extractClaims(token);
            List<Map<String, String>> roles = claims.get("Authorities", List.class);

            return roles.stream()
                    .map(roleMap -> Role.builder()
                            .roleName(RoleEnum.valueOf(roleMap.get("roleName")))
                            .build())
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    public Date extractExpiration(String jwtToken) {
        return extractClaims(jwtToken).getExpiration();
    }
}
