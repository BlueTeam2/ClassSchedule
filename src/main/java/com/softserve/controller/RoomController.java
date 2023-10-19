package com.softserve.controller;

import com.softserve.dto.MessageDTO;
import com.softserve.dto.RoomDTO;
import com.softserve.dto.RoomForScheduleInfoDTO;
import com.softserve.entity.Room;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.mapper.RoomForScheduleInfoMapper;
import com.softserve.mapper.RoomMapper;
import com.softserve.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@Api(tags = "Room API")
@RequestMapping("/rooms")
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;
    private final RoomForScheduleInfoMapper roomForScheduleInfoMapper;

    @Autowired
    public RoomController(RoomService roomService, RoomMapper roomMapper, RoomForScheduleInfoMapper roomForScheduleInfoMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
        this.roomForScheduleInfoMapper = roomForScheduleInfoMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get the list of all rooms")
    public ResponseEntity<List<RoomDTO>> list() {
        log.info("Enter into list of RoomController");
        return ResponseEntity.ok().body(roomMapper.convertToDtoList(roomService.getAll()));
    }

    @GetMapping("/free")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get the list of all free rooms by specific day and period")
    public ResponseEntity<List<RoomDTO>> freeRoomList(@RequestParam(value = "semesterId") Long semesterId,
                                                      @RequestParam(value = "classId") Long classId,
                                                      @RequestParam(value = "dayOfWeek") DayOfWeek dayOfWeek,
                                                      @RequestParam(value = "evenOdd") EvenOdd evenOdd
    ) {
        log.info("In freeRoomList (semesterId = [{}], classId = [{}], dayOfWeek = [{}], evenOdd = [{}])", semesterId, classId, dayOfWeek, evenOdd);
        List<Room> rooms = roomService.getAvailableRoomsForSchedule(semesterId, dayOfWeek, evenOdd, classId);
        return ResponseEntity.ok().body(roomMapper.convertToDtoList(rooms));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get room info by id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RoomDTO> get(@PathVariable("id") long id) {
        log.info("Enter into get of RoomController with id {} ", id);
        Room room = roomService.getById(id);
        return ResponseEntity.ok().body(roomMapper.convertToDto(room));
    }

    @PostMapping
    @ApiOperation(value = "Create new room")
    public ResponseEntity<RoomDTO> save(@RequestBody RoomDTO roomDTO) {
        log.info("Enter into save of RoomController with roomDTO: {}", roomDTO);
        Room newRoom = roomService.save(roomMapper.convertToEntity(roomDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.convertToDto(newRoom));
    }

    @PutMapping
    @ApiOperation(value = "Update existing room")
    public ResponseEntity<RoomDTO> update(@RequestBody RoomDTO roomDTO) {
        log.info("Enter into update of RoomController with roomDTO: {}", roomDTO);
        Room newRoom = roomService.update(roomMapper.convertToEntity(roomDTO));
        return ResponseEntity.ok().body(roomMapper.convertToDto(newRoom));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete room by id")
    public ResponseEntity<MessageDTO> delete(@PathVariable("id") long id) {
        log.info("Enter into delete of RoomController with id: {}", id);
        roomService.delete(roomService.getById(id));
        return ResponseEntity.ok().body(new MessageDTO("Room has been deleted successfully."));
    }

    @GetMapping("/disabled")
    @ApiOperation(value = "Get the list of disabled rooms")
    public ResponseEntity<List<RoomDTO>> getDisabled() {
        log.info("Enter into list of RoomController");
        return ResponseEntity.ok().body(roomMapper.convertToDtoList(roomService.getDisabled()));
    }

    @GetMapping("/available")
    @ApiOperation(value = "Get the list of all rooms (available/not available) with status")
    public ResponseEntity<List<RoomForScheduleInfoDTO>> getAvailableAndNotAvailableRoomsForSchedule(
            @RequestParam(value = "semesterId") Long semesterId,
            @RequestParam(value = "classId") Long classId,
            @RequestParam(value = "dayOfWeek") DayOfWeek dayOfWeek,
            @RequestParam(value = "evenOdd") EvenOdd evenOdd
    ) {
        List<Room> availableRooms = roomService.getAvailableRoomsForSchedule(semesterId, dayOfWeek, evenOdd, classId);
        List<Room> notAvailableRooms = roomService.getNotAvailableRoomsForSchedule(semesterId, dayOfWeek, evenOdd, classId);

        List<RoomForScheduleInfoDTO> availableRoomsDTO = roomForScheduleInfoMapper.toRoomForScheduleDTOList(availableRooms);
        List<RoomForScheduleInfoDTO> notAvailableRoomsDTO = roomForScheduleInfoMapper.toRoomForScheduleDTOList(notAvailableRooms);

        availableRoomsDTO.forEach(s -> s.setAvailable(true));
        availableRoomsDTO.addAll(notAvailableRoomsDTO);
        return ResponseEntity.ok().body(availableRoomsDTO);
    }

    @GetMapping("/ordered")
    @ApiOperation(value = "Get the list of all rooms sorted by order")
    public ResponseEntity<List<RoomDTO>> getAllOrdered() {
        log.info("Entered getAllOrdered");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomMapper.convertToDtoList(roomService.getAllOrdered()));
    }

    @PostMapping("/after/{id}")
    @ApiOperation(value = "Create room after id")
    public ResponseEntity<RoomDTO> saveRoomAfterId(@PathVariable("id") Long roomAfterId, @RequestBody RoomDTO roomDTO) {
        log.trace("Entered saveRoomAfterId({}{})", roomAfterId, roomDTO);
        Room room = roomService.createAfterOrder(roomMapper.convertToEntity(roomDTO), roomAfterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.convertToDto(room));
    }

    @PutMapping("/after/{id}")
    @ApiOperation(value = "Update room order")
    public ResponseEntity<RoomDTO> upgradeRoomAfterId(@PathVariable("id") Long roomAfterId, @RequestBody RoomDTO roomDTO) {
        log.trace("Entered upgradeRoomAfterId({}{})", roomAfterId, roomDTO);
        Room room = roomService.updateAfterOrder(roomMapper.convertToEntity(roomDTO), roomAfterId);
        return ResponseEntity.status(HttpStatus.OK).body(roomMapper.convertToDto(room));
    }
}
