package com.uptc.authmicroservice.mapper;

import com.uptc.authmicroservice.dto.AuthUserDTO;
import com.uptc.authmicroservice.entity.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AuthUserMapper {
    AuthUserMapper INSTANCE = Mappers.getMapper(AuthUserMapper.class);

    AuthUser mapUserDTOToUser(AuthUserDTO userDTO);
    AuthUserDTO mapUserToUserDTO(AuthUser user);

    List<AuthUserDTO> mapUserListToUserDTOList(List<AuthUser> userList);
    List<AuthUser> mapUserDTOListToUserList(List<AuthUserDTO> userDTOList);
}