package com.softserve.mapper;

import org.junit.Assert;
import com.softserve.dto.TeacherDTO;
import com.softserve.dto.TeacherForUpdateDTO;
import com.softserve.entity.Teacher;
import com.softserve.entity.User;
import com.softserve.service.UnitTestCategory;
import com.softserve.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class TeacherMapperTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private TeacherMapperImpl teacherMapper;

    private Teacher teacherOnlyWithUserId1;

    private User userOnlyWithEmail;

    @Before
    public void setUp() {
        teacherOnlyWithUserId1 = new Teacher();
        teacherOnlyWithUserId1.setUserId(1L);

        userOnlyWithEmail = new User();
        userOnlyWithEmail.setEmail("teacher@gmail.com");
    }

    @Test
    public void testUserIdToEmailInTeacherToTeacherDTOIfUserIdExists() {
        when(userService.getById(1L)).thenReturn(userOnlyWithEmail);
        TeacherDTO actualTeacherDTO = teacherMapper.teacherToTeacherDTO(teacherOnlyWithUserId1);
        assertThat(actualTeacherDTO.getEmail()).isEqualTo(userOnlyWithEmail.getEmail());
        verify(userService).getById(1L);
    }

    @Test
    public void testUserIdToEmailInTeacherToTeacherForUpdateDTOIfUserIdExists() {
        when(userService.getById(1L)).thenReturn(userOnlyWithEmail);
        TeacherForUpdateDTO actualTeacherForUpdateDTO
                = teacherMapper.teacherToTeacherForUpdateDTO(teacherOnlyWithUserId1);
        assertThat(actualTeacherForUpdateDTO.getEmail()).isEqualTo(userOnlyWithEmail.getEmail());
        verify(userService).getById(1L);
    }

    @Test
    public void testUserIdToEmailInTeacherToTeacherDTOIfUserIdNotExist() {
        Teacher teacher = new Teacher();
        TeacherDTO teacherDTO = teacherMapper.teacherToTeacherDTO(teacher);
        assertThat(teacherDTO.getEmail()).isNull();
    }

    @Test
    public void testUserIdToEmailInTeacherToTeacherForUpdateDTOIfUserIdNotExist() {
        Teacher teacher = new Teacher();
        TeacherForUpdateDTO teacherForUpdateDTO = teacherMapper.teacherToTeacherForUpdateDTO(teacher);
        assertThat(teacherForUpdateDTO.getEmail()).isNull();
    }

    @Test
    public void teacherDTOToTeacherForSiteTest() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setSurname("Harrison");
        teacherDTO.setName("Ford");
        teacherDTO.setPatronymic("Edward");
        teacherDTO.setPosition("Doctor of Science");
        String expectedTeacherForSite = "Doctor of Science Harrison F. E.";

        String result = TeacherMapper.teacherDTOToTeacherForSite(teacherDTO);

        Assert.assertEquals(expectedTeacherForSite, result);
    }

}
