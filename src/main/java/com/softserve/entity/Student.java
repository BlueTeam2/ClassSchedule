package com.softserve.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "students")
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 35, message = "Name must be between 2 and 35 characters long")
    @Column(length = 35, nullable = false)
    @CsvBindByName
    private String name;

    @NotEmpty(message = "Surname cannot be empty")
    @Size(min = 2, max = 35, message = "Surname must be between 2 and 35 characters long")
    @Column(length = 35, nullable = false)
    @CsvBindByName
    private String surname;

    @NotEmpty(message = "Patronymic cannot be empty")
    @Size(min = 2, max = 35, message = "Patronymic must be between 2 and 35 characters long")
    @Column(length = 35, nullable = false)
    @CsvBindByName
    private String patronymic;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @NotNull(message = "Group cannot be null")
    @CsvRecurse
    private Group group;
}
