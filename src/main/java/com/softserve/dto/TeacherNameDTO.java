package com.softserve.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class TeacherNameDTO implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
}
