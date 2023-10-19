package com.softserve.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class SubjectDTO {
    private Long id;
    private String name;
    private boolean disable;
}
