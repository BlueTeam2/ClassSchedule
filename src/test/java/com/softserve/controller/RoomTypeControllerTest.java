package com.softserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.config.DBConfigTest;
import com.softserve.config.MyWebAppInitializer;
import com.softserve.config.WebMvcConfig;
import com.softserve.dto.RoomTypeDTO;
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
@Sql(value = "classpath:create-roomtypes-before.sql")
public class RoomTypeControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void getAllRoomTypes() throws Exception {
        mockMvc.perform(get("/room-types").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getRoomTypeById() throws Exception {
        mockMvc.perform(get("/room-types/{id}", 4).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @WithMockUser(username = "first@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "USER")
    public void returnForbiddenIfAuthenticatedUserRoleIsNotManager() throws Exception {
        mockMvc.perform(get("/room-types/{id}", 4).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void saveRoomTypeIfSavedRoomTypeDoesNotExist() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(1L);
        roomTypeDTO.setDescription("Another Small auditory");

        mockMvc.perform(post("/room-types").content(objectMapper.writeValueAsString(roomTypeDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateRoomTypeIfUpdatedDescriptionDoesNotExist() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(4L);
        roomTypeDTO.setDescription("Another Small auditory");

        mockMvc.perform(put("/room-types", 4).content(objectMapper.writeValueAsString(roomTypeDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roomTypeDTO.getId()))
                .andExpect(jsonPath("$.description").value(roomTypeDTO.getDescription()));
    }

    @Test
    public void deleteExistRoomType() throws Exception {
        mockMvc.perform(delete("/room-types/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void returnNotFoundIfRoomTypeNotFoundedById() throws Exception {
        mockMvc.perform(get("/room-types/100")).andExpect(status().isNotFound());
    }

    @Test
    public void returnBadRequestIfSavedRoomTypeAlreadyExists() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setDescription("small auditory");

        mockMvc.perform(post("/room-types").content(objectMapper.writeValueAsString(roomTypeDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnBadRequestIfUpdatedRoomTypeAlreadyExists() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setDescription("small auditory");
        roomTypeDTO.setId(5L);

        mockMvc.perform(put("/room-types", 5).content(objectMapper.writeValueAsString(roomTypeDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnBadRequestIfSavedDescriptionIsNull() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(1L);
        roomTypeDTO.setDescription(null);

        mockMvc.perform(post("/room-types").content(objectMapper.writeValueAsString(roomTypeDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnBadRequestIfUpdatedDescriptionIsNull() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(5L);
        roomTypeDTO.setDescription(null);

        mockMvc.perform(put("/room-types").content(objectMapper.writeValueAsString(roomTypeDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
