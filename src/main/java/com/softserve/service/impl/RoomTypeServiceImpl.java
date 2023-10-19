package com.softserve.service.impl;

import com.softserve.entity.RoomType;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.repository.RoomTypeRepository;
import com.softserve.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoomType getById(Long id) {
        log.info("In getById(id = [{}])", id);
        return roomTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(RoomType.class, "id", id.toString()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoomType> getAll() {
        log.info("In getAll()");
        return roomTypeRepository.getAll();
    }

    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if given room type already exists in the repository
     */
    @Override
    public RoomType save(RoomType object) {
        log.info("In save(entity = [{}]", object);
        if (isRoomTypeExistsWithDescription(object.getDescription())) {
            throw new FieldAlreadyExistsException(RoomType.class, "description", object.getDescription());
        }
        return roomTypeRepository.save(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoomType update(RoomType object) {
        log.info("In update(entity = [{}]", object);
        if (isExistsWithId(object.getId())) {
            if (isRoomTypeExistsWithDescription(object.getDescription())) {
                log.error("RoomType with Description [{}] already exists", object.getDescription());
                throw new FieldAlreadyExistsException(RoomType.class, "description", object.getDescription());
            }
            return roomTypeRepository.update(object);
        } else {
            throw new EntityNotFoundException(RoomType.class, "id", object.getId().toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoomType delete(RoomType object) {
        log.info("In delete(entity = [{}])", object);
        return roomTypeRepository.delete(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRoomTypeExistsWithDescription(String description) {
        log.info("In isRoomTypeExistsWithDescription(description = [{}])", description);
        return roomTypeRepository.countRoomTypesWithDescription(description) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistsWithId(Long id) {
        log.info("In isExistsWithId(id = [{}])", id);
        return roomTypeRepository.countByRoomTypeId(id) != 0;
    }
}
