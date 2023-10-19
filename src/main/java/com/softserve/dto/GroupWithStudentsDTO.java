package com.softserve.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupWithStudentsDTO {
    private Long id;
    private String title;
    private List<StudentWithoutGroupDTO> students;
}
