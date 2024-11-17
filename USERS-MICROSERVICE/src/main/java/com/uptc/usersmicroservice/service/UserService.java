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
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.time.ZoneId;

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
    CityService cityService;

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

    public boolean isUserOldMayor(String email){
        User user = getUserByEmail(email);
        LocalDate birthLocalDate = user.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthLocalDate, currentDate).getYears();
        return age >= 18;
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
        EmergencyContact actEmergencyContact = userDTO.getEmergencyContact();
        actEmergencyContact.setCity(cityService.findCityById(userDTO.getEmergencyContact().getCity().getId()));
        if (emergencyContact != null){
            userDTO.getEmergencyContact().setId(emergencyContact.getId());
        }
        return emergencyContactService.save(actEmergencyContact);
    }

    private MedicalInformation saveUserMedicalInformation(MedicalInformation medicalInformation, UserDTO userDTO){
        if (medicalInformation != null){
            userDTO.getMedicalInformation().setId(medicalInformation.getId());
        }
        return medicalInformationService.save(userDTO.getMedicalInformation());
    }

    public User saveUser(UserDTO userDTO) {
        User existingUser = getUserByEmail(userDTO.getEmail());
        User userToAdd = UserMapper.INSTANCE.mapUserDTOToUser(userDTO);

        if(userDTO.getUserType() != null) {
            userToAdd.setUserType(userDTO.getUserType());
        }

        userToAdd.setCity(cityService.findCityById(4));

        if (existingUser.getId() != 0) {
            userToAdd.setName(existingUser.getName());
            userToAdd.setId(existingUser.getId());
            userToAdd.setUniversityInformation(saveUserUniversityInformation(existingUser.getUniversityInformation(),
                    userDTO));
            userToAdd.setEmergencyContact(saveUserEmergencyContact(existingUser.getEmergencyContact(),
                    userDTO));
            userToAdd.setMedicalInformation(saveUserMedicalInformation(existingUser.getMedicalInformation(),
                    userDTO));
            userToAdd.setCity(cityService.findCityById(userDTO.getCityId()));

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
