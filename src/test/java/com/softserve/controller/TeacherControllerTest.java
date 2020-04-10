package com.softserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.config.DBConfigTest;
import com.softserve.config.MyWebAppInitializer;
import com.softserve.config.WebMvcConfig;
import com.softserve.dto.TeacherDTO;
import com.softserve.entity.Teacher;
import com.softserve.service.TeacherService;
import com.softserve.service.mapper.TeacherMapperImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, DBConfigTest.class, MyWebAppInitializer.class})
@WebAppConfiguration
public class TeacherControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Teacher teacher = new Teacher();
    private TeacherDTO teacherDtoForBefore = new TeacherDTO();
    private TeacherDTO teacherDtoForSave = new TeacherDTO();
    private TeacherDTO teacherDtoForUpdate = new TeacherDTO();

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TeacherService teacherService;

    @Before
    public void insertData() {

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        //Save new period before all Test methods
        teacherDtoForBefore.setName("dto name");
        teacherDtoForBefore.setSurname("dto surname");
        teacherDtoForBefore.setPatronymic("dto patronymic");
        teacherDtoForBefore.setPosition("dto position");
        teacher = new TeacherMapperImpl().teacherDTOToTeacher(teacherDtoForBefore);
        teacherService.save(teacher);

        teacherDtoForSave.setName("save name");
        teacherDtoForSave.setSurname("save surname");
        teacherDtoForSave.setPatronymic("save patronymic");
        teacherDtoForSave.setPosition("save position");

        teacherDtoForUpdate.setId(teacher.getId());
        teacherDtoForUpdate.setName("update name");
        teacherDtoForUpdate.setSurname("update surname");
        teacherDtoForUpdate.setPatronymic("update patronymic");
        teacherDtoForUpdate.setPosition("update position");
    }

    @After
    public void deleteData() {
        teacherService.delete(teacher);
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/teachers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testGet() throws Exception {

        mockMvc.perform(get("/teachers/{id}", String.valueOf(teacher.getId())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(String.valueOf(teacher.getId())));
    }

    @Test
    public void testSave() throws Exception {

        mockMvc.perform(post("/teachers").content(objectMapper.writeValueAsString(teacherDtoForSave))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdate() throws Exception {

        Teacher teacherForCompare = new TeacherMapperImpl().teacherDTOToTeacher(teacherDtoForUpdate);

        mockMvc.perform(put("/teachers", String.valueOf(teacher.getId())).content(objectMapper.writeValueAsString(teacherDtoForUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacherForCompare.getId()))
                .andExpect(jsonPath("$.name").value(teacherForCompare.getName()))
                .andExpect(jsonPath("$.surname").value(teacherForCompare.getSurname()))
                .andExpect(jsonPath("$.patronymic").value(teacherForCompare.getPatronymic()))
                .andExpect(jsonPath("$.position").value(teacherForCompare.getPosition()));
    }

    @Test
    public void testDelete() throws Exception {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setName("delete name");
        teacherDTO.setSurname("delete surname");
        teacherDTO.setPatronymic("delete patronymic");
        teacherDTO.setPosition("delete position");
        Teacher save = teacherService.save(new TeacherMapperImpl().teacherDTOToTeacher(teacherDTO));
        mockMvc.perform(delete("/teachers/{id}", String.valueOf(save.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
