package com.uptc.authmicroservice.controller;

import com.uptc.authmicroservice.dto.*;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.mapper.AuthUserMapper;
import com.uptc.authmicroservice.security.JwtProvider;
import com.uptc.authmicroservice.service.AuthUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthUserDTO> login(@RequestBody AuthUserDTO dto){
        TokenDTO tokenDto = authUserService.login(dto);
        if(tokenDto == null)
            return ResponseEntity.badRequest().build();

        AuthUser authUser = authUserService.getUserByUserName(dto.getUserName());
        AuthUserDTO authUserDTO = AuthUserMapper.INSTANCE.mapUserToUserDTO(authUser);
        authUserDTO.setRoles(authUser.getRoles());
        authUserDTO.setToken(tokenDto);
        authUserDTO.setPassword(null);
        authUserDTO.setUserVerified(authUser.isUserVerified());

        return ResponseEntity.ok(authUserDTO);
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authUserService.logout(request, response);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDTO> validate(@RequestParam String token, @RequestBody RequestDTO requestDTO){
        TokenDTO tokenDto = authUserService.validate(token, requestDTO);
        if(tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthUserDTO> create(@RequestBody AuthUserDTO dto){
        AuthUser authUser = authUserService.save(dto);
        if(authUser == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(AuthUserMapper.INSTANCE.mapUserToUserDTO(authUser ));
    }

    @PostMapping("/verify/user")
    public ResponseEntity<AuthUser> verifyUser(@RequestBody UserDTO dto){
        AuthUser authUser = authUserService.verifyAuthUser(dto.getEmail());
        if(authUser == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authUser);
    }

    @PostMapping("/change/password")
    public ResponseEntity<AuthUser> changePassword(@RequestBody AuthUserDTO dto){
        AuthUser authUser = authUserService.changeAuthUserPassword(dto.getUserName(), dto.getPassword());
        if(authUser == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authUser);
    }

    @PostMapping("/change-role")
    public ResponseEntity<AuthBasicUserDTO> changeUserRole(@RequestBody AuthBasicUserDTO authBasicUserDTO){
        AuthUser authUser = authUserService.changeAuthUserRole(authBasicUserDTO.getUserName(), new ArrayList<>(authBasicUserDTO.getRoles()).get(0));
        if(authUser == null)
            return ResponseEntity.badRequest().build();
        AuthBasicUserDTO authBasicUserDTOChanged = new AuthBasicUserDTO();
        authBasicUserDTOChanged.setUserName(authUser.getUserName());
        return ResponseEntity.ok(authBasicUserDTOChanged);
    }

    @PostMapping("/change-wellbeing-director")
    public ResponseEntity<AuthBasicUserDTO> changeWellbeingDirector(@RequestBody AuthBasicUserDTO authBasicUserDTO){
        AuthUser authUser = authUserService.changeWellbeingDirector(authBasicUserDTO.getUserName());
        if(authUser == null)
            return ResponseEntity.badRequest().build();
        AuthBasicUserDTO authBasicUserDTOChanged = new AuthBasicUserDTO();
        authBasicUserDTOChanged.setUserName(authUser.getUserName());
        return ResponseEntity.ok(authBasicUserDTOChanged);
    }

    @PostMapping("/inactive-user/{userName}")
    public ResponseEntity<AuthUserDTO> inactiveUser(@PathVariable("userName") String userName){
        AuthUser authUser = authUserService.changeAuthUserState(userName, false);
        if(authUser == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(AuthUserMapper.INSTANCE.mapUserToUserDTO(authUser));
    }

    @PostMapping("/active-user/{userName}")
    public ResponseEntity<AuthUserDTO> activeUser(@PathVariable("userName") String userName){
        AuthUser authUser = authUserService.changeAuthUserState(userName, true);
        if(authUser == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(AuthUserMapper.INSTANCE.mapUserToUserDTO(authUser));
    }
}