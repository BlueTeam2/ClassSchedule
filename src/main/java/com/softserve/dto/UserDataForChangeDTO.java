package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDataForChangeDTO {
    @JsonProperty(value = "id")
    private Long teacherId;
    @JsonProperty(value = "name")
    private String teacherName;
    @JsonProperty(value = "surname")
    private String teacherSurname;
    @JsonProperty(value = "patronymic")
    private String teacherPatronymic;
    @JsonProperty(value = "position")
    private String teacherPosition;
    @JsonProperty(value = "password")
    private String currentPassword;
    @JsonProperty(value = "new_password")
    private String newPassword;
    @JsonProperty(value = "department")
    private DepartmentDTO teacherDepartmentDTO;
}
