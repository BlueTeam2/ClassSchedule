package com.softserve.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

@NamedQuery(
        name = "findName",
        query = "from Period p where p.name= :name"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "periods")
public class Period implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start time cannot be empty")
    @Column(name = "start_time")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be empty")
    @Column(name = "end_time")
    private LocalTime endTime;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 1, max = 35, message = "Name must be between 1 and 35 characters long")
    @Column(length = 35, nullable = false, unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Period period = (Period) o;
        return name.equals(period.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
