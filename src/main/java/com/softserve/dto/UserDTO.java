package com.softserve.dto;

import com.softserve.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    private String email;
    private Role role;
}
