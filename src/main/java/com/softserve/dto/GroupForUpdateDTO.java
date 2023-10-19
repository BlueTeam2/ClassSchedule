package com.softserve.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupForUpdateDTO {
    private Long id;
    private String title;
    private boolean disable;
}
