package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClassesInScheduleForTeacherDTO implements Serializable {
    @JsonProperty("classes")
    private List<ClassForTeacherScheduleDTO> periods;
}
