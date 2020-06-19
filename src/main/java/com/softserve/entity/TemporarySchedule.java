package com.softserve.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.entity.enums.LessonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "temporary_schedule")
public class TemporarySchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @NotNull(message = "Schedule date cannot be empty")
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "teacher_for_site")
    private String teacherForSite;

    @Column(name = "subject_for_site")
    private String subjectForSite;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;


    @Column(name ="lesson_id")
    private Long lessonId;

    @ManyToOne(targetEntity = Teacher.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(targetEntity = Subject.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;

    @ManyToOne(targetEntity = Group.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    @ManyToOne(targetEntity = Semester.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id", nullable = true)
    private Semester semester;

    @ManyToOne(targetEntity = Room.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;

    @ManyToOne(targetEntity = Period.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "period_id", nullable = true)
    private Period period;


    @Column(name = "grouped",  columnDefinition = "boolean default 'false'")
    private boolean grouped = false;

    @Column(name = "vacation",  columnDefinition = "boolean default 'false'")
    private boolean vacation = false;
}