package com.softserve.service;

import com.softserve.entity.RoomType;

public interface RoomTypeService extends BasicService<RoomType, Long> {

    /**
     * Checks if room type with given description already exists in the repository.
     *
     * @param description the description of the room type
     * @return {@code true} if room type with given description already exists
     */
    boolean isRoomTypeExistsWithDescription(String description);

    /**
     * Checks if room type with given id already exist in the repository.
     *
     * @param id the id of the room type
     * @return {@code true} if room type with given id already exist
     */
    boolean isExistsWithId(Long id);
}
