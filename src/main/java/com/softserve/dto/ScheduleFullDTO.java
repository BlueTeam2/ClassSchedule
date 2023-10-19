package com.softserve.dto;

import lombok.*;

import java.util.List;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ScheduleFullDTO implements Serializable {
    private SemesterDTO semester;
    private List<ScheduleForGroupDTO> schedule;
}
