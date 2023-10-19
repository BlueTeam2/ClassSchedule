package com.softserve.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinTeacherWithUserDTO {
    private Long teacherId;
    private Long userId;
}
