package com.uptc.authmicroservice.service;

import com.uptc.authmicroservice.dto.AuthUserDTO;
import com.uptc.authmicroservice.dto.RequestDTO;
import com.uptc.authmicroservice.dto.TokenDTO;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.enums.RoleEnum;
import com.uptc.authmicroservice.repository.AuthUserRepository;
import com.uptc.authmicroservice.repository.RoleRepository;
import com.uptc.authmicroservice.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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

    public void logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().invalidate();
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
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
        return user.map(AuthUser::getRoles).orElse(null);
    }

    public AuthUser changeAuthUserState(String userName, boolean authUserState){
        AuthUser user = getUserByUserName(userName);
        System.out.println("USER NAME: " + user.getUserName() + " - " + authUserState);
        if(user.getId() != 0){
            user.setActive(authUserState);
            AuthUser userToChangeState = authUserRepository.save(user);
            if(userToChangeState.getId() != 0){
                return userToChangeState;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public AuthUser changeAuthUserRole(String userName, RoleEnum roleEnum){
        Optional<AuthUser> user = authUserRepository.findByUserName(userName);
        if (user.isPresent()){
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByRoleName(roleEnum));

            user.get().setRoles(roles);
            return authUserRepository.save(user.get());
        }
        return null;
    }

    public AuthUser changeWellbeingDirector(String userName){
        List<AuthUser> actualWellbeingDirector = authUserRepository.findByRoleName(RoleEnum.ROLE_WELLBEING_DIRECTOR);
        if(!actualWellbeingDirector.isEmpty()){
            AuthUser changeActualDirectorRol = changeAuthUserRole(actualWellbeingDirector.get(0).getUserName(), RoleEnum.ROLE_USER);
            if(changeActualDirectorRol == null){
                return null;
            }
        }

        AuthUser newWellbeingDirector = changeAuthUserRole(userName, RoleEnum.ROLE_WELLBEING_DIRECTOR);
        if(newWellbeingDirector == null){
            return null;
        }
        return newWellbeingDirector;
    }
}
