package com.softserve.entity;

import com.softserve.entity.enums.EvenOdd;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "schedules")
public class Schedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 35, nullable = false)
    @NotNull
    private DayOfWeek dayOfWeek;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EvenOdd evenOdd;

    @ManyToOne(targetEntity = Room.class)
    @JoinColumn(name = "room_id")
    @NotNull
    @Where(clause = "disable = false")
    private Room room;

    @ManyToOne(targetEntity = Period.class)
    @JoinColumn(name = "period_id")
    @NotNull
    private Period period;

    @ManyToOne(targetEntity = Lesson.class)
    @JoinColumn(name = "lesson_id")
    @NotNull
    private Lesson lesson;
}
