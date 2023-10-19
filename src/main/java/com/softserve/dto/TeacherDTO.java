package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TeacherDTO implements Serializable {
    private Long id;
    private Boolean disable;
    private String name;
    private String surname;
    private String patronymic;
    private String position;
    private String email;
    @JsonProperty("department")
    private DepartmentDTO departmentDTO;
}
