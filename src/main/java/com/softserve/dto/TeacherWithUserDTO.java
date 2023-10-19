package com.softserve.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TeacherWithUserDTO implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String position;
    private Long userId;
}
