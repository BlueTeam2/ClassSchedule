package com.softserve.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LessonTeacherDTO implements Serializable {
    private LessonsTeacherDateRangeDTO lesson;
}
