package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.dto.enums.ImportSaveStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TeacherImportDTO implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String position;
    private String email;
    @JsonProperty("department")
    private DepartmentDTO departmentDTO;
    private ImportSaveStatus importSaveStatus;
}
