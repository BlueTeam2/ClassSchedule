package com.softserve.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CreateScheduleInfoDTO implements Serializable {
    private boolean isTeacherAvailable;
    private List<RoomForScheduleInfoDTO> rooms;
}
