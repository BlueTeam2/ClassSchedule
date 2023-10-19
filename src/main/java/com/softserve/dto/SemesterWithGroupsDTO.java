package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class SemesterWithGroupsDTO extends SemesterDTO {
    @JsonProperty("semester_groups")
    private LinkedList<GroupDTO> groups;
}
