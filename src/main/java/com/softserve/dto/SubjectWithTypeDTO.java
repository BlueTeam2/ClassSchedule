package com.softserve.dto;

import com.softserve.entity.Subject;
import com.softserve.entity.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SubjectWithTypeDTO {

    private Subject subject;
    private LessonType lessonType;
}
