package com.softserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.config.DBConfigTest;
import com.softserve.config.MyWebAppInitializer;
import com.softserve.config.WebMvcConfig;
import com.softserve.dto.ScheduleSaveDTO;
import com.softserve.entity.Lesson;
import com.softserve.entity.Teacher;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.service.*;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import static com.softserve.entity.enums.LessonType.LECTURE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Category(IntegrationTestCategory.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, DBConfigTest.class, MyWebAppInitializer.class})
@WebAppConfiguration
@WithMockUser(username = "first@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "MANAGER")
@Sql(value = "classpath:create-schedule-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ScheduleControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private LessonService lessonService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void getListOfAllSchedules() throws Exception {
        mockMvc.perform(get("/schedules").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getListOfAllSchedulesBySemesterId() throws Exception {
        mockMvc.perform(get("/schedules/semester").param("semesterId", "4").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void returnEmptyListIfGetListOfAllSchedulesByNoExistSemester() throws Exception {
        mockMvc.perform(get("/schedules/semester").param("semesterId", "100").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", IsEmptyCollection.empty()))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getInfoForNoExistScheduleBySemesterIdByDayOfWeekByEvenOddByClassIdByLessonId() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setPatronymic("Ivanovych");
        teacher.setSurname("Tymysh");
        teacher.setName("Oleg");
        teacher.setPosition("docent");
        teacherService.save(teacher);
        Lesson lesson = new Lesson();
        lesson.setHours(2);
        lesson.setSubjectForSite("lesson for getInfo");
        lesson.setLinkToMeeting("some link....");
        lesson.setLessonType(LECTURE);
        lesson.setSubject(subjectService.getById(4L));
        lesson.setGroup(groupService.getById(4L));
        lesson.setTeacher(teacher);
        lessonService.save(lesson);

        mockMvc.perform(get("/schedules/data-before")
                        .param("semesterId", "4")
                        .param("dayOfWeek", "MONDAY")
                        .param("evenOdd", "EVEN")
                        .param("classId", "6")
                        .param("lessonId", lesson.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void returnBadRequestIfGetInfoForExistScheduleBySemesterIdByDayOfWeekByEvenOddByClassIdByLessonId() throws Exception {
        mockMvc.perform(get("/schedules/data-before")
                        .param("semesterId", "4")
                        .param("dayOfWeek", "MONDAY")
                        .param("evenOdd", "EVEN")
                        .param("classId", "4")
                        .param("lessonId", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getFullScheduleForGroup() throws Exception {
        mockMvc.perform(get("/schedules/full/groups")
                        .param("semesterId", "4")
                        .param("groupId", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void returnEmptyListOfScheduleIfGetFullScheduleForNotFoundedGroup() throws Exception {
        mockMvc.perform(get("/schedules/full/groups")
                        .param("semesterId", "4")
                        .param("groupId", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedule").value(Matchers.empty()))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getFullScheduleForSemester() throws Exception {
        mockMvc.perform(get("/schedules/full/semester")
                        .param("semesterId", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void returnNotFoundIfGetFullScheduleForNotFoundedSemester() throws Exception {
        mockMvc.perform(get("/schedules/full/semester")
                        .param("semesterId", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getFullScheduleForTeacher() throws Exception {
        mockMvc.perform(get("/schedules/full/teachers")
                        .param("semesterId", "4")
                        .param("teacherId", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void returnNotFoundIfGetFullScheduleForNotFoundedTeacher() throws Exception {
        mockMvc.perform(get("/schedules/full/teachers")
                        .param("semesterId", "4")
                        .param("teacherId", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getFullScheduleForRoom() throws Exception {
        mockMvc.perform(get("/schedules/full/rooms")
                        .param("semesterId", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void saveSchedule() throws Exception {
        ScheduleSaveDTO scheduleSaveDTO = new ScheduleSaveDTO();
        scheduleSaveDTO.setDayOfWeek(DayOfWeek.TUESDAY);
        scheduleSaveDTO.setEvenOdd(EvenOdd.ODD);
        scheduleSaveDTO.setLessonId(5L);
        scheduleSaveDTO.setPeriodId(4L);
        scheduleSaveDTO.setRoomId(5L);

        mockMvc.perform(post("/schedules")
                        .content(objectMapper.writeValueAsString(scheduleSaveDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void saveScheduleWithGroupedLessons() throws Exception {
        ScheduleSaveDTO scheduleSaveDTO = new ScheduleSaveDTO();
        scheduleSaveDTO.setDayOfWeek(DayOfWeek.TUESDAY);
        scheduleSaveDTO.setEvenOdd(EvenOdd.ODD);
        scheduleSaveDTO.setLessonId(8L);
        scheduleSaveDTO.setPeriodId(5L);
        scheduleSaveDTO.setRoomId(4L);
        ScheduleSaveDTO scheduleGrouped = new ScheduleSaveDTO();
        scheduleGrouped.setDayOfWeek(DayOfWeek.TUESDAY);
        scheduleGrouped.setEvenOdd(EvenOdd.ODD);
        scheduleGrouped.setLessonId(9L);
        scheduleGrouped.setPeriodId(5L);
        scheduleGrouped.setRoomId(4L);

        MvcResult mvcResult = mockMvc.perform(post("/schedules")
                        .content(objectMapper.writeValueAsString(scheduleSaveDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<ScheduleSaveDTO> savedSchedules = Arrays.asList(objectMapper.readValue(contentAsString, ScheduleSaveDTO[].class));

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(scheduleSaveDTO).isEqualToComparingOnlyGivenFields(savedSchedules.get(0),
                "dayOfWeek", "evenOdd", "lessonId", "periodId", "roomId");
        softAssertions.assertThat(scheduleGrouped).isEqualToComparingOnlyGivenFields(savedSchedules.get(1),
                "dayOfWeek", "evenOdd", "lessonId", "periodId", "roomId");
        softAssertions.assertAll();
    }

//  Uncomment when fix response statusCode
    /*@Test
    public void saveScheduleIfScheduleIsExist() throws Exception {
        ScheduleSaveDTO scheduleSaveDTO = new ScheduleSaveDTO();
        scheduleSaveDTO.setDayOfWeek(DayOfWeek.MONDAY);
        scheduleSaveDTO.setEvenOdd(EvenOdd.EVEN);
        scheduleSaveDTO.setLessonId(4L);
        scheduleSaveDTO.setPeriodId(4L);
        scheduleSaveDTO.setRoomId(4L);
        scheduleSaveDTO.setSemesterId(4L);

        mockMvc.perform(post("/schedules").content(objectMapper.writeValueAsString(scheduleSaveDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }*/

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/schedules/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void returnBadRequestIfSaveScheduleWhenEvenOddIsNull() throws Exception {
        ScheduleSaveDTO scheduleSaveDTO = new ScheduleSaveDTO();
        scheduleSaveDTO.setDayOfWeek(DayOfWeek.MONDAY);
        scheduleSaveDTO.setEvenOdd(null);
        scheduleSaveDTO.setLessonId(4L);
        scheduleSaveDTO.setPeriodId(5L);
        scheduleSaveDTO.setRoomId(4L);

        mockMvc.perform(post("/schedules").content(objectMapper.writeValueAsString(scheduleSaveDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
