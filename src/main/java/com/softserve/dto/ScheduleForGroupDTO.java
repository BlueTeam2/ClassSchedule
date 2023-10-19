package com.softserve.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ScheduleForGroupDTO implements Serializable {
    private GroupDTO group;
    private List<DaysOfWeekWithClassesForGroupDTO> days;
}
