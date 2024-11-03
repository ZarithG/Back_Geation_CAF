package com.uptc.usersmicroservice.controller;

import com.uptc.usersmicroservice.dto.UserDTO;
import com.uptc.usersmicroservice.entity.User;
import com.uptc.usersmicroservice.mapper.UserMapper;
import com.uptc.usersmicroservice.repository.UserRepository;
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
    UserRepository userRepository;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserMapper.INSTANCE.mapUserToUserDTO(user));
    }

//    @GetMapping("/{email}")
//    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable("email") String email) {
//        User user = userService.getUserByEmail(email);
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(UserMapper.INSTANCE.mapUserToUserDTO(user));
//    }

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(userDTO.getEmail());

        User userToAdd = UserMapper.INSTANCE.mapUserDTOToUser(userDTO);
        if (existingUser != null) {
            userToAdd.setId(existingUser.getId());
            HttpEntity<UserDTO> requestVerifyUser = new HttpEntity<>(userDTO);

            ResponseEntity<UserDTO> responseFromVerifyUser = restTemplate.exchange(
                    "http://AUTH-MICROSERVICE/auth/verify/user",
                    HttpMethod.POST,
                    requestVerifyUser,
                    UserDTO.class
            );

//            if (!responseFromVerifyUser.getStatusCode().equals(HttpStatus.ACCEPTED)){
//
//            }
        }

        User user = userRepository.save(userToAdd);
        return ResponseEntity.ok(UserMapper.INSTANCE.mapUserToUserDTO(user));
    }
}
