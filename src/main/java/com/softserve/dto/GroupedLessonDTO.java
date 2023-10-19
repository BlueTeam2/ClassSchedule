package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupedLessonDTO {
    @JsonProperty(value = "original_schedule_id")
    private Long scheduleId;
    private List<Long> lessons;
}
