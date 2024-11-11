package com.uptc.cafmicroservice.service;

import com.uptc.cafmicroservice.dto.AuthBasicUserDTO;
import com.uptc.cafmicroservice.dto.UserBasicDTO;
import com.uptc.cafmicroservice.entity.FitnessCenter;
import com.uptc.cafmicroservice.enums.RoleEnum;
import com.uptc.cafmicroservice.repository.FitnessCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FitnessCenterService {
    @Autowired
    FitnessCenterRepository fitnessCenterRepository;

    @Autowired
    RestTemplate restTemplate;

    public List<FitnessCenter> getAll() {
        return fitnessCenterRepository.findAll();
    }

    public FitnessCenter changeFitnessCenterCoordinator(int fitnessCenterId, String userEmail){
        if(fitnessCenterRepository.existsById(fitnessCenterId)){
            FitnessCenter fitnessCenter = fitnessCenterRepository.findById(fitnessCenterId).get();

            if(!fitnessCenter.getCoordinatorEmail().isEmpty()){
                AuthBasicUserDTO authBasicUserDTO = new AuthBasicUserDTO();
                authBasicUserDTO.setUserName(fitnessCenter.getCoordinatorEmail());
                Set<RoleEnum> roles = new HashSet<>();
                roles.add(RoleEnum.ROLE_USER);
                authBasicUserDTO.setRoles(roles);

                ResponseEntity<AuthBasicUserDTO> responseChangeCoordinatorToUser = restTemplate.exchange(
                        "http://AUTH-MICROSERVICE/auth/change-role",
                        HttpMethod.POST,
                        new HttpEntity<>(authBasicUserDTO),
                        AuthBasicUserDTO.class
                );

                if (responseChangeCoordinatorToUser.getStatusCode() != HttpStatus.OK) {
                    System.out.println("ERRROR CORDINADOR QUE HABIA");
                    return null;
                }
            }

            Set<RoleEnum> roles = new HashSet<>();
            roles.add(RoleEnum.ROLE_CAF_COORDINATOR);
            AuthBasicUserDTO authBasicUserDTO = new AuthBasicUserDTO();
            authBasicUserDTO.setRoles(roles);
            authBasicUserDTO.setUserName(userEmail);

            ResponseEntity<AuthBasicUserDTO> responseChangeUserToCoordinator = restTemplate.exchange(
                    "http://AUTH-MICROSERVICE/auth/change-role",
                    HttpMethod.POST,
                    new HttpEntity<>(authBasicUserDTO),
                    AuthBasicUserDTO.class
            );

            if (responseChangeUserToCoordinator.getStatusCode() != HttpStatus.OK){
                System.out.println("ERRROR CAMBIAR NUEVO COORDINADOR");
                return null;
            }

            fitnessCenter.setCoordinatorEmail(userEmail);
            return fitnessCenterRepository.save(fitnessCenter);
        }
        System.out.println("ERRROR NO EXISTE CAF");
        return null;
    }
}
