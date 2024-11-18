package com.uptc.cafmicroservice.service;

import com.uptc.cafmicroservice.dto.UserResponseDTO;
import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.entity.UserResponse;
import com.uptc.cafmicroservice.mapper.PARQQuestionMapper;
import com.uptc.cafmicroservice.repository.PARQQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserResponsesService {
    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private PARQQuestionRepository parqQuestionRepository;

    public List<UserResponseDTO> getUserResponsesByInscriptionId(int inscriptionId) {
        Inscription inscription = inscriptionService.inscriptionRepository.findInscriptionById(inscriptionId);
        if(inscription != null){
            return convertUserResponseToUserResponseDTO(inscription.getUserResponseList());
        }
        return null;
    }

    private List<UserResponseDTO> convertUserResponseToUserResponseDTO(List<UserResponse> userResponseList) {
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        for (UserResponse userResponse : userResponseList) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setParqQuestionDTO(PARQQuestionMapper.INSTANCE.mapPARQQuestionToPARQQuestionDTO(parqQuestionRepository.findById(userResponse.getParqQuestion().getId()))); // Obtiene la pregunta PAR-Q
            userResponseDTO.setQuestionAnswer(userResponse.isQuestionAnswer());
            userResponseDTOList.add(userResponseDTO);
        }
        return userResponseDTOList;
    }
}
