package com.uptc.authmicroservice.controller;

import com.uptc.authmicroservice.dto.*;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.mapper.AuthUserMapper;
import com.uptc.authmicroservice.security.JwtProvider;
import com.uptc.authmicroservice.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody AuthUserDTO dto){
        TokenDTO tokenDto = authUserService.login(dto);
        if(tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
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
}