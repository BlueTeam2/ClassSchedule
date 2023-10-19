package com.softserve.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class StudentWithoutGroupDTO {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
}
