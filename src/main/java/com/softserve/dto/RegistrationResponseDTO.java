package com.softserve.dto;

import com.softserve.entity.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegistrationResponseDTO {
    private String email;
    private Role role;
}
