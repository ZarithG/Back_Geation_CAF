package com.uptc.cafmicroservice.service;

import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.dto.UserBasicDTO;
import com.uptc.cafmicroservice.dto.UserResponseDTO;
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

    public List<InscriptionDTO> getAllInscriptionByFitnessCenter(int fitnessCenterId){
        if (fitnessCenterRepository.existsById(fitnessCenterId)){
            List<InscriptionDTO> inscriptionDTOList = new ArrayList<>();
            List<Inscription> inscriptionList = inscriptionRepository.findAllUserInscriptions(fitnessCenterId);
            for (Inscription inscription : inscriptionList){
                InscriptionDTO inscriptionDTO = new InscriptionDTO();
                inscriptionDTO.setId(inscription.getId());
                inscriptionDTO.setInscriptionDate(inscription.getInscriptionDate());
                inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
                inscriptionDTO.setFitnessCenterDTO(FitnessCenterMapper.INSTANCE.mapFitnessCenterToFitnessCenterDTO(inscription.getFitnessCenter()));
                inscriptionDTOList.add(inscriptionDTO);
            }
            return inscriptionDTOList;




            //return inscriptionRepository.findFitnessCenterInscriptions(fitnessCenterId);
        }else{
            return null;
        }
    }

    public List<InscriptionDTO> getAllUserInscriptions(String email){
        int userId = searchUserByEmail(email);

        if(userId != 0){
            List<InscriptionDTO> inscriptionDTOList = new ArrayList<>();
            List<Inscription> inscriptionList = inscriptionRepository.findAllUserInscriptions(userId);
            for (Inscription inscription : inscriptionList){
                InscriptionDTO inscriptionDTO = new InscriptionDTO();
                inscriptionDTO.setId(inscription.getId());
                inscriptionDTO.setInscriptionDate(inscription.getInscriptionDate());
                inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
                inscriptionDTO.setFitnessCenterDTO(FitnessCenterMapper.INSTANCE.mapFitnessCenterToFitnessCenterDTO(inscription.getFitnessCenter()));
                inscriptionDTOList.add(inscriptionDTO);
            }
            return inscriptionDTOList;
        }
        return null;
    }

    public InscriptionDTO inscribeUserInFitnessCenter(InscriptionDTO inscriptionDTO, String email){
        Optional<FitnessCenter> fitnessCenterOptional = fitnessCenterRepository.findById(inscriptionDTO.getFitnessCenterDTO().getId());
        Inscription inscription = new Inscription();

        if (fitnessCenterOptional.isPresent()){
            int userId = searchUserByEmail(email);

            if (userId != 0){
                inscription.setUserId(userId);
                inscription.setInscriptionDate(new Date());
                inscription.setInscriptionStatus(InscriptionStatusEnum.PENDING);
                inscription.setFitnessCenter(fitnessCenterOptional.get());
                inscription.setUserResponseList(convertUserResponsesDTOToUserResponses(inscriptionDTO.getUserResponseDTOList()));

                inscription = inscriptionRepository.save(inscription);
            }else{
                return null;
            }
        }else {
            return null;
        }
        return InscriptionMapper.INSTANCE.mapInscriptionToInscriptionDTO(inscription);
    }

    private int searchUserByEmail(String email){
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
        return 0;
    }

    public Inscription changeUserInscription(int inscriptionId, InscriptionStatusEnum inscriptionStatus){
        Inscription inscription = inscriptionRepository.findInscriptionById(inscriptionId);
        if(inscription.getId() != 0){
            inscription.setInscriptionStatus(inscriptionStatus);
            inscription = inscriptionRepository.save(inscription);
        }else{
            return null;
        }
        return inscription;
    }

    private List<UserResponse> convertUserResponsesDTOToUserResponses(List<UserResponseDTO> userResponseDTOList) {
        List<UserResponse> userResponses = new ArrayList<>();
        for (UserResponseDTO userResponseDTO : userResponseDTOList) {
            UserResponse userResponse = new UserResponse();
            userResponse.setParqQuestion(parqQuestionRepository.findById(userResponseDTO.getParqQuestionDTO().getId()));
            userResponse.setQuestionAnswer(userResponseDTO.isQuestionAnswer());
            userResponses.add(userResponse);
        }
        responseRepository.saveAll(userResponses);
        return userResponses;
    }
}