package com.softserve.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class LessonInScheduleByWeekDTO implements Serializable {
    private LessonsInScheduleDTO even;
    private LessonsInScheduleDTO odd;

}
