package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupWithLessonIdDTO {
    private GroupDTO groupDTO;
    @JsonProperty(value = "lesson_id")
    private Long lessonId;
}
