package com.softserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.assertions.CustomMockMvcAssertions;
import com.softserve.config.DBConfigTest;
import com.softserve.config.MyWebAppInitializer;
import com.softserve.config.WebMvcConfig;
import com.softserve.dto.*;
import com.softserve.entity.Lesson;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.mapper.GroupMapperImpl;
import com.softserve.mapper.LessonInfoMapperImpl;
import com.softserve.mapper.SubjectMapperImpl;
import com.softserve.mapper.TeacherNameMapperImpl;
import com.softserve.service.GroupService;
import com.softserve.service.LessonService;
import com.softserve.service.SubjectService;
import com.softserve.service.TeacherService;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.SoftAssertions;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static com.softserve.entity.enums.LessonType.LABORATORY;
import static com.softserve.entity.enums.LessonType.LECTURE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Category(IntegrationTestCategory.class)
@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, DBConfigTest.class, MyWebAppInitializer.class})
@WebAppConfiguration
@WithMockUser(username = "first@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "MANAGER")
@Sql(value = "classpath:create-lessons-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LessonsControllerTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule smr = new SpringMethodRule();

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private GroupService groupService;

    private CustomMockMvcAssertions assertions;

    @Before
    public void insertData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        assertions = new CustomMockMvcAssertions(mockMvc, objectMapper, "/lessons");
    }

    @Test
    public void getAllLessons() throws Exception {
        mockMvc.perform(get("/lessons").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getLessonById() throws Exception {
        mockMvc.perform(get("/lessons/{id}", 4).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @WithMockUser(username = "first@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "USER")
    public void returnForbiddenIfAuthenticatedUserRoleIsNotManager() throws Exception {
        mockMvc.perform(get("/lessons/{id}", 4).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllLessonsTypes() throws Exception {
        mockMvc.perform(get("/lessons/types").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void saveLessonsIfLessonDoesNotExist() throws Exception {
        TeacherNameDTO teacherDTO = new TeacherNameMapperImpl().teacherDTOToTeacher(teacherService.getById(5L));
        SubjectDTO subjectDTO = new SubjectMapperImpl().subjectToSubjectDTO(subjectService.getById(4L));
        GroupDTO groupDTO = new GroupMapperImpl().groupToGroupDTO(groupService.getById(4L));
        LessonInfoDTO lessonDtoForSave = new LessonInfoDTO();
        lessonDtoForSave.setHours(1);
        lessonDtoForSave.setSubjectForSite("");
        lessonDtoForSave.setLinkToMeeting("");
        lessonDtoForSave.setLessonType(LABORATORY);
        lessonDtoForSave.setTeacher(teacherDTO);
        lessonDtoForSave.setSubject(subjectDTO);
        lessonDtoForSave.setGroup(groupDTO);

        mockMvc.perform(post("/lessons").content(objectMapper.writeValueAsString(lessonDtoForSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateLessonIfLessonDoesNotExist() throws Exception {
        LessonInfoDTO lessonDtoForUpdate = new LessonInfoDTO();
        lessonDtoForUpdate.setId(5L);
        lessonDtoForUpdate.setHours(2);
        lessonDtoForUpdate.setLinkToMeeting("https://softserveinc.zoom.us/j/93198369163?pwd=Rk1GU281cDFtK1FCK3pJWXphRkJrQT09");
        lessonDtoForUpdate.setSubjectForSite("History updated");
        lessonDtoForUpdate.setLessonType(LECTURE);
        lessonDtoForUpdate.setTeacher(new TeacherNameMapperImpl().teacherDTOToTeacher(teacherService.getById(6L)));
        lessonDtoForUpdate.setSubject(new SubjectMapperImpl().subjectToSubjectDTO(subjectService.getById(6L)));
        lessonDtoForUpdate.setGroup(new GroupMapperImpl().groupToGroupDTO(groupService.getById(4L)));

        Lesson lessonForCompare = new LessonInfoMapperImpl().lessonInfoDTOToLesson(lessonDtoForUpdate);

        mockMvc.perform(put("/lessons").content(objectMapper.writeValueAsString(lessonDtoForUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(lessonForCompare.getId()))
                .andExpect(jsonPath("$.hours").value(lessonForCompare.getHours()))
                .andExpect(jsonPath("$.linkToMeeting").value(lessonForCompare.getLinkToMeeting()))
                .andExpect(jsonPath("$.subjectForSite").value(lessonForCompare.getSubjectForSite()))
                .andExpect(jsonPath("$.lessonType").value(lessonForCompare.getLessonType().toString()))
                .andExpect(jsonPath("$.subject").value(lessonForCompare.getSubject()))
                .andExpect(jsonPath("$.group").value(lessonForCompare.getGroup()));
    }

    @Test
    public void updateForGroupedLesson() throws Exception {
        LessonInfoDTO lessonDtoForUpdate = new LessonInfoDTO();
        lessonDtoForUpdate.setId(13L);
        lessonDtoForUpdate.setHours(2);
        lessonDtoForUpdate.setLinkToMeeting("https://softserveinc.zoom.us/j/93198369163?pwd=Rk1GU281cDFtK1FCK3pJWXphRkJrQT09");
        lessonDtoForUpdate.setSubjectForSite("Biology 3");
        lessonDtoForUpdate.setLessonType(LABORATORY);
        lessonDtoForUpdate.setTeacher(new TeacherNameMapperImpl().teacherDTOToTeacher(teacherService.getById(5L)));
        lessonDtoForUpdate.setSubject(new SubjectMapperImpl().subjectToSubjectDTO(subjectService.getById(4L)));
        lessonDtoForUpdate.setGroup(new GroupMapperImpl().groupToGroupDTO(groupService.getById(4L)));
        lessonDtoForUpdate.setGrouped(true);

        Lesson expectedLesson = new LessonInfoMapperImpl().lessonInfoDTOToLesson(lessonDtoForUpdate);

        mockMvc.perform(put("/lessons").content(objectMapper.writeValueAsString(lessonDtoForUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedLesson.getId()))
                .andExpect(jsonPath("$.hours").value(expectedLesson.getHours()))
                .andExpect(jsonPath("$.linkToMeeting").value(expectedLesson.getLinkToMeeting()))
                .andExpect(jsonPath("$.subjectForSite").value(expectedLesson.getSubjectForSite()))
                .andExpect(jsonPath("$.lessonType").value(expectedLesson.getLessonType().toString()))
                .andExpect(jsonPath("$.subject").value(expectedLesson.getSubject()))
                .andExpect(jsonPath("$.teacher").value(expectedLesson.getTeacher()))
                .andExpect(jsonPath("$.group").value(expectedLesson.getGroup()))
                .andExpect(jsonPath("$.grouped").value(expectedLesson.isGrouped()));

        LessonInfoDTO groupedWithSameSubjectForSite = new LessonInfoMapperImpl().lessonToLessonInfoDTO(lessonService.getById(14L));

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(lessonDtoForUpdate).isEqualToComparingOnlyGivenFields(groupedWithSameSubjectForSite,
                "hours", "linkToMeeting", "subjectForSite", "lessonType", "teacher",
                "subject", "grouped");
        softAssertions.assertThat(lessonDtoForUpdate.getGroup()).isNotEqualTo(groupedWithSameSubjectForSite.getGroup());
        softAssertions.assertAll();
    }

    @Test
    public void updateTeacherAndSubjectForGroupedLesson() throws Exception {
        LessonInfoDTO lessonDtoForUpdate = new LessonInfoDTO();
        lessonDtoForUpdate.setId(13L);
        lessonDtoForUpdate.setHours(1);
        lessonDtoForUpdate.setLinkToMeeting("");
        lessonDtoForUpdate.setSubjectForSite("History");
        lessonDtoForUpdate.setLessonType(LECTURE);
        lessonDtoForUpdate.setTeacher(new TeacherNameMapperImpl().teacherDTOToTeacher(teacherService.getById(4L)));
        lessonDtoForUpdate.setSubject(new SubjectMapperImpl().subjectToSubjectDTO(subjectService.getById(5L)));
        lessonDtoForUpdate.setGroup(new GroupMapperImpl().groupToGroupDTO(groupService.getById(4L)));
        lessonDtoForUpdate.setGrouped(true);

        Lesson expectedLesson = new LessonInfoMapperImpl().lessonInfoDTOToLesson(lessonDtoForUpdate);

        mockMvc.perform(put("/lessons").content(objectMapper.writeValueAsString(lessonDtoForUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedLesson.getId()))
                .andExpect(jsonPath("$.hours").value(expectedLesson.getHours()))
                .andExpect(jsonPath("$.linkToMeeting").value(expectedLesson.getLinkToMeeting()))
                .andExpect(jsonPath("$.subjectForSite").value(expectedLesson.getSubjectForSite()))
                .andExpect(jsonPath("$.lessonType").value(expectedLesson.getLessonType().toString()))
                .andExpect(jsonPath("$.subject").value(expectedLesson.getSubject()))
                .andExpect(jsonPath("$.teacher").value(expectedLesson.getTeacher()))
                .andExpect(jsonPath("$.group").value(expectedLesson.getGroup()))
                .andExpect(jsonPath("$.grouped").value(expectedLesson.isGrouped()));

        LessonInfoDTO groupedWithSameSubjectForSite = new LessonInfoMapperImpl().lessonToLessonInfoDTO(lessonService.getById(14L));
        LessonInfoDTO groupedWithDiffSubjectForSite = new LessonInfoMapperImpl().lessonToLessonInfoDTO(lessonService.getById(15L));

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(lessonDtoForUpdate).isEqualToComparingOnlyGivenFields(groupedWithSameSubjectForSite,
                "hours", "linkToMeeting", "subjectForSite", "lessonType", "teacher",
                "subject", "grouped");
        softAssertions.assertThat(lessonDtoForUpdate.getGroup()).isNotEqualTo(groupedWithSameSubjectForSite.getGroup());
        softAssertions.assertThat(lessonDtoForUpdate).isEqualToComparingOnlyGivenFields(groupedWithDiffSubjectForSite,
                "hours", "linkToMeeting", "subjectForSite", "lessonType", "teacher",
                "subject", "grouped");
        softAssertions.assertThat(lessonDtoForUpdate.getGroup()).isNotEqualTo(groupedWithDiffSubjectForSite.getGroup());
        softAssertions.assertAll();
    }

    @Test
    public void deleteLesson() throws Exception {
        mockMvc.perform(delete("/lessons/{id}", 7)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteLessonGrouped() throws Exception {
        assertions.assertForDelete(13, "/lessons/{id}");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThatThrownBy(() -> lessonService.getById(14L)).isInstanceOf(EntityNotFoundException.class);
        softAssertions.assertThat(lessonService.getById(15L)).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    public void returnNotFoundIfLessonNotFoundedById() throws Exception {
        mockMvc.perform(get("/lessons/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void returnBadRequestIfSaveExistLesson() throws Exception {
        Lesson lesson = lessonService.getById(7L);
        LessonInfoDTO lessonDtoForSave = new LessonInfoMapperImpl().lessonToLessonInfoDTO(lesson);

        LessonForGroupsDTO lessonForGroupsDTO = new LessonForGroupsDTO();

        lessonForGroupsDTO.setGroups(Collections.singletonList(lessonDtoForSave.getGroup()));
        lessonForGroupsDTO.setLessonType(lessonDtoForSave.getLessonType());
        lessonForGroupsDTO.setGrouped(lessonDtoForSave.isGrouped());
        lessonForGroupsDTO.setId(7L);
        lessonForGroupsDTO.setSemesterId(lesson.getSemester().getId());
        lessonForGroupsDTO.setHours(lessonDtoForSave.getHours());
        lessonForGroupsDTO.setLinkToMeeting(lessonDtoForSave.getLinkToMeeting());
        lessonForGroupsDTO.setSubjectForSite(lessonDtoForSave.getSubjectForSite());
        lessonForGroupsDTO.setLessonType(lessonDtoForSave.getLessonType());
        lessonForGroupsDTO.setSubject(lessonDtoForSave.getSubject());
        lessonForGroupsDTO.setTeacher(lessonDtoForSave.getTeacher());

        mockMvc.perform(post("/lessons").content(objectMapper.writeValueAsString(lessonForGroupsDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnInternalServerErrorIfSavedTeacherIsNull() throws Exception {
        SubjectDTO subjectDTO = new SubjectMapperImpl().subjectToSubjectDTO(subjectService.getById(6L));
        GroupDTO groupDTO = new GroupMapperImpl().groupToGroupDTO(groupService.getById(6L));
        LessonForGroupsDTO lessonDtoForSave = new LessonForGroupsDTO();
        lessonDtoForSave.setHours(2);
        lessonDtoForSave.setSubjectForSite("");
        lessonDtoForSave.setLinkToMeeting("");
        lessonDtoForSave.setLessonType(LABORATORY);
        lessonDtoForSave.setTeacher(null);
        lessonDtoForSave.setSubject(subjectDTO);
        lessonDtoForSave.setGroups(List.of(groupDTO));

        mockMvc.perform(post("/lessons").content(objectMapper.writeValueAsString(lessonDtoForSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void returnInternalServerErrorIfUpdatedTeacherIsNull() throws Exception {
        LessonInfoDTO lessonDtoForUpdate = new LessonInfoDTO();
        lessonDtoForUpdate.setId(4L);
        lessonDtoForUpdate.setHours(1);
        lessonDtoForUpdate.setLinkToMeeting("https://softserveinc.zoom.us/j/93198369163?pwd=Rk1GU281cDFtK1FCK3pJWXphRkJrQT09");
        lessonDtoForUpdate.setSubjectForSite("History of World");
        lessonDtoForUpdate.setLessonType(LECTURE);
        lessonDtoForUpdate.setTeacher(null);
        lessonDtoForUpdate.setSubject(new SubjectMapperImpl().subjectToSubjectDTO(subjectService.getById(5L)));
        lessonDtoForUpdate.setGroup(new GroupMapperImpl().groupToGroupDTO(groupService.getById(4L)));

        mockMvc.perform(put("/lessons", 4).content(objectMapper.writeValueAsString(lessonDtoForUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Parameters(method = "parametersToUpdateLinkToMeeting")
    public void updateLinkToMeeting(LessonWithLinkDTO lessonWithLinkDTO, Integer result) throws Exception {

        mockMvc.perform(put("/lessons/link").content(objectMapper.writeValueAsString(lessonWithLinkDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").value(result));
    }

    private Object[] parametersToUpdateLinkToMeeting() {
        LessonWithLinkDTO lessonWithSubjectAndType = new LessonWithLinkDTO();
        lessonWithSubjectAndType.setLinkToMeeting("https://www.youtube.com/");
        lessonWithSubjectAndType.setSemesterId(7L);
        lessonWithSubjectAndType.setTeacherId(5L);
        lessonWithSubjectAndType.setSubjectId(5L);
        lessonWithSubjectAndType.setLessonType("LECTURE");

        LessonWithLinkDTO lessonWithSubject = new LessonWithLinkDTO();
        lessonWithSubject.setLinkToMeeting("https://www.youtube.com/");
        lessonWithSubject.setSemesterId(7L);
        lessonWithSubject.setTeacherId(5L);
        lessonWithSubject.setSubjectId(5L);

        LessonWithLinkDTO lesson = new LessonWithLinkDTO();
        lesson.setLinkToMeeting("https://www.youtube.com/");
        lesson.setSemesterId(7L);
        lesson.setTeacherId(5L);

        LessonWithLinkDTO lessonWithNoExistingType = new LessonWithLinkDTO();
        lessonWithNoExistingType.setLinkToMeeting("https://www.youtube.com/");
        lessonWithNoExistingType.setSemesterId(7L);
        lessonWithNoExistingType.setTeacherId(5L);
        lessonWithNoExistingType.setSubjectId(5L);
        lessonWithNoExistingType.setLessonType("LABORATORY");

        return new Object[]{
                new Object[]{lessonWithSubjectAndType, 2},
                new Object[]{lessonWithSubject, 3},
                new Object[]{lesson, 4},
                new Object[]{lessonWithNoExistingType, 0}
        };
    }
}
