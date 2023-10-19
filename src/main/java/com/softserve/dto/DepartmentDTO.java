package com.softserve.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDTO implements Serializable {
    private Long id;
    private String name;
    private boolean disable;
}
