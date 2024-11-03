package com.uptc.authmicroservice.service;

import com.uptc.authmicroservice.dto.AuthUserDTO;
import com.uptc.authmicroservice.dto.RequestDTO;
import com.uptc.authmicroservice.dto.TokenDTO;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.repository.AuthUserRepository;
import com.uptc.authmicroservice.repository.RoleRepository;
import com.uptc.authmicroservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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

    public AuthUser save(AuthUserDTO dto) {
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
        if(user.isPresent())
            return null;
        AuthUser authUser = AuthUser.builder()
                .userName(dto.getUserName())
                .password("DEFAULT")
                .roles(dto.getRoles())
                .build();
        return authUserRepository.save(authUser);
    }

    public TokenDTO login(AuthUserDTO authUserDTO) {
        Optional<AuthUser> user = authUserRepository.findByUserName(authUserDTO.getUserName());
        if(user.isEmpty())
            return null;
        if(passwordEncoder.matches(authUserDTO.getPassword(), user.get().getPassword()))
            return new TokenDTO(jwtProvider.createToken(user.get()));
        return null;
    }

    public TokenDTO validate(String token, RequestDTO requestDTO) {
        if(!jwtProvider.validate(token, requestDTO))
            return null;
        String username = jwtProvider.getUserNameFromToken(token);
        if(authUserRepository.findByUserName(username).isEmpty())
            return null;
        return new TokenDTO(token);
    }

    public AuthUser getUserByUserName(String userName){
        Optional<AuthUser> user = authUserRepository.findByUserName(userName);
        return user.orElse(null);
    }

    public AuthUser verifyAuthUser(String userName){
        AuthUser authUser = getUserByUserName(userName);
        if (authUser != null){
            authUser.setUserVerified(true);
            authUser = authUserRepository.save(authUser);
        }
        return authUser;
    }

    public AuthUser changeAuthUserPassword(String userName, String password){
        Optional<AuthUser> user = authUserRepository.findByUserName(userName);

        AuthUser userChanged = null;
        if (user.isPresent()){
            String encodedPassword = passwordEncoder.encode(password);
            user.get().setPassword(encodedPassword);
            userChanged = authUserRepository.save(user.get());
        }
        return userChanged;
    }

    public Set<Role> getRolesByUserName(String userName){
        Optional<AuthUser> user = authUserRepository.findByUserName(userName);
        return user.get().getRoles();
    }
}
