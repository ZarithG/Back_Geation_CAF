package com.uptc.usersmicroservice.mapper;

import com.uptc.usersmicroservice.entity.User;
import com.uptc.usersmicroservice.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapUserDTOToUser(UserDTO userDTO);
    UserDTO mapUserToUserDTO(User user);

    List<UserDTO> mapUserListToUserDTOList(List<User> userList);
    List<User> mapUserDTOListToUserList(List<UserDTO> userDTOList);
}