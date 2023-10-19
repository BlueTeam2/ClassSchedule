package com.softserve.mapper;

import com.softserve.dto.RegistrationRequestDTO;
import com.softserve.dto.UserCreateDTO;
import com.softserve.dto.UserDTO;
import com.softserve.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "token", ignore = true)
    User toUser(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "token", ignore = true)
    User toCreateUser(RegistrationRequestDTO registrationDTO);

    List<UserDTO> toUserDTOs(List<User> users);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "token", ignore = true)
    User toUser(UserCreateDTO userCreateDTO);

    UserCreateDTO toUserCreateDTO(User user);

}
