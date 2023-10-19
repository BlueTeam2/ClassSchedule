package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LessonsInRoomScheduleDTO implements Serializable {

    private List<LessonsListInRoomScheduleDTO> lessons;
    @JsonProperty("class_id")
    private Long classId;

    @JsonProperty("class_name")
    private String className;

}
