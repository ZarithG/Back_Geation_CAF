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

/**
 * Manejador personalizado para el éxito de la autenticación OAuth2. Este componente extiende de
 * SimpleUrlAuthenticationSuccessHandler y maneja lo que ocurre una vez que el usuario se autentica
 * exitosamente mediante OAuth2.
 */
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    JwtProvider jwtProvider;  // Proveedor de JWT para generar tokens de autenticación.

    @Autowired
    AuthUserService authUserService;  // Servicio para manejar la lógica de usuarios (como registro, cambio de rol, etc.).

    @Autowired
    RestTemplate restTemplate;  // Plantilla de RestTemplate para realizar solicitudes HTTP a otros servicios.

    /**
     * Este método se ejecuta después de una autenticación exitosa mediante OAuth2. Aquí se obtiene la
     * información del usuario autenticado y se maneja la creación o actualización de un usuario en la base de datos.
     *
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @param authentication El objeto de autenticación que contiene la información del usuario autenticado.
     * @throws IOException Si ocurre un error de entrada o salida.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // Obtiene los detalles del usuario autenticado desde el token OAuth2.
        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauth2AuthenticationToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String pictureUrl = oAuth2User.getAttribute("picture");

        // Busca el usuario en la base de datos usando el correo electrónico.
        AuthUser user = authUserService.getUserByUserName(email);
        ResponseEntity<UserDTO> userDTOResponseEntity;

        try {
            // Llama a un microservicio para obtener detalles adicionales sobre el usuario.
            userDTOResponseEntity = restTemplate.exchange(
                    "http://USERS-MICROSERVICE/user/email/" + email,
                    HttpMethod.GET,
                    null,
                    UserDTO.class
            );

            // Si el usuario no existe en la base de datos o no tiene datos adicionales en el microservicio, crea un nuevo usuario.
            if (user == null || userDTOResponseEntity.getBody() == null) {
                AuthUserDTO userDTOToCreate = new AuthUserDTO();
                userDTOToCreate.setUserName(email);
                userDTOToCreate.setPictureUrl(pictureUrl);
                userDTOToCreate.setActive(true);

                // Asigna un rol por defecto (ROLE_USER).
                Set<Role> roles = new HashSet<>();
                Role role = new Role();
                role.setId(1);
                role.setRoleName(RoleEnum.ROLE_USER);
                roles.add(role);
                userDTOToCreate.setRoles(roles);

                // Guarda al nuevo usuario en la base de datos.
                user = authUserService.save(userDTOToCreate);

                // Crea un DTO de usuario y lo envía al microservicio para crear un nuevo registro.
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

            // Genera un token JWT para el usuario autenticado.
            String token = jwtProvider.createToken(user);

            // Elimina información sensible antes de enviar el usuario al cliente.
            user.setId(0);
            user.setPassword(null);

            // Mapea el usuario a un DTO.
            AuthUserDTO userDTO = AuthUserMapper.INSTANCE.mapUserToUserDTO(user);
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setToken(token);
            userDTO.setToken(tokenDTO);

            // Convierte el DTO del usuario a JSON.
            ObjectMapper objectMapper = new ObjectMapper();
            String authUserJson = objectMapper.writeValueAsString(userDTO);

            // Busca al usuario en la base de datos para determinar su rol.
            AuthUser authUser = authUserService.getUserByUserName(userDTO.getUserName());

            boolean isUser = false;
            String targetUrl = "";

            // Verifica si el usuario tiene el rol ROLE_USER. Si es así, redirige a una página específica.
            for (int i = 0; i < authUser.getRoles().toArray().length; i++) {
                if (authUser.getRoles().toArray()[i].equals(RoleEnum.ROLE_USER)) {
                    isUser = true;
                    targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/register/informationData")
                            .queryParam("authUser", URLEncoder.encode(authUserJson, StandardCharsets.UTF_8))
                            .build().toUriString();
                    getRedirectStrategy().sendRedirect(request, response, targetUrl);
                }
            }

            // Si el usuario no tiene el rol ROLE_USER, se redirige a la página principal.
            if (!isUser) {
                targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/")
                        .queryParam("authUser", URLEncoder.encode(authUserJson, StandardCharsets.UTF_8))
                        .build().toUriString();
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
            }

        } catch (Exception e) {
            // Si ocurre un error, invalida la sesión y redirige al usuario a la página principal.
            authUserService.logout(request, response);
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/")
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}