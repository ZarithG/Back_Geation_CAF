package com.uptc.cafmicroservice;

import com.uptc.cafmicroservice.dto.FitnessCenterDTO;
import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.dto.PARQQuestionDTO;
import com.uptc.cafmicroservice.dto.UserResponseDTO;
import com.uptc.cafmicroservice.entity.FitnessCenter;
import com.uptc.cafmicroservice.entity.PARQQuestion;
import com.uptc.cafmicroservice.entity.UserResponse;
import com.uptc.cafmicroservice.enums.InscriptionStatusEnum;
import com.uptc.cafmicroservice.repository.FitnessCenterRepository;
import com.uptc.cafmicroservice.repository.InscriptionRepository;
import com.uptc.cafmicroservice.repository.PARQQuestionRepository;
import com.uptc.cafmicroservice.repository.UserResponseRepository;
import com.uptc.cafmicroservice.service.InscriptionService;
import org.junit.jupiter.api.Assertions;
import com.uptc.cafmicroservice.dto.UserBasicDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class InscriptionServiceTest {
    @Mock
    private InscriptionRepository inscriptionRepository;

    @Mock
    private FitnessCenterRepository fitnessCenterRepository;

    @Mock
    private PARQQuestionRepository parqQuestionRepository;

    @Mock
    UserResponseRepository userResponseRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private InscriptionService inscriptionService;

    @DisplayName("Prueba del método de inscripción de usuarios en un CAF específico")
    @Test
    public void createInscriptionFromUserToCAF(){
        InscriptionDTO inscriptionDTO = new InscriptionDTO();
        FitnessCenterDTO fitnessCenterDTO = new FitnessCenterDTO();
        fitnessCenterDTO.setId(2);
        inscriptionDTO.setFitnessCenterDTO(fitnessCenterDTO);
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();

        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(1,""), true));
        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(2,""), true));
        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(3,""), true));
        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(4,""), true));
        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(5,""), true));
        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(6,""), true));
        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(7,""), true));
        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(8,""), true));
        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(9,""), true));
        userResponseDTOList.add(new UserResponseDTO(0, new PARQQuestionDTO(10,""), true));
        inscriptionDTO.setUserResponseDTOList(userResponseDTOList);

        Mockito.when(inscriptionRepository.save(Mockito.any())).thenReturn(inscriptionDTO);
        Mockito.when(fitnessCenterRepository.findById(2)).thenReturn(Optional.of(new FitnessCenter()));
        Mockito.when(parqQuestionRepository.findById(1)).thenReturn(new PARQQuestion());
        Mockito.when(parqQuestionRepository.findById(2)).thenReturn(new PARQQuestion());
        Mockito.when(parqQuestionRepository.findById(3)).thenReturn(new PARQQuestion());
        Mockito.when(parqQuestionRepository.findById(4)).thenReturn(new PARQQuestion());
        Mockito.when(parqQuestionRepository.findById(5)).thenReturn(new PARQQuestion());
        Mockito.when(parqQuestionRepository.findById(6)).thenReturn(new PARQQuestion());
        Mockito.when(parqQuestionRepository.findById(7)).thenReturn(new PARQQuestion());
        Mockito.when(parqQuestionRepository.findById(8)).thenReturn(new PARQQuestion());
        Mockito.when(parqQuestionRepository.findById(9)).thenReturn(new PARQQuestion());
        Mockito.when(parqQuestionRepository.findById(10)).thenReturn(new PARQQuestion());

        List<UserResponse> userResponses = new ArrayList<>();
        for (UserResponseDTO userResponseDTO : userResponseDTOList) {
            UserResponse userResponse = new UserResponse();
            userResponse.setParqQuestion(parqQuestionRepository.findById(userResponseDTO.getParqQuestionDTO().getId())); // Obtiene la pregunta PAR-Q
            userResponse.setQuestionAnswer(userResponseDTO.isQuestionAnswer()); // Asigna la respuesta
            userResponses.add(userResponse);
        }
        Mockito.when(userResponseRepository.saveAll(userResponses)).thenReturn(userResponses);

        UserBasicDTO userBasicDTO = new UserBasicDTO();
        userBasicDTO.setId(1); // Configura un ID de usuario simulado
        ResponseEntity<UserBasicDTO> responseEntity = new ResponseEntity<>(userBasicDTO, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.eq("http://USERS-MICROSERVICE/user/basic/juan.archila04@uptc.edu.co"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(UserBasicDTO.class)
        )).thenReturn(responseEntity);

        final InscriptionDTO inscriptionDTOResult = inscriptionService.inscribeUserInFitnessCenter(inscriptionDTO, "juan.archila04@uptc.edu.co");
        Assertions.assertEquals(inscriptionDTOResult.getId(),1);
        Assertions.assertEquals(1, inscriptionDTOResult.getUserId());
        Assertions.assertEquals(inscriptionDTO.getInscriptionStatus(), InscriptionStatusEnum.PENDING);
    }
}
