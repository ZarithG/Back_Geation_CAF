package com.uptc.authmicroservice.controller;

import com.uptc.authmicroservice.dto.*;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.enums.RoleEnum;
import com.uptc.authmicroservice.mapper.AuthUserMapper;
import com.uptc.authmicroservice.security.JwtProvider;
import com.uptc.authmicroservice.service.AuthUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controlador REST para manejar las operaciones relacionadas con la autenticación y gestión de usuarios.
 */
@RestController
@RequestMapping("/auth")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    /**
     * Maneja el inicio de sesión de usuarios.
     *
     * @param dto Datos de usuario proporcionados para la autenticación.
     * @return ResponseEntity con los detalles del usuario autenticado o un estado de error.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthUserDTO> login(@RequestBody AuthUserDTO dto) {
        TokenDTO tokenDto = authUserService.login(dto);
        if (tokenDto == null) {
            return ResponseEntity.badRequest().build();
        } else if (tokenDto.getToken().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        AuthUser authUser = authUserService.getUserByUserName(dto.getUserName());
        AuthUserDTO authUserDTO = AuthUserMapper.INSTANCE.mapUserToUserDTO(authUser);
        authUserDTO.setRoles(authUser.getRoles());
        authUserDTO.setToken(tokenDto);
        authUserDTO.setPassword(null); // Evitar exponer la contraseña.
        authUserDTO.setUserVerified(authUser.isUserVerified());

        return ResponseEntity.ok(authUserDTO);
    }

    /**
     * Maneja el cierre de sesión de usuarios.
     *
     * @param request  Petición HTTP.
     * @param response Respuesta HTTP.
     */
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authUserService.logout(request, response);
    }

    /**
     * Recupera una lista de todos los usuarios registrados.
     *
     * @return ResponseEntity con la lista de usuarios o un estado de "No Content" si la lista está vacía.
     */
    @GetMapping("/user/all")
    public ResponseEntity<List<AuthUserCompleteDTO>> getAllUser() {
        List<AuthUserCompleteDTO> authUserCompleteDTOList = authUserService.getAllUser();
        if (authUserCompleteDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authUserCompleteDTOList);
    }

    @GetMapping("/user/caf/all")
    public ResponseEntity<List<AuthUserCompleteDTO>> getAllCAFCoordinators() {
        List<AuthUserCompleteDTO> cafCoordinatorsDTOList = authUserService.getAllCAFCoordinators();
        if (cafCoordinatorsDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cafCoordinatorsDTOList);
    }

    @GetMapping("/user/is-registered/{email}")
    public ResponseEntity<AuthUser> isUserRegisteredByEmail(@PathVariable("email") String email){
        AuthUser authUser = authUserService.getUserByUserName(email);
        if (authUser == null) {
            return ResponseEntity.noContent().build();
        }
        authUser.setPassword(null);
        return ResponseEntity.ok(authUser);
    }

    /**
     * Valida un token para una solicitud específica.
     *
     * @param token      Token a validar.
     * @param requestDTO Datos adicionales para la validación.
     * @return ResponseEntity con el token validado o un estado de error.
     */
    @PostMapping("/validate")
    public ResponseEntity<TokenDTO> validate(@RequestParam String token, @RequestBody RequestDTO requestDTO) {
        TokenDTO tokenDto = authUserService.validate(token, requestDTO);
        if (tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param dto Datos del usuario a crear.
     * @return ResponseEntity con los datos del usuario creado o un estado de error.
     */
    @PostMapping("/create")
    public ResponseEntity<AuthUserDTO> create(@RequestBody AuthUserDTO dto) {
        AuthUser authUser = authUserService.save(dto);
        if (authUser == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(AuthUserMapper.INSTANCE.mapUserToUserDTO(authUser));
    }

    /**
     * Verifica si un usuario está registrado utilizando su correo electrónico.
     *
     * @param dto Objeto que contiene el correo electrónico del usuario.
     * @return ResponseEntity con los detalles del usuario o un estado de error.
     */
    @PostMapping("/verify/user")
    public ResponseEntity<AuthUser> verifyUser(@RequestBody UserDTO dto) {
        AuthUser authUser = authUserService.verifyAuthUser(dto.getEmail());
        if (authUser == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authUser);
    }

    /**
     * Verifica si un usuario está marcado como verificado.
     *
     * @param email Correo electrónico del usuario.
     * @return ResponseEntity con 1 si está verificado, de lo contrario un estado de error.
     */
    @GetMapping("/isUserVerified/{email}")
    public ResponseEntity<Integer> isUserVerified(@PathVariable("email") String email) {
        if (authUserService.isUserVerified(email))
            return ResponseEntity.ok(1);
        else
            return ResponseEntity.badRequest().build();
    }

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param dto Datos del usuario con la nueva contraseña.
     * @return ResponseEntity con los detalles del usuario o un estado de error.
     */
    @PostMapping("/change/password")
    public ResponseEntity<AuthUser> changePassword(@RequestBody AuthUserDTO dto) {
        AuthUser authUser = authUserService.changeAuthUserPassword(dto.getUserName(), dto.getPassword());
        if (authUser == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authUser);
    }

    /**
     * Cambia el rol de un usuario.
     *
     * @param authBasicUserDTO Datos básicos del usuario con el nuevo rol.
     * @return ResponseEntity con los detalles del cambio de rol o un estado de error.
     */
    @PostMapping("/change-role")
    public ResponseEntity<AuthBasicUserDTO> changeUserRole(@RequestBody AuthBasicUserDTO authBasicUserDTO) {
        AuthUser authUser = authUserService.changeAuthUserRole(authBasicUserDTO.getUserName(),
                new ArrayList<>(authBasicUserDTO.getRoles()).get(0));
        if (authUser == null)
            return ResponseEntity.badRequest().build();
        AuthBasicUserDTO authBasicUserDTOChanged = new AuthBasicUserDTO();
        authBasicUserDTOChanged.setUserName(authUser.getUserName());
        return ResponseEntity.ok(authBasicUserDTOChanged);
    }

    /**
     * Cambia el director de bienestar asignado a un usuario.
     *
     * @param authBasicUserDTO Datos del usuario que será asignado como director de bienestar.
     * @return ResponseEntity con los detalles del cambio o un estado de error.
     */
    @PostMapping("/change-wellbeing-director")
    public ResponseEntity<AuthBasicUserDTO> changeWellbeingDirector(@RequestBody AuthBasicUserDTO authBasicUserDTO) {
        AuthUser authUser = authUserService.changeWellbeingDirector(authBasicUserDTO.getUserName());
        if (authUser == null)
            return ResponseEntity.badRequest().build();
        AuthBasicUserDTO authBasicUserDTOChanged = new AuthBasicUserDTO();
        authBasicUserDTOChanged.setUserName(authUser.getUserName());
        return ResponseEntity.ok(authBasicUserDTOChanged);
    }

    /**
     * Marca un usuario como inactivo.
     *
     * @param userName Nombre del usuario.
     * @return ResponseEntity con los datos del usuario actualizado o un estado de error.
     */
    @PostMapping("/inactive-user/{userName}")
    public ResponseEntity<AuthUserDTO> inactiveUser(@PathVariable("userName") String userName) {
        AuthUser authUser = authUserService.changeAuthUserState(userName, false);
        if (authUser == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(AuthUserMapper.INSTANCE.mapUserToUserDTO(authUser));
    }

    /**
     * Marca un usuario como activo.
     *
     * @param userName Nombre del usuario.
     * @return ResponseEntity con los datos del usuario actualizado o un estado de error.
     */
    @PostMapping("/active-user/{userName}")
    public ResponseEntity<AuthUserDTO> activeUser(@PathVariable("userName") String userName) {
        AuthUser authUser = authUserService.changeAuthUserState(userName, true);
        if (authUser == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(AuthUserMapper.INSTANCE.mapUserToUserDTO(authUser));
    }
}
