package com.softserve.service;

import com.softserve.entity.RoomType;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.repository.RoomTypeRepository;
import com.softserve.service.impl.RoomTypeServiceImpl;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class RoomTypeServiceTest {

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @InjectMocks
    private RoomTypeServiceImpl roomTypeService;

    @Test
    public void getRoomTypeById() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("1 rooType");

        when(roomTypeRepository.findById(1L)).thenReturn(Optional.of(roomType));

        RoomType result = roomTypeService.getById(1L);
        assertNotNull(result);
        assertEquals(roomType.getId(), result.getId());
        verify(roomTypeRepository, times(1)).findById(anyLong());
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfRoomTypeNotExists() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("1 rooType");

        roomTypeService.getById(2L);
        verify(roomTypeRepository, times(1)).findById(2L);
    }

    @Test
    public void saveRoomTypeIfDescriptionDoesNotExists() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("1 roomType");

        when(roomTypeRepository.save(roomType)).thenReturn(roomType);
        when(roomTypeRepository.countRoomTypesWithDescription(anyString())).thenReturn(0L);

        RoomType result = roomTypeService.save(roomType);
        assertNotNull(result);
        assertEquals(roomType.getDescription(), result.getDescription());
        verify(roomTypeRepository, times(1)).save(roomType);
        verify(roomTypeRepository, times(1)).countRoomTypesWithDescription(anyString());
    }

    @Test(expected = FieldAlreadyExistsException.class)
    public void throwFieldAlreadyExistsExceptionIfSavedDescriptionAlreadyExists() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("1 roomType");

        when(roomTypeRepository.countRoomTypesWithDescription(anyString())).thenReturn(1L);

        roomTypeService.save(roomType);
        verify(roomTypeRepository, times(1)).save(roomType);
        verify(roomTypeRepository, times(1)).countRoomTypesWithDescription(anyString());
    }

    @Test
    public void updateRoomTypeIfDescriptionDoesNotExists() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("1 roomType");
        RoomType updatedRoomType = new RoomType();
        updatedRoomType.setId(1L);
        updatedRoomType.setDescription("updated roomType");

        when(roomTypeRepository.countByRoomTypeId(1L)).thenReturn(1L);
        when(roomTypeRepository.countRoomTypesWithDescription(anyString())).thenReturn(0L);
        when(roomTypeRepository.update(updatedRoomType)).thenReturn(updatedRoomType);

        roomType = roomTypeService.update(updatedRoomType);
        assertNotNull(roomType);
        assertEquals(updatedRoomType, roomType);
        verify(roomTypeRepository, times(1)).update(updatedRoomType);
        verify(roomTypeRepository, times(1)).countByRoomTypeId(1L);
        verify(roomTypeRepository, times(1)).countRoomTypesWithDescription(anyString());
    }

    @Test(expected = FieldAlreadyExistsException.class)
    public void throwFieldAlreadyExistsExceptionIfUpdatedDescriptionAlreadyExists() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("1 roomType");
        RoomType updatedRoomType = new RoomType();
        updatedRoomType.setId(1L);
        updatedRoomType.setDescription("1 roomType");

        when(roomTypeRepository.countByRoomTypeId(anyLong())).thenReturn(1L);
        when(roomTypeRepository.countRoomTypesWithDescription(anyString())).thenReturn(1L);

        roomTypeService.update(updatedRoomType);
        verify(roomTypeRepository, times(1)).countByRoomTypeId(anyLong());
        verify(roomTypeRepository, times(1)).countRoomTypesWithDescription(anyString());
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateWhenRoomTypeNotFound() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("1 roomType");

        when(roomTypeRepository.countByRoomTypeId(anyLong())).thenReturn(0L);

        roomTypeService.update(roomType);
        verify(roomTypeRepository, times(1)).update(roomType);
    }
}
