package com.uptc.cafmicroservice.service;

import com.uptc.cafmicroservice.dto.*;
import com.uptc.cafmicroservice.entity.FitnessCenter;
import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.entity.UserResponse;
import com.uptc.cafmicroservice.enums.InscriptionStatusEnum;
import com.uptc.cafmicroservice.mapper.FitnessCenterMapper;
import com.uptc.cafmicroservice.mapper.InscriptionMapper;
import com.uptc.cafmicroservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class InscriptionService {
    // Inyección de repositorios y servicios necesarios para gestionar inscripciones, preguntas PAR-Q y comunicación con otros microservicios
    @Autowired
    InscriptionRepository inscriptionRepository;

    @Autowired
    UserResponseRepository responseRepository;

    @Autowired
    FitnessCenterRepository fitnessCenterRepository;

    @Autowired
    PARQQuestionRepository parqQuestionRepository;

    @Autowired
    RestTemplate restTemplate;

    /**
     * Obtiene todas las inscripciones asociadas a un centro de fitness específico.
     *
     * @param email Correo electrónico del coordinador actual del CAF
     * @return Lista de objetos InscriptionDTO o null si el centro de fitness no existe.
     */
    public List<UserInscriptionDTO> getAllInscriptionByFitnessCenter(String email) {
        FitnessCenter fitnessCenter = fitnessCenterRepository.findByCoordinatorEmail(email);
        List<UserInscriptionDTO> inscriptionDTOList = new ArrayList<>();
        if (fitnessCenter.getId() != 0) {
            List<Inscription> inscriptionList = inscriptionRepository.findFitnessCenterInscriptions(email);
            for (Inscription inscription : inscriptionList) {
                UserAllDataDTO userAllDataDTO = searchAllUserDataByUserId(inscription.getUserId());
                UserInscriptionDTO userInscriptionDTO = new UserInscriptionDTO();

                if(userAllDataDTO != null){
                    userInscriptionDTO.setInscriptionId(inscription.getId());
                    userInscriptionDTO.setUserAllDataDTO(userAllDataDTO);
                    userInscriptionDTO.setInscriptionDate(inscription.getInscriptionDate());
                    userInscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
                    inscriptionDTOList.add(userInscriptionDTO);
                }
            }
            // Convierte cada inscripción en un objeto DTO y lo agrega a la lista
            return inscriptionDTOList;
        } else {
            return null;
        }
    }

    /**
     * Obtiene todas las inscripciones activas de un centro de fitness específico.
     *
     * @param email Correo electrónico del coordinador actual del CAF
     * @return Lista de objetos InscriptionDTO o null si el centro de fitness no existe.
     */
    public List<UserInscriptionDTO> getAllActiveInscriptionByFitnessCenter(String email) {
        FitnessCenter fitnessCenter = fitnessCenterRepository.findByCoordinatorEmail(email);
        List<UserInscriptionDTO> inscriptionDTOList = new ArrayList<>();
        if (fitnessCenter.getId() != 0) {
            List<Inscription> inscriptionList = inscriptionRepository.findFitnessCenterActiveInscriptions(email);
            for (Inscription inscription : inscriptionList) {
                UserAllDataDTO userAllDataDTO = searchAllUserDataByUserId(inscription.getUserId());
                UserInscriptionDTO userInscriptionDTO = new UserInscriptionDTO();

                if(userAllDataDTO != null){
                    userInscriptionDTO.setInscriptionId(inscription.getId());
                    userInscriptionDTO.setUserAllDataDTO(userAllDataDTO);
                    userInscriptionDTO.setInscriptionDate(inscription.getInscriptionDate());
                    userInscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
                    inscriptionDTOList.add(userInscriptionDTO);
                }
            }
            // Convierte cada inscripción en un objeto DTO y lo agrega a la lista
            return inscriptionDTOList;
        } else {
            return null;
        }
    }

    public Inscription getInscriptionById(int id) {
        return inscriptionRepository.findById(id).orElse(null);
    }


    /**
     * Obtiene todas las inscripciones asociadas a un usuario específico basado en su email.
     *
     * @param email Email del usuario.
     * @return Lista de objetos InscriptionDTO o null si el usuario no existe.
     */
    public List<InscriptionDTO> getAllUserActiveInscriptions(String email) {
        int userId = searchUserByEmail(email); // Busca el ID del usuario por su email

        if (userId != 0) {
            List<Inscription> inscriptionList = inscriptionRepository.findAllUserActiveInscriptions(userId);
            // Convierte cada inscripción en un objeto DTO y lo agrega a la lista
            return convertInscriptionListToInscriptionDTOList(inscriptionList);
        }
        return null;
    }

    /**
     * Obtiene todas las inscripciones de un usuario a un CAF específico que sean activas (ACCEPTED), inactivas (INACTIVE) o pendientes (PENDING)
     *
     * @param email Email del usuario.
     * @param fitnessCenterId Id del CAF
     * @return Lista de objetos InscriptionDTO o null si el usuario no existe.
     */
    public List<InscriptionDTO> getAllUserInscriptionsToCAF(String email, int fitnessCenterId) {
        int userId = searchUserByEmail(email); // Busca el ID del usuario por su email
        System.out.println("UserID" + userId);
        if (userId != 0) {
            List<Inscription> inscriptionList = inscriptionRepository.findAllUserInscriptionsToCaf(userId, fitnessCenterId);
            // Convierte cada inscripción en un objeto DTO y lo agrega a la lista
            return convertInscriptionListToInscriptionDTOList(inscriptionList);
        }
        return null;
    }

    public List<InscriptionDTO> findAllUserInscriptions(String email){
        int userId = searchUserByEmail(email);

        if (userId != 0) {
            List<Inscription> inscriptionList = inscriptionRepository.findAllUserInscriptions(userId);

            // Convierte cada inscripción en un objeto DTO y lo agrega a la lista
            return convertInscriptionListToInscriptionDTOList(inscriptionList);
        }
        return null;
    }

    /**
     * Inscribe a un usuario en un centro de fitness y guarda la inscripción junto con las respuestas a preguntas PAR-Q.
     *
     * @param inscriptionDTO Objeto DTO con la información de inscripción.
     * @param email          Email del usuario que se desea inscribir.
     * @return Objeto InscriptionDTO con la inscripción creada o null si falla.
     */
    public InscriptionDTO inscribeUserInFitnessCenter(InscriptionDTO inscriptionDTO, String email) {
        Optional<FitnessCenter> fitnessCenterOptional = fitnessCenterRepository.findById(inscriptionDTO.getFitnessCenterDTO().getId());
        Inscription inscription = new Inscription();

        if (fitnessCenterOptional.isPresent()) {
            int userId = searchUserByEmail(email);

            if (userId != 0) {
                // Configura la inscripción con los datos del usuario y el centro de fitness
                inscription.setUserId(userId);
                inscription.setInscriptionDate(new Date());
                inscription.setInscriptionStatus(InscriptionStatusEnum.PENDING);
                inscription.setFitnessCenter(fitnessCenterOptional.get());
                inscription.setUserResponseList(convertUserResponsesDTOToUserResponses(inscriptionDTO.getUserResponseDTOList()));

                inscription = inscriptionRepository.save(inscription); // Guarda la inscripción en la base de datos
            } else {
                return null;
            }
        } else {
            return null;
        }
        return InscriptionMapper.INSTANCE.mapInscriptionToInscriptionDTO(inscription); // Devuelve la inscripción convertida a DTO
    }

    /**
     * Busca el ID de un usuario en el microservicio de usuarios dado su email.
     *
     * @param email Email del usuario.
     * @return El ID del usuario o 0 si no existe.
     */
    private int searchUserByEmail(String email) {
        ResponseEntity<UserBasicDTO> response = restTemplate.exchange(
                "http://USERS-MICROSERVICE/user/basic/" + email,
                HttpMethod.GET,
                new HttpEntity<>(null),
                UserBasicDTO.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            if (response.getBody() != null) {
                return response.getBody().getId();
            }
        }
        return 0; // Retorna 0 si no se encuentra el usuario
    }

    private UserAllDataDTO searchAllUserDataByUserId(int userId) {
        ResponseEntity<UserAllDataDTO> response = restTemplate.exchange(
                "http://USERS-MICROSERVICE/user/all-user-data/user-id/" + userId,
                HttpMethod.GET,
                new HttpEntity<>(null),
                UserAllDataDTO.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            if (response.getBody() != null) {
                return response.getBody();
            }
        }
        return null;
    }

    /**
     * Cambia el estado de una inscripción a un nuevo estado (Aceptado, Rechazado, Inactivo, etc.).
     *
     * @param inscriptionId     ID de la inscripción.
     * @param inscriptionStatus Nuevo estado de la inscripción.
     * @return La inscripción actualizada o null si no existe.
     */
    public Inscription changeUserInscription(int inscriptionId, InscriptionStatusEnum inscriptionStatus) {
        Inscription inscription = inscriptionRepository.findInscriptionById(inscriptionId);
        if (inscription.getId() != 0) {
            inscription.setInscriptionStatus(inscriptionStatus); // Cambia el estado
            inscription = inscriptionRepository.save(inscription); // Guarda los cambios
        } else {
            return null;
        }
        return inscription;
    }

    /**
     * Convierte una lista de objetos UserResponseDTO a entidades UserResponse y los guarda en la base de datos.
     *
     * @param userResponseDTOList Lista de DTOs con respuestas a preguntas PAR-Q.
     * @return Lista de entidades UserResponse.
     */
    private List<UserResponse> convertUserResponsesDTOToUserResponses(List<UserResponseDTO> userResponseDTOList) {
        List<UserResponse> userResponses = new ArrayList<>();
        for (UserResponseDTO userResponseDTO : userResponseDTOList) {
            UserResponse userResponse = new UserResponse();
            userResponse.setParqQuestion(parqQuestionRepository.findById(userResponseDTO.getParqQuestionDTO().getId())); // Obtiene la pregunta PAR-Q
            userResponse.setQuestionAnswer(userResponseDTO.isQuestionAnswer()); // Asigna la respuesta
            userResponses.add(userResponse);
        }
        responseRepository.saveAll(userResponses); // Guarda todas las respuestas en la base de datos
        return userResponses;
    }


    /**
     * Convierte una lista de objetos Inscription en una lista de objetos InscriptionDTO.
     *
     * @param inscriptionList Lista de objetos Inscription a convertir.
     * @return Lista de objetos InscriptionDTO resultante.
     */
    private List<InscriptionDTO> convertInscriptionListToInscriptionDTOList(List<Inscription> inscriptionList) {
        // Crear una nueva lista para almacenar los objetos InscriptionDTO.
        List<InscriptionDTO> inscriptionDTOList = new ArrayList<>();

        // Iterar sobre cada objeto Inscription en la lista de entrada.
        for (Inscription inscription : inscriptionList) {
            // Crear una nueva instancia de InscriptionDTO.
            InscriptionDTO inscriptionDTO = new InscriptionDTO();

            // Mapear los atributos del objeto Inscription al objeto InscriptionDTO.
            inscriptionDTO.setId(inscription.getId()); // Asignar el ID.
            inscriptionDTO.setInscriptionDate(inscription.getInscriptionDate()); // Asignar la fecha de inscripción.
            inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus()); // Asignar el estado de inscripción.

            // Mapear el objeto FitnessCenter relacionado al objeto FitnessCenterDTO usando un mapper.
            inscriptionDTO.setFitnessCenterDTO(FitnessCenterMapper.INSTANCE.mapFitnessCenterToFitnessCenterDTO(inscription.getFitnessCenter()));

            // Agregar el objeto InscriptionDTO a la lista resultante.
            inscriptionDTOList.add(inscriptionDTO);
        }

        // Devolver la lista convertida de InscriptionDTOs.
        return inscriptionDTOList;
    }

}