package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class SemesterDTO implements Serializable {
    private Long id;
    private String description;
    private int year;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate endDay;
    private boolean currentSemester;
    private boolean defaultSemester;
    @JsonProperty("semester_days")
    private TreeSet<DayOfWeek> daysOfWeek;
    @JsonProperty("semester_classes")
    private LinkedHashSet<PeriodDTO> periods;
    private boolean disable;
}
