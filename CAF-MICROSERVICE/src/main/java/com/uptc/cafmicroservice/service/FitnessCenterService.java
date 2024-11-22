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
    // Inyección de la dependencia de FitnessCenterRepository
    @Autowired
    FitnessCenterRepository fitnessCenterRepository;

    // Inyección de la dependencia de RestTemplate para realizar llamadas a servicios externos
    @Autowired
    RestTemplate restTemplate;

    /**
     * Obtiene todos los centros de fitness almacenados en la base de datos.
     * @return Lista de objetos FitnessCenter.
     */
    public List<FitnessCenter> getAll() {
        return fitnessCenterRepository.findAll(); // Devuelve la lista completa de centros de fitness
    }

    /**
     * Cambia el coordinador de un centro de fitness dado su ID y el email del nuevo coordinador.
     * @param fitnessCenterId El ID del centro de fitness.
     * @param userEmail El email del nuevo coordinador.
     * @return El objeto FitnessCenter actualizado o null si el cambio falla.
     */
    public FitnessCenter changeFitnessCenterCoordinator(int fitnessCenterId, String userEmail) {
        // Verifica si el centro de fitness existe
        if (fitnessCenterRepository.existsById(fitnessCenterId)) {
            // Obtiene el centro de fitness desde la base de datos
            FitnessCenter fitnessCenter = fitnessCenterRepository.findById(fitnessCenterId).get();

            // Si ya hay un coordinador asignado, cambia su rol a "ROLE_USER"
            if (!fitnessCenter.getCoordinatorEmail().isEmpty()) {
                AuthBasicUserDTO authBasicUserDTO = new AuthBasicUserDTO();
                authBasicUserDTO.setUserName(fitnessCenter.getCoordinatorEmail());

                // Asigna el rol de usuario básico
                Set<RoleEnum> roles = new HashSet<>();
                roles.add(RoleEnum.ROLE_USER);
                authBasicUserDTO.setRoles(roles);

                // Realiza la llamada al microservicio de autenticación para cambiar el rol
                ResponseEntity<AuthBasicUserDTO> responseChangeCoordinatorToUser = restTemplate.exchange(
                        "http://AUTH-MICROSERVICE/auth/change-role",
                        HttpMethod.POST,
                        new HttpEntity<>(authBasicUserDTO),
                        AuthBasicUserDTO.class
                );

                // Si el cambio de rol falla, devuelve null
                if (responseChangeCoordinatorToUser.getStatusCode() != HttpStatus.OK) {
                    return null;
                }
            }

            // Asigna el rol de coordinador al nuevo usuario
            Set<RoleEnum> roles = new HashSet<>();
            roles.add(RoleEnum.ROLE_CAF_COORDINATOR);
            AuthBasicUserDTO authBasicUserDTO = new AuthBasicUserDTO();
            authBasicUserDTO.setRoles(roles);
            authBasicUserDTO.setUserName(userEmail);

            // Realiza la llamada al microservicio de autenticación para asignar el rol de coordinador
            ResponseEntity<AuthBasicUserDTO> responseChangeUserToCoordinator = restTemplate.exchange(
                    "http://AUTH-MICROSERVICE/auth/change-role",
                    HttpMethod.POST,
                    new HttpEntity<>(authBasicUserDTO),
                    AuthBasicUserDTO.class
            );

            // Si el cambio de rol falla, devuelve null
            if (responseChangeUserToCoordinator.getStatusCode() != HttpStatus.OK) {
                return null;
            }

            // Actualiza el email del coordinador en el centro de fitness y guarda los cambios
            fitnessCenter.setCoordinatorEmail(userEmail);
            return fitnessCenterRepository.save(fitnessCenter);
        }

        // Devuelve null si el centro de fitness no existe
        return null;
    }

    public int obtainFitnessCenterIdByCoordinatorEmail(String coordinatorEmail){
        FitnessCenter fitnessCenter = fitnessCenterRepository.findByCoordinatorEmail(coordinatorEmail);
        return fitnessCenter.getId();
    }
}
