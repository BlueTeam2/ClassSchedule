package com.softserve.dto;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private GroupDTO group;
}
