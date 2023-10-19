package com.softserve.entity;

import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@FilterDef(name = "departmentDisableFilter", parameters = {
        @ParamDef(name = "disable", type = "boolean"),
})
@Filter(name = "departmentDisableFilter", condition = "disable = :disable")
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "boolean default 'false'")
    private boolean disable = false;
}
