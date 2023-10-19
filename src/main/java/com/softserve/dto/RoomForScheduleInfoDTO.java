package com.softserve.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RoomForScheduleInfoDTO implements Serializable {
    private Long id;
    private String name;
    private RoomTypeDTO type;
    private boolean isAvailable;
}
