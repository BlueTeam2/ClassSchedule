package com.softserve.dto;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(value = "archive_schedule")
public class ScheduleFullForArchiveDTO {
    private SemesterDTO semesterDTO;
    private List<ScheduleForGroupDTO> schedule;
    private List<TemporaryScheduleForArchiveDTO> temporarySchedule;
}
