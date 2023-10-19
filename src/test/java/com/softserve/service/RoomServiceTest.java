package com.softserve.service;

import com.softserve.entity.Room;
import com.softserve.entity.RoomType;
import com.softserve.exception.EntityAlreadyExistsException;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.SortOrderNotExistsException;
import com.softserve.repository.RoomRepository;
import com.softserve.repository.SortOrderRepository;
import com.softserve.service.impl.RoomServiceImpl;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private SortOrderRepository<Room> sortOrderRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    public void getRoomById() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("Small auditory");
        Room room = new Room();
        room.setId(1L);
        room.setName("1 Room");
        room.setType(roomType);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room result = roomService.getById(1L);
        assertNotNull(result);
        assertEquals(room.getId(), result.getId());
        assertEquals(room.getType(), result.getType());
        verify(roomRepository, times(1)).findById(anyLong());
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfRoomNotFounded() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("Small auditory");
        Room room = new Room();
        room.setId(1L);
        room.setName("1 Room");
        room.setType(roomType);

        roomService.getById(2L);
        verify(roomRepository, times(1)).findById(2L);
    }

    @Test
    public void saveRoomIfNameAndTypeAreNotExist() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("Small auditory");
        Room room = new Room();
        room.setId(1L);
        room.setName("1 Room");
        room.setType(roomType);

        when(roomRepository.countRoomDuplicates(any(Room.class))).thenReturn(0L);
        when(roomRepository.save(room)).thenReturn(room);

        Room result = roomService.save(room);
        assertNotNull(result);
        assertEquals(room.getName(), result.getName());
        assertEquals(room.getType(), result.getType());
        verify(roomRepository, times(1)).save(room);
        verify(roomRepository, times(1)).countRoomDuplicates(room);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void throwEntityAlreadyExistsExceptionIfSavedRoomAlreadyExists() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("Small auditory");
        Room room = new Room();
        room.setId(1L);
        room.setName("1 Room");
        room.setType(roomType);

        when(roomRepository.countRoomDuplicates(room)).thenReturn(1L);

        roomService.save(room);
        verify(roomRepository, times(1)).save(room);
        verify(roomRepository, times(1)).countRoomDuplicates(room);
    }

    @Test
    public void updateRoomIfNameAndTypeAreNotExist() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("Small auditory");
        Room roomFromDTO = new Room();
        roomFromDTO.setId(1L);
        roomFromDTO.setName("1 Room updated");
        roomFromDTO.setType(roomType);
        Integer expectedSortOrder = 1;

        when(roomRepository.countRoomDuplicates(any(Room.class))).thenReturn(0L);
        when(sortOrderRepository.getSortOrderById(anyLong())).thenReturn(Optional.of(expectedSortOrder));
        when(roomRepository.update(roomFromDTO)).thenReturn(roomFromDTO);

        Room room = roomService.update(roomFromDTO);
        assertNotNull(room);
        assertThat(room).isEqualToComparingFieldByField(roomFromDTO);
        assertEquals(expectedSortOrder, room.getSortOrder());
        verify(roomRepository, times(1)).update(roomFromDTO);
        verify(roomRepository, times(1)).countRoomDuplicates(any(Room.class));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void throwEntityAlreadyExistsExceptionIfUpdatedNameAndTypeAlreadyExist() {
        RoomType updatedType = new RoomType();
        updatedType.setId(2L);
        updatedType.setDescription("Medium auditory");
        Room updatedRoom = new Room();
        updatedRoom.setId(1L);
        updatedRoom.setName("1 Room updated");
        updatedRoom.setType(updatedType);

        when(roomRepository.countRoomDuplicates(any(Room.class))).thenReturn(1L);

        roomService.update(updatedRoom);
        verify(roomRepository, times(1)).update(updatedRoom);
        verify(roomRepository, times(1)).countRoomDuplicates(any(Room.class));
    }

    @Test(expected = SortOrderNotExistsException.class)
    public void saveAfterOrder_WhenSaveAfterNotExistRoom_ShouldThrowSortOrderNotExistsException() throws SortOrderNotExistsException {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("Small auditory");
        Room room = new Room();
        room.setId(11L);
        room.setName("11 Room");
        room.setType(roomType);
        Long afterNotExistRoomId = 10L;

        when(sortOrderRepository.createAfterOrder(room, afterNotExistRoomId)).thenThrow(SortOrderNotExistsException.class);

        roomService.createAfterOrder(room, afterNotExistRoomId);

        verify(sortOrderRepository, times(1)).createAfterOrder(room, afterNotExistRoomId);
    }

    @Test
    public void saveAfterOrder_WhenSaveAfterNotExistRoom_ShouldThrowSortOrderNotExistsException2() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setDescription("Small auditory");
        Room room = new Room();
        room.setName("11 Room");
        room.setType(roomType);

        Room room1 = new Room();
        room1.setName("11 Room");
        room1.setType(roomType);
        room1.setSortOrder(1);

        Long afterNotExistRoomId = 0L;
        when(sortOrderRepository.createAfterOrder(room, afterNotExistRoomId)).thenReturn(room1);

        Room room2 = roomService.createAfterOrder(room, afterNotExistRoomId);

        assertEquals(room2, room1);
        assertSame(room2, room1);
        assertThat(room2).isEqualToComparingFieldByField(room1);

        verify(sortOrderRepository, times(1)).createAfterOrder(room, afterNotExistRoomId);
    }

}
