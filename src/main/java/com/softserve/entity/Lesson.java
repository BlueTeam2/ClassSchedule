package com.softserve.entity;

import com.softserve.entity.enums.LessonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "lessons")
public class Lesson implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    private int hours;

    @Column(name = "link_to_meeting")
    private String linkToMeeting;

    @Column(name = "subject_for_site")
    @NotNull
    private String subjectForSite;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LessonType lessonType;

    @ManyToOne(targetEntity = Teacher.class)
    @JoinColumn(name = "teacher_id")
    @NotNull
    private Teacher teacher;

    @ManyToOne(targetEntity = Subject.class)
    @JoinColumn(name = "subject_id")
    @NotNull
    private Subject subject;

    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name = "group_id")
    @NotNull
    private Group group;

    @ManyToOne(targetEntity = Semester.class)
    @JoinColumn(name = "semester_id")
    @NotNull
    private Semester semester;

    @Column(name = "grouped", columnDefinition = "boolean default 'false'")
    private boolean grouped = false;
}
