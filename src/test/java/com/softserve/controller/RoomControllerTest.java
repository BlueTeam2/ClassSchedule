package com.softserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.config.DBConfigTest;
import com.softserve.config.MyWebAppInitializer;
import com.softserve.config.WebMvcConfig;
import com.softserve.dto.RoomDTO;
import com.softserve.dto.RoomTypeDTO;
import com.softserve.entity.Room;

import com.softserve.mapper.RoomMapperImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Category(IntegrationTestCategory.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, DBConfigTest.class, MyWebAppInitializer.class})
@WebAppConfiguration
@WithMockUser(username = "first@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "MANAGER")
@Sql(value = "classpath:create-rooms-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RoomControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void getAllRooms() throws Exception {
        mockMvc.perform(get("/rooms").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getRoomById() throws Exception {
        mockMvc.perform(get("/rooms/{id}", 4).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @WithMockUser(username = "first@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "USER")
    public void returnForbiddenIfAuthenticatedUserRoleIsNotManager() throws Exception {
        mockMvc.perform(get("/rooms/{id}", 4).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getFreeRoomsBySpecificPeriodDayOfWeekAndNumberOfWeek() throws Exception {
        mockMvc.perform(get("/rooms/free?id=1&dayOfWeek=MONDAY&evenOdd=EVEN")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void saveRoomIfSavedRoomDoesNotExist() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(4L);
        roomTypeDTO.setDescription("Small auditory");
        RoomDTO roomDtoForSave = new RoomDTO();
        roomDtoForSave.setName("save small room");
        roomDtoForSave.setType(roomTypeDTO);

        mockMvc.perform(post("/rooms").content(objectMapper.writeValueAsString(roomDtoForSave))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateRoomIfUpdatedRoomDoesNotExist() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(5L);
        roomTypeDTO.setDescription("Medium auditory");
        RoomDTO roomDtoForUpdate = new RoomDTO();
        roomDtoForUpdate.setId(4L);
        roomDtoForUpdate.setName("update medium room");
        roomDtoForUpdate.setType(roomTypeDTO);

        Room roomForCompare = new RoomMapperImpl().convertToEntity(roomDtoForUpdate);

        mockMvc.perform(put("/rooms", 4).content(objectMapper.writeValueAsString(roomDtoForUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roomForCompare.getId()))
                .andExpect(jsonPath("$.type").value(roomForCompare.getType()))
                .andExpect(jsonPath("$.name").value(roomForCompare.getName()));
    }

    @Test
    public void deleteExistRoom() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void returnNotFoundIfRoomNotFoundedById() throws Exception {
        mockMvc.perform(get("/rooms/100")).andExpect(status().isNotFound());
    }

    @Test
    public void returnBadRequestIfSavedNameIsNull() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(4L);
        roomTypeDTO.setDescription("Small auditory");
        RoomDTO roomDtoForSave = new RoomDTO();
        roomDtoForSave.setName(null);
        roomDtoForSave.setType(roomTypeDTO);

        mockMvc.perform(post("/rooms").content(objectMapper.writeValueAsString(roomDtoForSave))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnBadRequestIfUpdatedTypeIsNull() throws Exception {
        RoomDTO roomDtoForUpdate = new RoomDTO();
        roomDtoForUpdate.setId(4L);
        roomDtoForUpdate.setName("update name");
        roomDtoForUpdate.setType(null);

        mockMvc.perform(put("/rooms", 4).content(objectMapper.writeValueAsString(roomDtoForUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}