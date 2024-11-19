package com.uptc.usersmicroservice.controller;

import com.uptc.usersmicroservice.dto.UserBasicDTO;
import com.uptc.usersmicroservice.dto.UserDTO;
import com.uptc.usersmicroservice.entity.User;
import com.uptc.usersmicroservice.mapper.UserMapper;
import com.uptc.usersmicroservice.repository.ProgramRepository;
import com.uptc.usersmicroservice.service.ProgramService;
import com.uptc.usersmicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ProgramService programService;

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> usersList = userService.getAll();
        if (usersList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(UserMapper.INSTANCE.mapUserListToUserDTOList(usersList));
    }

    @GetMapping("/basic/{email}")
    public ResponseEntity<UserBasicDTO> getBasicUserByEmail(@PathVariable("email") String userEmail){
        User user = userService.getUserByEmail(userEmail);
        if (user.getId() == 0) {
            return ResponseEntity.noContent().build();
        }

        UserBasicDTO userBasicDTO = new UserBasicDTO();
        userBasicDTO.setId(user.getId());
        userBasicDTO.setName(user.getName());
        System.out.println(user.getName());
        userBasicDTO.setEmail(user.getEmail());
        return ResponseEntity.ok(userBasicDTO);
    }

    @GetMapping("/basic/user-id/{userId}")
    public ResponseEntity<UserBasicDTO> getBasicUserById(@PathVariable("userId") int userId){
        User user = userService.getUserById(userId);
        if (user.getId() == 0) {
            return ResponseEntity.noContent().build();
        }

        UserBasicDTO userBasicDTO = new UserBasicDTO();
        userBasicDTO.setId(user.getId());
        userBasicDTO.setName(user.getName());
        System.out.println(user.getName());
        userBasicDTO.setEmail(user.getEmail());
        return ResponseEntity.ok(userBasicDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable("email") String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(UserMapper.INSTANCE.mapUserToUserDTO(user));
    }

    @GetMapping("/isUserOldMayor/{email}")
    public ResponseEntity<Boolean> isUserOldMayor(@PathVariable("email") String email){
        boolean isUserOldMayor = userService.isUserOldMayor(email);
        if (isUserOldMayor) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        User userToSave = userService.saveUser(userDTO);
        if (userToSave != null){
            UserDTO savedUserDTO = UserMapper.INSTANCE.mapUserToUserDTO(userToSave);
            savedUserDTO.setUniversityInformation(userToSave.getUniversityInformation());
            savedUserDTO.setMedicalInformation(userToSave.getMedicalInformation());
            savedUserDTO.setEmergencyContact(userToSave.getEmergencyContact());

            return ResponseEntity.ok(savedUserDTO);
        }
        return ResponseEntity.badRequest().build();
    }
}
