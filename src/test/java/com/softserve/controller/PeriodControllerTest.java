package com.softserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softserve.config.DBConfigTest;
import com.softserve.config.MyWebAppInitializer;
import com.softserve.config.WebMvcConfig;
import com.softserve.dto.AddPeriodDTO;
import com.softserve.dto.PeriodDTO;
import com.softserve.entity.Period;
import com.softserve.mapper.PeriodMapperImpl;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Category(IntegrationTestCategory.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, DBConfigTest.class, MyWebAppInitializer.class})
@WebAppConfiguration
@WithMockUser(username = "first@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "MANAGER")
@Sql(value = "classpath:create-periods-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PeriodControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void getAllPeriods() throws Exception {
        mockMvc.perform(get("/classes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getPeriodById() throws Exception {
        mockMvc.perform(get("/classes/{id}", 4).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @WithMockUser(username = "first@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "USER")
    public void returnForbiddenIfAuthenticatedUserRoleIsNotManager() throws Exception {
        mockMvc.perform(get("/classes/{id}", 4).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void savePeriodIfSavedPeriodDoesNotExist() throws Exception {
        AddPeriodDTO periodDtoForSave = new AddPeriodDTO();
        periodDtoForSave.setName("save period");
        periodDtoForSave.setStartTime(LocalTime.parse("09:00:00"));
        periodDtoForSave.setEndTime(LocalTime.parse("10:00:00"));

        mockMvc.perform(post("/classes").content(objectMapper.writeValueAsString(periodDtoForSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void saveListOfPeriodsIfAllOfThemDoNotExist() throws Exception {
        AddPeriodDTO periodDtoForList = new AddPeriodDTO();
        periodDtoForList.setName("save list of periods");
        periodDtoForList.setStartTime(LocalTime.parse("11:00:00"));
        periodDtoForList.setEndTime(LocalTime.parse("12:00:00"));
        List<AddPeriodDTO> periodDtoListForSave = new ArrayList<>();
        periodDtoListForSave.add(periodDtoForList);

        mockMvc.perform(post("/classes/all").content(objectMapper.writeValueAsString(periodDtoListForSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updatePeriodIfUpdatedPeriodDoesNotExist() throws Exception {
        PeriodDTO periodDtoForUpdate = new PeriodDTO();
        periodDtoForUpdate.setId(4L);
        periodDtoForUpdate.setName("1 para updated");
        periodDtoForUpdate.setStartTime(LocalTime.parse("13:00:00"));
        periodDtoForUpdate.setEndTime(LocalTime.parse("14:00:00"));

        Period periodForCompare = new PeriodMapperImpl().convertToEntity(periodDtoForUpdate);

        mockMvc.perform(put("/classes", 4).content(objectMapper.writeValueAsString(periodDtoForUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(periodForCompare.getId()))
                .andExpect(jsonPath("$.class_name").value(periodForCompare.getName()));
    }

    @Test
    public void deleteExistPeriod() throws Exception {
        mockMvc.perform(delete("/classes/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void returnNotFoundIfPeriodNotFoundedById() throws Exception {
        mockMvc.perform(get("/classes/100")).andExpect(status().isNotFound());
    }

    @Test
    public void returnBadRequestIfSavedPeriodAlreadyExist() throws Exception {
        AddPeriodDTO addPeriodDTO = new AddPeriodDTO();
        addPeriodDTO.setName("1 para");
        addPeriodDTO.setStartTime(LocalTime.parse("01:00:00"));
        addPeriodDTO.setEndTime(LocalTime.parse("02:00:00"));
        mockMvc.perform(post("/classes").content(objectMapper.writeValueAsString(addPeriodDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnInternalServerErrorIfSavedNameIsNull() throws Exception {
        AddPeriodDTO periodDtoForSave = new AddPeriodDTO();
        periodDtoForSave.setName(null);
        periodDtoForSave.setStartTime(LocalTime.parse("03:00:00"));
        periodDtoForSave.setEndTime(LocalTime.parse("04:00:00"));

        mockMvc.perform(post("/classes").content(objectMapper.writeValueAsString(periodDtoForSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void returnBadRequestIfUpdatedPeriodAlreadyExist() throws Exception {
        PeriodDTO periodDtoForUpdate = new PeriodDTO();
        periodDtoForUpdate.setId(6L);
        periodDtoForUpdate.setName("1 para");
        periodDtoForUpdate.setStartTime(LocalTime.parse("13:00:00"));
        periodDtoForUpdate.setEndTime(LocalTime.parse("14:00:00"));

        mockMvc.perform(put("/classes", 6).content(objectMapper.writeValueAsString(periodDtoForUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnBadRequestIfUpdatedNameIsNull() throws Exception {
        PeriodDTO periodDtoForUpdate = new PeriodDTO();
        periodDtoForUpdate.setId(5L);
        periodDtoForUpdate.setName(null);
        periodDtoForUpdate.setStartTime(LocalTime.parse("13:00:00"));
        periodDtoForUpdate.setEndTime(LocalTime.parse("14:00:00"));

        mockMvc.perform(put("/classes", 5).content(objectMapper.writeValueAsString(periodDtoForUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnBadRequestIfSavedPeriodIntersectsWithOtherPeriod() throws Exception {
        AddPeriodDTO periodDtoForSave = new AddPeriodDTO();
        periodDtoForSave.setName("intersect period");
        periodDtoForSave.setStartTime(LocalTime.parse("03:30:00"));
        periodDtoForSave.setEndTime(LocalTime.parse("04:40:00"));

        mockMvc.perform(post("/classes").content(objectMapper.writeValueAsString(periodDtoForSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllPublicClasses() throws Exception {
        mockMvc.perform(get("/public/classes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
