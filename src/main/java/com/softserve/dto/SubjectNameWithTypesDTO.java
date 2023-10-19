package com.softserve.dto;

import com.softserve.entity.enums.LessonType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode
public class SubjectNameWithTypesDTO {
    private Long id;
    private String name;
    @EqualsAndHashCode.Exclude
    private Set<LessonType> types;
}
