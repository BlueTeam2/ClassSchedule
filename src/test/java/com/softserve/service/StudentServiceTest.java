package com.softserve.service;

import com.softserve.dto.GroupDTO;
import com.softserve.dto.StudentDTO;
import com.softserve.dto.StudentImportDTO;
import com.softserve.dto.enums.ImportSaveStatus;
import com.softserve.entity.Group;
import com.softserve.entity.Student;
import com.softserve.entity.User;
import com.softserve.entity.enums.Role;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.exception.FieldNullException;
import com.softserve.mapper.GroupMapper;
import com.softserve.mapper.StudentMapper;
import com.softserve.repository.StudentRepository;
import com.softserve.service.impl.StudentServiceImpl;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Category(UnitTestCategory.class)
@RunWith(JUnitParamsRunner.class)
public class StudentServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UserService userService;
    @Mock
    private GroupService groupService;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private GroupMapper groupMapper;

    private Student studentWithId1L;
    private StudentDTO studentDTOWithId1L;
    private StudentDTO studentDTOWithId2L;

    @Before
    public void setUp() {

        User userWithId1L = new User();
        userWithId1L.setId(1L);
        userWithId1L.setEmail("userWithId1L@test.com");
        userWithId1L.setPassword("12345@testAa");
        userWithId1L.setRole(Role.ROLE_STUDENT);

        studentWithId1L = new Student();
        studentWithId1L.setName("Name");
        studentWithId1L.setSurname("Surname");
        studentWithId1L.setPatronymic("Patronymic");
        studentWithId1L.setUser(userWithId1L);

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(3L);
        groupDTO.setTitle("Test");

        studentDTOWithId1L = new StudentDTO();
        studentDTOWithId1L.setId(null);
        studentDTOWithId1L.setName("Name");
        studentDTOWithId1L.setSurname("Surname");
        studentDTOWithId1L.setPatronymic("Patronymic");
        studentDTOWithId1L.setEmail("aware.123db@gmail.com");
        studentDTOWithId1L.setGroup(groupDTO);

        studentDTOWithId2L = new StudentDTO();
        studentDTOWithId2L.setId(null);
        studentDTOWithId2L.setName("Name");
        studentDTOWithId2L.setSurname("Surname");
        studentDTOWithId2L.setPatronymic("Patronymic");
        studentDTOWithId2L.setEmail(null);

    }

    @Test
    public void getAll() {
        List<Student> expected = singletonList(studentWithId1L);
        when(studentRepository.getAll()).thenReturn(expected);

        List<Student> actual = studentService.getAll();

        assertThat(actual).hasSameSizeAs(expected).hasSameElementsAs(expected);
        verify(studentRepository).getAll();
    }

    @Test
    public void getById() {
        Student expected = studentWithId1L;
        when(studentRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Student actual = studentService.getById(expected.getId());

        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(studentRepository).findById(expected.getId());
    }

    @Test
    public void save() {
        Student expected = studentWithId1L;
        when(studentRepository.save(expected)).thenReturn(expected);

        Student actual = studentService.save(expected);

        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(studentRepository).save(expected);
    }

    @Test
    public void update() {
        Student expected = studentWithId1L;
        when(studentRepository.update(expected)).thenReturn(expected);

        Student actual = studentService.update(expected);

        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(studentRepository).update(expected);
    }

    @Test
    public void delete() {
        Student expected = studentWithId1L;
        when(studentRepository.delete(expected)).thenReturn(expected);

        Student actual = studentService.delete(expected);

        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(studentRepository).delete(expected);
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionWhenGetById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        studentService.getById(1L);
        verify(studentRepository).findById(1L);
    }

    @Test(expected = FieldAlreadyExistsException.class)
    public void throwFieldAlreadyExistsExceptionWhenSave() {
        User user = new User();
        user.setId(16L);
        user.setEmail("aware.123db@gmail.com");
        user.setPassword("Pass1233!");
        user.setRole(Role.ROLE_STUDENT);

        Group group = new Group();
        group.setId(1L);
        group.setTitle("First Title");

        Student student = new Student();
        student.setId(null);
        student.setName("Name");
        student.setName("Name");
        student.setSurname("Surname");
        student.setPatronymic("Patronymic");
        student.setUser(user);
        student.setGroup(group);

        StudentDTO expected = studentDTOWithId1L;
        when(studentMapper.studentDTOToStudent(expected)).thenReturn(student);
        when(userService.findSocialUser(expected.getEmail())).thenReturn(Optional.of(user));
        when(studentRepository.isEmailInUse(anyString())).thenReturn(true);
        studentService.save(expected);
    }

    @Test(expected = FieldNullException.class)
    public void throwFieldNullExceptionWhenSave() {
        Student student = new Student();
        student.setId(null);
        student.setName("Name");
        student.setName("Name");
        student.setSurname("Surname");
        student.setPatronymic("Patronymic");
        student.setUser(null);

        StudentDTO expected = studentDTOWithId2L;
        when(studentMapper.studentDTOToStudent(expected)).thenReturn(student);
        studentService.save(expected);
    }

    @Test(expected = FieldNullException.class)
    public void throwFieldNullExceptionWhenUpdate() {
        Student student = new Student();
        student.setId(null);
        student.setName("Name");
        student.setName("Name");
        student.setSurname("Surname");
        student.setPatronymic("Patronymic");
        student.setUser(null);

        StudentDTO expected = studentDTOWithId2L;
        when(studentMapper.studentDTOToStudent(expected)).thenReturn(student);
        studentService.update(expected);
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionWhenUpdate() {
        User user = new User();
        user.setId(16L);
        user.setEmail("aware.123db@gmail.com");
        user.setPassword("Pass1233!");
        user.setRole(Role.ROLE_STUDENT);

        Group group = new Group();
        group.setId(1L);
        group.setTitle("First Title");

        Student student = new Student();
        student.setId(null);
        student.setName("Name");
        student.setName("Name");
        student.setSurname("Surname");
        student.setPatronymic("Patronymic");
        student.setUser(user);
        student.setGroup(group);

        StudentDTO expected = studentDTOWithId1L;
        when(studentMapper.studentDTOToStudent(expected)).thenReturn(student);
        when(studentRepository.isIdPresent(student.getId())).thenReturn(false);
        studentService.update(expected);
    }

    @Test
    @Parameters(method = "parametersToTestImport")
    public void importStudentsFromFile(MockMultipartFile multipartFile) {

        User userWithId1L = new User();
        userWithId1L.setId(1L);
        userWithId1L.setEmail("romaniuk@gmail.com");
        userWithId1L.setPassword("12345@testAa");
        userWithId1L.setRole(Role.ROLE_STUDENT);

        User userWithId2L = new User();
        userWithId2L.setId(2L);
        userWithId2L.setEmail("hanushchak@bigmir.net");
        userWithId2L.setPassword("12345@testAa");
        userWithId2L.setRole(Role.ROLE_STUDENT);

        Group group = new Group();
        group.setId(10L);
        GroupDTO groupDTO = new GroupDTO();
        group.setId(10L);

        List<StudentImportDTO> expectedStudents = new ArrayList<>();

        StudentImportDTO studentImportDTO1 = new StudentImportDTO();
        studentImportDTO1.setName("Hanna");
        studentImportDTO1.setSurname("Romaniuk");
        studentImportDTO1.setPatronymic("Stepanivna");
        studentImportDTO1.setEmail("romaniuk@gmail.com");

        StudentImportDTO studentImportDTO1Mapped = new StudentImportDTO();
        studentImportDTO1Mapped.setId(1L);
        studentImportDTO1Mapped.setName("Hanna");
        studentImportDTO1Mapped.setSurname("Romaniuk");
        studentImportDTO1Mapped.setPatronymic("Stepanivna");
        studentImportDTO1Mapped.setEmail("romaniuk@gmail.com");
        studentImportDTO1Mapped.setGroupDTO(groupDTO);

        StudentImportDTO studentImportDTO2 = new StudentImportDTO();
        studentImportDTO2.setId(null);
        studentImportDTO2.setName("Oleksandr");
        studentImportDTO2.setSurname("Boichuk");
        studentImportDTO2.setPatronymic("Ivanovych");
        studentImportDTO2.setEmail("");
        studentImportDTO2.setGroupDTO(null);
        studentImportDTO2.setImportSaveStatus(ImportSaveStatus.VALIDATION_ERROR);

        StudentImportDTO studentImportDTO3 = new StudentImportDTO();
        studentImportDTO3.setName("Viktor");
        studentImportDTO3.setSurname("Hanushchak");
        studentImportDTO3.setPatronymic("Mykolaiovych");
        studentImportDTO3.setEmail("hanushchak@bigmir.net");

        StudentImportDTO studentImportDTO3Mapped = new StudentImportDTO();
        studentImportDTO3Mapped.setId(1L);
        studentImportDTO3Mapped.setName("Viktor");
        studentImportDTO3Mapped.setSurname("Hanushchak");
        studentImportDTO3Mapped.setPatronymic("Mykolaiovych");
        studentImportDTO3Mapped.setEmail("hanushchak@bigmir.net");
        studentImportDTO3Mapped.setGroupDTO(groupDTO);

        expectedStudents.add(studentImportDTO1);
        expectedStudents.add(studentImportDTO2);
        expectedStudents.add(studentImportDTO3);

        Student student1 = new Student();
        student1.setName("Hanna");
        student1.setSurname("Romaniuk");
        student1.setPatronymic("Stepanivna");
        StudentDTO studentDTO1 = new StudentDTO();
        studentDTO1.setName("Hanna");
        studentDTO1.setSurname("Romaniuk");
        studentDTO1.setPatronymic("Stepanivna");
        studentDTO1.setEmail("romaniuk@gmail.com");
        Student student1registered = new Student();
        student1registered.setName("Hanna");
        student1registered.setSurname("Romaniuk");
        student1registered.setPatronymic("Stepanivna");
        student1registered.setUser(userWithId1L);
        student1registered.setGroup(group);

        Student student3 = new Student();
        student3.setName("Viktor");
        student3.setSurname("Hanushchak");
        student3.setPatronymic("Mykolaiovych");
        StudentDTO studentDTO3 = new StudentDTO();
        studentDTO3.setName("Viktor");
        studentDTO3.setSurname("Hanushchak");
        studentDTO3.setPatronymic("Mykolaiovych");
        studentDTO3.setEmail("hanushchak@bigmir.net");
        Student student3registered = new Student();
        student3registered.setName("Viktor");
        student3registered.setSurname("Hanushchak");
        student3registered.setPatronymic("Mykolaiovych");
        student3registered.setUser(userWithId2L);
        student3registered.setGroup(group);

        when(studentMapper.studentImportDTOToStudent(studentImportDTO1)).thenReturn(student1);
        when(studentMapper.studentImportDTOToStudent(studentImportDTO3)).thenReturn(student3);

        when(studentMapper.studentDTOToStudent(studentDTO1)).thenReturn(student1);
        when(userService.automaticRegistration(studentDTO1.getEmail(), Role.ROLE_STUDENT)).thenReturn(userWithId1L);
        when(studentMapper.studentDTOToStudent(studentDTO3)).thenReturn(student3);
        when(userService.automaticRegistration(studentDTO3.getEmail(), Role.ROLE_STUDENT)).thenReturn(userWithId2L);

        when(studentService.save(studentDTO1)).thenReturn(student1);
        when(studentService.save(studentDTO3)).thenReturn(student3);

        when(groupService.getById(anyLong())).thenReturn(group);
        when(groupMapper.groupToGroupDTO(group)).thenReturn(groupDTO);

        when(studentMapper.studentImportDTOToStudent(studentImportDTO1)).thenReturn(student1);
        when(studentMapper.studentToStudentImportDTO(student1registered)).thenReturn(studentImportDTO1);
        when(studentMapper.studentImportDTOToStudent(studentImportDTO3)).thenReturn(student3);
        when(studentMapper.studentToStudentImportDTO(student3registered)).thenReturn(studentImportDTO3);

        List<StudentImportDTO> actualStudents = studentService.saveFromFile(multipartFile, 4L).getNow(new ArrayList<>());
        assertNotNull(actualStudents);
        assertEquals(expectedStudents, actualStudents);
        verify(studentRepository).save(student1);
        verify(studentRepository).save(student3);
        verify(studentRepository).getExistingStudent(student1);
        verify(studentRepository).getExistingStudent(student3);
    }

    private Object[] parametersToTestImport() throws IOException {

        MockMultipartFile multipartFileCsv = new MockMultipartFile("file",
                "students.csv",
                "text/csv",
                Files.readAllBytes(Path.of("src/test/resources/test_students.csv")));

        MockMultipartFile multipartFileTxt = new MockMultipartFile("file",
                "students.txt",
                "text/plain",
                Files.readAllBytes(Path.of("src/test/resources/test_students.csv")));

        return new Object[]{multipartFileCsv, multipartFileTxt};
    }
}
