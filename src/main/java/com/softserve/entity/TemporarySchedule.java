package com.softserve.entity;

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
    private Long id;

    @NotNull(message = "Schedule date cannot be empty")
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "link_to_meeting")
    private String linkToMeeting;

    @Column(name = "subject_for_site")
    private String subjectForSite;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;


    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(targetEntity = Teacher.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(targetEntity = Subject.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(targetEntity = Group.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(targetEntity = Semester.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ManyToOne(targetEntity = Room.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(targetEntity = Period.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "period_id")
    private Period period;


    @Column(name = "grouped", columnDefinition = "boolean default 'false'")
    private boolean grouped = false;

    @Column(name = "vacation", columnDefinition = "boolean default 'false'")
    private boolean vacation = false;

    @Column(name = "notification", columnDefinition = "boolean default 'false'")
    private boolean notification = false;
}
