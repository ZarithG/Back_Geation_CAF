package com.uptc.usersmicroservice.service;

import com.uptc.usersmicroservice.dto.UserDTO;
import com.uptc.usersmicroservice.entity.EmergencyContact;
import com.uptc.usersmicroservice.entity.MedicalInformation;
import com.uptc.usersmicroservice.entity.UniversityInformation;
import com.uptc.usersmicroservice.entity.User;
import com.uptc.usersmicroservice.enums.UserTypeEnum;
import com.uptc.usersmicroservice.mapper.UserMapper;
import com.uptc.usersmicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UniversityInformationService universityInformationService;

    @Autowired
    EmergencyContactService emergencyContactService;

    @Autowired
    MedicalInformationService medicalInformationService;

    @Autowired
    RestTemplate restTemplate;

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.findById(id).orElse(null
        );
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    private UniversityInformation saveUserUniversityInformation(UniversityInformation actUniversityInformation, UserDTO userDTO){
        if (actUniversityInformation == null){
            return universityInformationService.save(
                    new UniversityInformation(
                            0,
                            userDTO.getUniversityInformation().getActualSemester(),
                            userDTO.getUniversityInformation().getProgram()
                    ));
        }
        return universityInformationService.save(
                new UniversityInformation(
                        actUniversityInformation.getId(),
                        userDTO.getUniversityInformation().getActualSemester(),
                        userDTO.getUniversityInformation().getProgram()
                ));
    }

    private EmergencyContact saveUserEmergencyContact(EmergencyContact emergencyContact, UserDTO userDTO){
        if (emergencyContact == null){
            return emergencyContactService.save(userDTO.getEmergencyContact());
        }
        userDTO.getEmergencyContact().setId(emergencyContact.getId());
        return emergencyContactService.save(userDTO.getEmergencyContact());
    }

    private MedicalInformation saveUserMedicalInformation(MedicalInformation medicalInformation, UserDTO userDTO){
        if (medicalInformation == null){
            return medicalInformationService.save(userDTO.getMedicalInformation());
        }
        userDTO.getMedicalInformation().setId(medicalInformation.getId());
        return medicalInformationService.save(userDTO.getMedicalInformation());
    }

    public User saveUser(UserDTO userDTO) {
        User existingUser = getUserByEmail(userDTO.getEmail());
        User userToAdd = UserMapper.INSTANCE.mapUserDTOToUser(userDTO);
        userToAdd.setUserType(UserTypeEnum.valueOf(userDTO.getUserType()));

        if (existingUser != null) {
            userToAdd.setId(existingUser.getId());
            userToAdd.setUniversityInformation(saveUserUniversityInformation(existingUser.getUniversityInformation(),
                    userDTO));
            userToAdd.setEmergencyContact(saveUserEmergencyContact(existingUser.getEmergencyContact(),
                    userDTO));
            userToAdd.setMedicalInformation(saveUserMedicalInformation(existingUser.getMedicalInformation(),
                    userDTO));

            if (existingUser.getUniversityCode() == null) {
                HttpEntity<UserDTO> requestVerifyUser = new HttpEntity<>(userDTO);
                ResponseEntity<UserDTO> responseFromVerifyUser = restTemplate.exchange(
                        "http://AUTH-MICROSERVICE/auth/verify/user",
                        HttpMethod.POST,
                        requestVerifyUser,
                        UserDTO.class
                );
            }
        }
        return userRepository.save(userToAdd);
    }
}
