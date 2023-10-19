package com.softserve.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RoomDTO implements Serializable {
    private Long id;
    private String name;
    private boolean disable = false;
    private RoomTypeDTO type;
}
