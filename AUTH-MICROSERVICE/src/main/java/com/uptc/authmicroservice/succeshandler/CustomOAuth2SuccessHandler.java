package com.uptc.authmicroservice.succeshandler;

import com.uptc.authmicroservice.dto.AuthUserDTO;
import com.uptc.authmicroservice.dto.TokenDTO;
import com.uptc.authmicroservice.dto.UserDTO;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.enums.RoleEnum;
import com.uptc.authmicroservice.mapper.AuthUserMapper;
import com.uptc.authmicroservice.security.JwtProvider;
import com.uptc.authmicroservice.service.AuthUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthUserService authUserService;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauth2AuthenticationToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String pictureUrl = oAuth2User.getAttribute("picture");

        AuthUser user = authUserService.getUserByUserName(email);
        ResponseEntity<UserDTO> userDTOResponseEntity;

        try {

            userDTOResponseEntity = restTemplate.exchange(
                    "http://USERS-MICROSERVICE/user/email/" + email,
                    HttpMethod.GET,
                    null,
                    UserDTO.class
            );

            if (user == null || userDTOResponseEntity.getBody() == null) {
                AuthUserDTO userDTOToCreate = new AuthUserDTO();
                userDTOToCreate.setUserName(email);
                userDTOToCreate.setPictureUrl(pictureUrl);
                userDTOToCreate.setActive(true);

                Set<Role> roles = new HashSet<>();
                Role role = new Role();
                role.setId(1);
                role.setRoleName(RoleEnum.ROLE_USER);
                roles.add(role);

                userDTOToCreate.setRoles(roles);
                user = authUserService.save(userDTOToCreate);

                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(email);
                userDTO.setName(name);
                userDTO.setUserType("STUDENT");

                HttpEntity<UserDTO> requestNewUser = new HttpEntity<>(userDTO);

                ResponseEntity<UserDTO> responseFromNewUser = restTemplate.exchange(
                        "http://USERS-MICROSERVICE/user/save",
                        HttpMethod.POST,
                        requestNewUser,
                        UserDTO.class
                );
            }

            String token = jwtProvider.createToken(user);

            user.setId(0);
            user.setPassword(null);

            AuthUserDTO userDTO = AuthUserMapper.INSTANCE.mapUserToUserDTO(user);
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setToken(token);
            userDTO.setToken(tokenDTO);

            ObjectMapper objectMapper = new ObjectMapper();
            String authUserJson = objectMapper.writeValueAsString(userDTO);

            AuthUser authUser = authUserService.getUserByUserName(userDTO.getUserName());

            boolean isUser = false;
            String targetUrl = "";

            for (int i = 0; i < authUser.getRoles().toArray().length; i++){
                if(authUser.getRoles().toArray()[i].equals(RoleEnum.ROLE_USER)){
                    isUser = true;
                    targetUrl = UriComponentsBuilder.fromUriString("https://cafuptc.netlify.app/register/informationData")
                            .queryParam("authUser", URLEncoder.encode(authUserJson, StandardCharsets.UTF_8))
                            .build().toUriString();
                    getRedirectStrategy().sendRedirect(request, response, targetUrl);
                }
            }

            if(!isUser){
                targetUrl = UriComponentsBuilder.fromUriString("https://cafuptc.netlify.app/")
                        .queryParam("authUser", URLEncoder.encode(authUserJson, StandardCharsets.UTF_8))
                        .build().toUriString();
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
            }

        }catch (Exception e){
            authUserService.logout(request, response);
            String targetUrl = UriComponentsBuilder.fromUriString("https://cafuptc.netlify.app/")
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}