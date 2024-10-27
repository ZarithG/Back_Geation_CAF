package com.uptc.authmicroservice.controller;

import com.uptc.authmicroservice.dto.AuthUserDTO;
import com.uptc.authmicroservice.dto.RequestDTO;
import com.uptc.authmicroservice.dto.TokenDTO;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.service.AuthUserService;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

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
    public ResponseEntity<AuthUser> create(@RequestBody AuthUserDTO dto){
        AuthUser authUser = authUserService.save(dto);
        if(authUser == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authUser);
    }

    @GetMapping("/google/login")
    public ResponseEntity<TokenDTO> sayWelcome(@RequestParam("token") String token){
        return ResponseEntity.ok(new TokenDTO(token));
    }
}