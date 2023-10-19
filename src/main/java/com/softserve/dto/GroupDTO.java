package com.softserve.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO implements Serializable {
    private Long id;
    private Boolean disable;
    private String title;
}
