package com.uptc.authmicroservice.service;

import com.uptc.authmicroservice.dto.*;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.enums.RoleEnum;
import com.uptc.authmicroservice.mapper.AuthUserMapper;
import com.uptc.authmicroservice.repository.AuthUserRepository;
import com.uptc.authmicroservice.repository.RoleRepository;
import com.uptc.authmicroservice.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para manejar la lógica relacionada con la autenticación y los usuarios.
 * Proporciona métodos para el registro, autenticación, validación de roles, y gestión de usuarios.
 */
@Service
public class AuthUserService {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RestTemplate restTemplate;

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param dto Objeto con los datos del usuario a registrar.
     * @return El usuario registrado o null si el usuario ya existe.
     */
    public AuthUser save(AuthUserDTO dto) {
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
        if (user.isPresent())
            return null;

        Set<Role> roles = new HashSet<>();
        for (Role role : dto.getRoles()){
            roles.add(roleRepository.findByRoleName(role.getRoleName()));
        }

        AuthUser authUser = AuthUser.builder()
                .userName(dto.getUserName())
                .password("DEFAULT") // Contraseña predeterminada.
                .roles(roles)
                .isActive(true)
                .build();

        return authUserRepository.save(authUser);
    }

    /**
     * Autentica a un usuario con su nombre de usuario y contraseña.
     *
     * @param authUserDTO Datos de autenticación del usuario.
     * @return Un token si las credenciales son válidas, de lo contrario null.
     */
    public TokenDTO login(AuthUserDTO authUserDTO) {
        Optional<AuthUser> user = authUserRepository.findByUserName(authUserDTO.getUserName());
        if (user.isEmpty())
            return null;

        if (passwordEncoder.matches(authUserDTO.getPassword(), user.get().getPassword())) {
            if (user.get().isActive()) {
                return new TokenDTO(jwtProvider.createToken(user.get()));
            } else {
                return TokenDTO.builder().token("").build(); // Usuario inactivo.
            }
        }
        return null;
    }

    /**
     * Cierra la sesión del usuario, invalidando la sesión actual.
     *
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * Verifica si un usuario está validado.
     *
     * @param userName Nombre del usuario.
     * @return true si el usuario está validado, de lo contrario false.
     */
    public boolean isUserVerified(String userName) {
        Optional<AuthUser> authUser = authUserRepository.findByUserName(userName);
        return authUser.map(AuthUser::isUserVerified).orElse(false);
    }

    /**
     * Valida un token JWT y verifica las rutas de acceso.
     *
     * @param token       Token JWT.
     * @param requestDTO  Datos de la solicitud.
     * @return Un objeto TokenDTO si la validación es exitosa, de lo contrario null.
     */
    public TokenDTO validate(String token, RequestDTO requestDTO) {
        if (!jwtProvider.validate(token, requestDTO))
            return null;

        String username = jwtProvider.getUserNameFromToken(token);
        if (authUserRepository.findByUserName(username).isEmpty())
            return null;

        return new TokenDTO(token);
    }

    /**
     * Obtiene un usuario por su nombre.
     *
     * @param userName Nombre del usuario.
     * @return El usuario encontrado o null si no existe.
     */
    public AuthUser getUserByUserName(String userName) {
        Optional<AuthUser> user = authUserRepository.findByUserName(userName);
        return user.orElse(null);
    }

    /**
     * Marca a un usuario como verificado.
     *
     * @param userName Nombre del usuario.
     * @return El usuario actualizado.
     */
    public AuthUser verifyAuthUser(String userName) {
        AuthUser authUser = getUserByUserName(userName);
        if (authUser != null) {
            authUser.setUserVerified(true);
            authUser = authUserRepository.save(authUser);
        }
        return authUser;
    }

    /**
     * Obtiene una lista de todos los usuarios, excluyendo a los administradores.
     *
     * @return Lista de usuarios con detalles completos.
     */
    public List<AuthUserCompleteDTO> getAllUser() {
        List<AuthUser> authUserList = authUserRepository.findAll();
        List<AuthUserCompleteDTO> authUserDTOList = new ArrayList<>();

        for (AuthUser authUser : authUserList) {
            boolean isAdmin = false;
            List<String> roles = new ArrayList<>();

            for (Role role : authUser.getRoles()) {
                roles.add(role.getRoleName().name());
                if (role.getRoleName().equals(RoleEnum.ROLE_ADMIN)) {
                    isAdmin = true;
                    break;
                }
            }

            if (!isAdmin) {
                ResponseEntity<UserBasicDTO> responseFromBasicUser = restTemplate.exchange(
                        "http://USERS-MICROSERVICE/user/basic/" + authUser.getUserName(),
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        UserBasicDTO.class
                );

                if(responseFromBasicUser.getBody() != null){
                    UserBasicDTO userBasicDTO = responseFromBasicUser.getBody();

                    AuthUserCompleteDTO authUserCompleteDTO = new AuthUserCompleteDTO();
                    authUserCompleteDTO.setId(authUser.getId());
                    authUserCompleteDTO.setName(userBasicDTO.getName());
                    authUserCompleteDTO.setUserName(authUser.getUserName());
                    authUserCompleteDTO.setRoles(roles);
                    authUserCompleteDTO.setActive(authUser.isActive());
                    authUserCompleteDTO.setUserVerified(authUser.isUserVerified());
                    authUserDTOList.add(authUserCompleteDTO);
                }
            }
        }
        return authUserDTOList;
    }

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param userName Nombre del usuario.
     * @param password Nueva contraseña.
     * @return El usuario con la contraseña actualizada.
     */
    public AuthUser changeAuthUserPassword(String userName, String password) {
        Optional<AuthUser> user = authUserRepository.findByUserName(userName);
        AuthUser userChanged = null;
        if (user.isPresent()) {
            String encodedPassword = passwordEncoder.encode(password);
            user.get().setPassword(encodedPassword);
            userChanged = authUserRepository.save(user.get());
        }
        return userChanged;
    }

    /**
     * Obtiene la lista de roles asociados a un usuario por su nombre de usuario.
     *
     * @param userName El nombre de usuario del cual se desean obtener los roles.
     * @return Una lista de roles asociados al usuario.
     */
    public List<Role> getRolesByUserName(String userName) {
        // Busca al usuario en la base de datos por su nombre de usuario.
        Optional<AuthUser> user = authUserRepository.findByUserName(userName);

        // Convierte el conjunto de roles (Set<Role>) a una lista (List<Role>) y la retorna.
        List<Role> roleList = user.get().getRoles().stream().collect(Collectors.toList());
        return roleList;
    }

    /**
     * Cambia el estado activo/inactivo de un usuario.
     *
     * @param userName El nombre de usuario cuyo estado se desea cambiar.
     * @param authUserState El nuevo estado (true para activo, false para inactivo).
     * @return El usuario con el estado actualizado, o null si el cambio no fue exitoso.
     */
    public AuthUser changeAuthUserState(String userName, boolean authUserState) {
        // Obtiene el usuario por su nombre de usuario.
        AuthUser user = getUserByUserName(userName);

        // Verifica si el usuario tiene un ID válido.
        if (user.getId() != 0) {
            // Cambia el estado activo del usuario.
            user.setActive(authUserState);

            // Guarda los cambios en la base de datos.
            AuthUser userToChangeState = authUserRepository.save(user);

            // Retorna el usuario actualizado si la operación fue exitosa.
            if (userToChangeState.getId() != 0) {
                return userToChangeState;
            } else {
                return null;
            }
        } else {
            return null; // Retorna null si el usuario no tiene un ID válido.
        }
    }

    /**
     * Cambia el rol de un usuario al rol especificado.
     *
     * @param userName El nombre de usuario cuyo rol se desea cambiar.
     * @param roleEnum El nuevo rol que se asignará al usuario.
     * @return El usuario con el rol actualizado, o null si el cambio no fue exitoso.
     */
    public AuthUser changeAuthUserRole(String userName, RoleEnum roleEnum) {
        // Busca al usuario en la base de datos por su nombre de usuario.
        Optional<AuthUser> user = authUserRepository.findByUserName(userName);

        if (user.isPresent()) {
            // Crea un nuevo conjunto de roles y agrega el nuevo rol al conjunto.
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByRoleName(roleEnum));

            // Asigna el nuevo conjunto de roles al usuario.
            user.get().setRoles(roles);

            // Guarda los cambios en la base de datos y retorna el usuario actualizado.
            return authUserRepository.save(user.get());
        }
        return null; // Retorna null si el usuario no existe.
    }

    /**
     * Cambia al director de bienestar actual por un nuevo usuario.
     *
     * @param userName El nombre de usuario que se convertirá en el nuevo director de bienestar.
     * @return El nuevo director de bienestar, o null si el cambio no fue exitoso.
     */
    public AuthUser changeWellbeingDirector(String userName) {
        // Busca al director de bienestar actual en la base de datos.
        List<AuthUser> actualWellbeingDirector = authUserRepository.findByRoleName(RoleEnum.ROLE_WELLBEING_DIRECTOR);

        // Si existe un director de bienestar actual, cambia su rol a ROLE_USER.
        if (!actualWellbeingDirector.isEmpty()) {
            AuthUser changeActualDirectorRol = changeAuthUserRole(actualWellbeingDirector.get(0).getUserName(), RoleEnum.ROLE_USER);

            // Si no se pudo cambiar el rol del director actual, retorna null.
            if (changeActualDirectorRol == null) {
                return null;
            }
        }

        // Cambia el rol del nuevo usuario a ROLE_WELLBEING_DIRECTOR.
        AuthUser newWellbeingDirector = changeAuthUserRole(userName, RoleEnum.ROLE_WELLBEING_DIRECTOR);

        // Si no se pudo asignar el rol al nuevo director, retorna null.
        if (newWellbeingDirector == null) {
            return null;
        }
        return newWellbeingDirector; // Retorna el nuevo director de bienestar.
    }

    public AuthUserCompleteDTO obtainWellbeingDirector() {
        List<AuthUser> actualWellbeingDirector = authUserRepository.findByRoleName(RoleEnum.ROLE_WELLBEING_DIRECTOR);
        if (!actualWellbeingDirector.isEmpty()) {
            AuthUser authUser = actualWellbeingDirector.get(0);
            ResponseEntity<UserBasicDTO> responseFromBasicUser = restTemplate.exchange(
                    "http://USERS-MICROSERVICE/user/basic/" + authUser.getUserName(),
                    HttpMethod.GET,
                    new HttpEntity<>(null),
                    UserBasicDTO.class
            );
            if (responseFromBasicUser.getStatusCode().equals(HttpStatus.OK)){
                return convertUserBasicToAuthUserCompleteDTO(responseFromBasicUser.getBody(), authUser);
            }
            return null;
        }
        return null;
    }

    public List<AuthUserCompleteDTO> getAllCAFCoordinators() {
        List<AuthUser> authUserList = authUserRepository.findByRoleName(RoleEnum.ROLE_CAF_COORDINATOR);
        List<AuthUserCompleteDTO> authUserDTOList = new ArrayList<>();
        if (!authUserList.isEmpty()){
            for (AuthUser authUser : authUserList) {
                ResponseEntity<UserBasicDTO> responseFromBasicUser = restTemplate.exchange(
                        "http://USERS-MICROSERVICE/user/basic/" + authUser.getUserName(),
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        UserBasicDTO.class
                );
                if (responseFromBasicUser.getStatusCode().equals(HttpStatus.OK)){
                    authUserDTOList.add(convertUserBasicToAuthUserCompleteDTO(responseFromBasicUser.getBody(), authUser));
                }
            }
            return authUserDTOList;
        }
        return null;
    }

    private AuthUserCompleteDTO convertUserBasicToAuthUserCompleteDTO(UserBasicDTO userBasicDTO, AuthUser authUser) {
        AuthUserCompleteDTO authUserCompleteDTO = new AuthUserCompleteDTO();
        authUserCompleteDTO.setId(userBasicDTO.getId());
        authUserCompleteDTO.setName(userBasicDTO.getName());
        authUserCompleteDTO.setUserName(userBasicDTO.getEmail());
        authUserCompleteDTO.setActive(authUser.isActive());
        return authUserCompleteDTO;
    }
}
