package com.uptc.cafmicroservice.service;

import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.dto.UserResponseDTO;
import com.uptc.cafmicroservice.entity.Consent;
import com.uptc.cafmicroservice.entity.FitnessCenter;
import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.entity.UserResponse;
import com.uptc.cafmicroservice.enums.ConsentTypeEnum;
import com.uptc.cafmicroservice.enums.InscriptionStatusEnum;
import com.uptc.cafmicroservice.mapper.InscriptionMapper;
import com.uptc.cafmicroservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class InscriptionService {
    @Autowired
    InscriptionRepository inscriptionRepository;

    @Autowired
    ConsentRepository consentRepository;

    @Autowired
    UserResponseRepository responseRepository;

    @Autowired
    FitnessCenterRepository fitnessCenterRepository;

    @Autowired
    PARQQuestionRepository parqQuestionRepository;

    @Autowired
    RestTemplate restTemplate;

    public List<Inscription> getAllInscriptionByFitnessCenter(int fitnessCenterId){
        if (fitnessCenterRepository.existsById(fitnessCenterId)){
            return inscriptionRepository.findFitnessCenterInscriptions(fitnessCenterId);

        }else{
            return null;
        }
    }

    public InscriptionDTO inscribeUserInFitnessCenter(InscriptionDTO inscriptionDTO){
        Optional<FitnessCenter> fitnessCenterOptional = fitnessCenterRepository.findById(inscriptionDTO.getFitnessCenterDTO().getId());
        Inscription inscription = new Inscription();

        if (fitnessCenterOptional.isPresent()){
            inscription.setUserId(inscriptionDTO.getUserId());
            inscription.setInscriptionDate(new Date());
            inscription.setInscriptionStatus(InscriptionStatusEnum.PENDING);
            inscription.setFitnessCenter(fitnessCenterOptional.get());
            inscription.setUserResponseList(convertUserResponsesDTOToUserResponses(inscriptionDTO.getUserResponseDTOList()));

            inscription = inscriptionRepository.save(inscription);

            if(inscription.getId() != 0){
                boolean wereConsentFilesSaveSuccessfully = saveConsentFiles(inscription, inscriptionDTO.getFiles(), inscriptionDTO.getConsentTypes());
                if (!wereConsentFilesSaveSuccessfully){
                    return null;
                }
            }
        }else {
            return null;
        }
        return InscriptionMapper.INSTANCE.mapInscriptionToInscriptionDTO(inscription);
    }

    private List<UserResponse> convertUserResponsesDTOToUserResponses(List<UserResponseDTO> userResponseDTOList){
        List<UserResponse> userResponses = new ArrayList<>();
        for (UserResponseDTO  userResponseDTO : userResponseDTOList){
            UserResponse userResponse = new UserResponse();
            userResponse.setParqQuestion(parqQuestionRepository.findById(userResponseDTO.getParqQuestionDTO().getId()));
            userResponse.setQuestionAnswer(userResponseDTO.isQuestionAnswer());
        }
        return userResponses;
    }

    private boolean saveConsentFiles(Inscription inscription, MultipartFile[] files, ConsentTypeEnum[] types) {
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            ConsentTypeEnum type = types[i];

            String filePath = saveFileInConsentTypeFolder(inscription.getInscriptionDate(),
                    inscription.getUserId(), file, type);
            if(!filePath.isEmpty()){
                Consent consent = new Consent();
                consent.setConsentType(type);
                consent.setFilePath(filePath);
                consent.setInscription(inscription);
                consent = consentRepository.save(consent);

                if(consent.getId() == 0){
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

    private String saveFileInConsentTypeFolder(Date inscriptionDate, int userId, MultipartFile file, ConsentTypeEnum consentType){
        String filePath = "";
        switch (consentType) {
            case RISKS:
                filePath = "risk-consents/";
                break;
            case TUTOR:
                filePath = "tutor-consents/";
                break;
            case MEDICAL:
                filePath = "medical-consents/";
                break;
            default:
                return filePath;
        }
        filePath += userId + "_" + inscriptionDate;

        try {
            Path path = Paths.get(filePath);
            Files.write(path, file.getBytes());
        }catch (IOException e){
            return "";
        }
        return filePath;
    }
}