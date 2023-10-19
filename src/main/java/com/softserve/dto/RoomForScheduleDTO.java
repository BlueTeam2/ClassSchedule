package com.softserve.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RoomForScheduleDTO implements Serializable {
    private Long id;
    private String name;
}
