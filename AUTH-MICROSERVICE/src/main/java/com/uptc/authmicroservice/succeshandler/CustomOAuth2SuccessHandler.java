package com.uptc.authmicroservice.succeshandler;

import com.uptc.authmicroservice.dto.AuthUserDTO;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.enums.RoleEnum;
import com.uptc.authmicroservice.security.JwtProvider;
import com.uptc.authmicroservice.service.AuthUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthUserService authUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauth2AuthenticationToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        AuthUser user = authUserService.getUserByUserName(email);
        if (user == null) {
            AuthUserDTO userDTOToCreate = new AuthUserDTO();
            userDTOToCreate.setUserName(email);
            Set<Role> roles = new HashSet<>();
            Role role = new Role();
            role.setId(1);
            role.setRoleName(RoleEnum.ROLE_USER);
            roles.add(role);

            userDTOToCreate.setRoles(roles);
            user = authUserService.save(userDTOToCreate);
        }

        String token = jwtProvider.createToken(user);

        String targetUrl = UriComponentsBuilder.fromUriString("/auth/google/login")
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}