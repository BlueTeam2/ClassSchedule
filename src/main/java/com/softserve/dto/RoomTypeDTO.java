package com.softserve.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RoomTypeDTO implements Serializable {
    private Long id;
    private String description;
}
