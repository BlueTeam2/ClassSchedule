package com.softserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.assertions.CustomMockMvcAssertions;
import com.softserve.config.DBConfigTest;
import com.softserve.config.MyWebAppInitializer;
import com.softserve.config.SecurityConfig;
import com.softserve.config.SecurityWebApplicationInitializer;
import com.softserve.config.WebMvcConfig;
import com.softserve.dto.GroupDTO;
import com.softserve.dto.StudentDTO;
import com.softserve.exception.apierror.ApiValidationError;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Category(IntegrationTestCategory.class)
@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(
        classes = {
                WebMvcConfig.class,
                DBConfigTest.class,
                MyWebAppInitializer.class,
                SecurityConfig.class,
                SecurityWebApplicationInitializer.class
        }
)
@WebAppConfiguration
@WithMockUser(
        username = "first@mail.com",
        password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.",
        roles = "MANAGER"
)
@Sql(value = {"classpath:create-students-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class StudentControllerTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule smr = new SpringMethodRule();

    private static MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private CustomMockMvcAssertions assertions;

    private StudentDTO studentDTOWithId8L;
    private StudentDTO studentDTOWithId9L;
    private StudentDTO studentDTOWithId10L;
    private StudentDTO studentDTOWithId12L;

    private final GroupDTO groupDTO = GroupDTO.builder()
            .id(1L)
            .disable(false)
            .title("First Title")
            .build();

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        assertions = new CustomMockMvcAssertions(mockMvc, OBJECT_MAPPER, "/students");

        studentDTOWithId8L = StudentDTO.builder()
                .id(8L)
                .name("First Name1")
                .surname("First Surname1")
                .patronymic("First Patronymic1")
                .email("aware.123db@gmail.com")
                .group(groupDTO)
                .build();

        studentDTOWithId9L = StudentDTO.builder()
                .id(9L)
                .name("First Name2")
                .surname("First Surname2")
                .patronymic("First Patronymic2")
                .email("student@gmail.com")
                .group(groupDTO)
                .build();

        studentDTOWithId10L = StudentDTO.builder()
                .id(10L)
                .name("Hanna")
                .surname("Romaniuk")
                .patronymic("Stepanivna")
                .email("romaniuk@gmail.com")
                .group(groupDTO)
                .build();

        studentDTOWithId12L = StudentDTO.builder()
                .id(12L)
                .name("Fourth")
                .surname("Fourth")
                .patronymic("Fourth")
                .email("Fourth@test.com")
                .group(groupDTO)
                .build();

    }

    @Test
    public void getAllStudents() throws Exception {
        assertions.assertForGetList(asList(
                studentDTOWithId8L, studentDTOWithId9L, studentDTOWithId10L, studentDTOWithId12L));
    }

    @Test
    public void getStudentById() throws Exception {
        assertions.assertForGet(studentDTOWithId8L, "/students/8");
    }

    @Test
    public void saveStudent() throws Exception {
        StudentDTO expected = studentDTOWithId8L;
        expected.setId(null);
        expected.setEmail("dfdfdf@gmail.com");
        assertions.assertForSave(expected, StudentControllerTest::matchStudentExcludingId);
    }

    @Test
    public void throwFieldAlreadyExistsExceptionWhenSave() throws Exception {
        StudentDTO studentDTO = studentDTOWithId8L;
        studentDTO.setId(null);
        assertThatReturnedFieldAlreadyExistsException(post("/students"), studentDTO);
    }

    @Test
    public void updateStudent() throws Exception {
        assertions.assertForUpdate(studentDTOWithId10L);
    }

    @Test
    public void throwFieldAlreadyExistsExceptionWhenUpdate() throws Exception {
        StudentDTO studentDTO = studentDTOWithId8L;
        studentDTO.setId(null);
        studentDTO.setEmail(studentDTOWithId9L.getEmail());
        assertThatReturnedFieldAlreadyExistsException(post("/students"), studentDTO);
    }

    @Test
    public void deleteStudent() throws Exception {
        assertions.assertForDelete(8);
    }

    @Test
    @WithMockUser(
            username = "vbforweewrk702@mail.com",
            password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.",
            roles = "USER"
    )
    public void returnForbiddenIfAuthenticatedUserRoleIsNotManagerOnSave() throws Exception {
        assertThatReturnedForbiddenStatus(post("/students")
                .content(OBJECT_MAPPER.writeValueAsString(studentDTOWithId8L)));
    }

    @Test
    @WithMockUser(
            username = "vbforweewrk702@mail.com",
            password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.",
            roles = "USER"
    )
    public void returnForbiddenIfAuthenticatedUserRoleIsNotManagerOnUpdate() throws Exception {
        assertThatReturnedForbiddenStatus(put("/students")
                .content(OBJECT_MAPPER.writeValueAsString(studentDTOWithId8L)));
    }

    @Test
    @WithMockUser(
            username = "vbforweewrk702@mail.com",
            password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.",
            roles = "USER"
    )
    public void returnForbiddenIfAuthenticatedUserRoleIsNotManagerOnDelete() throws Exception {
        assertThatReturnedForbiddenStatus(delete("/students/4"));
    }

    public Object[] parametersForTestValidationException() {
        String objectError = "Student";

        //Object 1
        StudentDTO studentDTOWithNullValues = new StudentDTO();
        studentDTOWithNullValues.setEmail("12345Asd@test.com");
        ApiValidationError nameIsNullError = new ApiValidationError(
                objectError,
                "name",
                null,
                "Name cannot be empty"
        );
        ApiValidationError surnameIsNullError = new ApiValidationError(
                objectError,
                "surname",
                null,
                "Surname cannot be empty"
        );
        ApiValidationError patronymicIsNullError = new ApiValidationError(
                objectError,
                "patronymic",
                null,
                "Patronymic cannot be empty"
        );
        ApiValidationError groupIsNullError = new ApiValidationError(
                objectError,
                "group",
                null,
                "Group cannot be null"
        );

        List<ApiValidationError> errorListWithNullValues = Arrays.asList(
                nameIsNullError,
                surnameIsNullError,
                patronymicIsNullError,
                groupIsNullError
        );

        //Object 2
        String wordWithLength1 = "T";
        StudentDTO studentDTOWithValuesLengthsLessThanMin = StudentDTO.builder()
                .name(wordWithLength1)
                .surname(wordWithLength1)
                .patronymic(wordWithLength1)
                .email("studenttttt@gmail.com")
                .group(groupDTO)
                .build();

        ApiValidationError nameLengthIs1Error = new ApiValidationError(
                objectError,
                "name",
                wordWithLength1,
                "Name must be between 2 and 35 characters long"
        );
        ApiValidationError surnameLengthIs1Error = new ApiValidationError(
                objectError,
                "surname",
                wordWithLength1,
                "Surname must be between 2 and 35 characters long"
        );
        ApiValidationError patronymicLengthIs1Error = new ApiValidationError(
                objectError,
                "patronymic",
                wordWithLength1,
                "Patronymic must be between 2 and 35 characters long"
        );
        List<ApiValidationError> errorListWithMinLength = Arrays.asList(
                nameLengthIs1Error,
                surnameLengthIs1Error,
                patronymicLengthIs1Error
        );

        //Object 3
        String wordWithLength55 = RandomStringUtils.random(45, "abc") + "@gmail.com";
        StudentDTO studentDTOWithValuesLengthsMoreThanMax = StudentDTO.builder()
                .name(wordWithLength55)
                .surname(wordWithLength55)
                .patronymic(wordWithLength55)
                .email("studentttttt@gmail.com")
                .group(groupDTO)
                .build();

        ApiValidationError nameLengthIs55Error = new ApiValidationError(
                objectError,
                "name",
                wordWithLength55,
                "Name must be between 2 and 35 characters long"
        );
        ApiValidationError surnameLengthIs55Error = new ApiValidationError(
                objectError,
                "surname",
                wordWithLength55,
                "Surname must be between 2 and 35 characters long"
        );
        ApiValidationError patronymicLengthIs55Error = new ApiValidationError(
                objectError,
                "patronymic",
                wordWithLength55,
                "Patronymic must be between 2 and 35 characters long"
        );
        List<ApiValidationError> errorListWithMaxLength = Arrays.asList(
                nameLengthIs55Error,
                surnameLengthIs55Error,
                patronymicLengthIs55Error
        );

        //Last Object
        objectError = "User";
        String incorrectEmail = "saass";
        StudentDTO studentDTOWithIncorrectEmail = StudentDTO.builder()
                .name("sdsdsd")
                .surname("dsdsds")
                .patronymic("ddsd")
                .email(incorrectEmail)
                .group(groupDTO)
                .build();
        ApiValidationError incorrectEmailError = new ApiValidationError(
                objectError,
                "email",
                incorrectEmail,
                "must be a well-formed email address"
        );

        return new Object[]{
                new Object[]{studentDTOWithNullValues, errorListWithNullValues},
                new Object[]{studentDTOWithValuesLengthsLessThanMin, errorListWithMinLength},
                new Object[]{studentDTOWithValuesLengthsMoreThanMax, errorListWithMaxLength},
                new Object[]{studentDTOWithIncorrectEmail, singletonList(incorrectEmailError)},
        };
    }

    @Parameters
    @Test
    //GZ
    public void testValidationException(StudentDTO studentDTO, List<ApiValidationError> errorList) throws Exception {
        assertions.assertForValidationErrorsOnSave(errorList, studentDTO);
    }

    @Test
    public void saveStudentsFromFile() throws Exception {

        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "students.csv",
                "text/csv",
                Files.readAllBytes(Path.of("src/test/resources/test_students2.csv")));

        mockMvc.perform(multipart("/students/import").file(multipartFile).param("groupId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(6)))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("First"))
                .andExpect(jsonPath("$[0].surname").value("First"))
                .andExpect(jsonPath("$[0].patronymic").value("First"))
                .andExpect(jsonPath("$[0].email").value("First@test.com"))
                .andExpect(jsonPath("$[0].groupDTO.id").value(2))
                .andExpect(jsonPath("$[0].groupDTO.title").value("Second Title"))
                .andExpect(jsonPath("$[0].importSaveStatus").value("SAVED"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Second"))
                .andExpect(jsonPath("$[1].surname").value("Second"))
                .andExpect(jsonPath("$[1].patronymic").value("Second"))
                .andExpect(jsonPath("$[1].email").value("Second@test.com"))
                .andExpect(jsonPath("$[1].groupDTO.id").value(2))
                .andExpect(jsonPath("$[1].groupDTO.title").value("Second Title"))
                .andExpect(jsonPath("$[1].importSaveStatus").value("SAVED"))

                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value("Third"))
                .andExpect(jsonPath("$[2].surname").value("Third"))
                .andExpect(jsonPath("$[2].patronymic").value("Third"))
                .andExpect(jsonPath("$[2].email").value("Second@test.com"))
                .andExpect(jsonPath("$[2].groupDTO").doesNotExist())
                .andExpect(jsonPath("$[2].importSaveStatus").value("ALREADY_EXIST"))

                .andExpect(jsonPath("$[3].id").value(12))
                .andExpect(jsonPath("$[3].name").value("Fourth"))
                .andExpect(jsonPath("$[3].surname").value("Fourth"))
                .andExpect(jsonPath("$[3].patronymic").value("Fourth"))
                .andExpect(jsonPath("$[3].email").value("Fourth@test.com"))
                .andExpect(jsonPath("$[3].groupDTO.id").value(1))
                .andExpect(jsonPath("$[3].groupDTO.title").value("First Title"))
                .andExpect(jsonPath("$[3].importSaveStatus").value("ALREADY_EXIST"))

                .andExpect(jsonPath("$[4].id").doesNotExist())
                .andExpect(jsonPath("$[4].name").value("Five"))
                .andExpect(jsonPath("$[4].surname").value("Five"))
                .andExpect(jsonPath("$[4].patronymic").value("Five"))
                .andExpect(jsonPath("$[4].email").value("Five@test.com"))
                .andExpect(jsonPath("$[4].groupDTO").doesNotExist())
                .andExpect(jsonPath("$[4].importSaveStatus").value("ROLE_CONFLICT"))

                .andExpect(jsonPath("$[5].id").doesNotExist())
                .andExpect(jsonPath("$[5].name").value("Name"))
                .andExpect(jsonPath("$[5].surname").value("Surname"))
                .andExpect(jsonPath("$[5].patronymic").value("Patronymic"))
                .andExpect(jsonPath("$[5].email").value("Email"))
                .andExpect(jsonPath("$[5].groupDTO").doesNotExist())
                .andExpect(jsonPath("$[5].importSaveStatus").value("VALIDATION_ERROR"));
    }

    private void assertThatReturnedFieldAlreadyExistsException(MockHttpServletRequestBuilder requestBuilder,
                                                               StudentDTO studentDTO) throws Exception {
        mockMvc.perform(requestBuilder.content(OBJECT_MAPPER.writeValueAsString(studentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message")
                        .value("Student with provided email already exists"));
    }

    private static ResultMatcher matchStudentExcludingId(StudentDTO expected) {
        return ResultMatcher.matchAll(
                jsonPath("$.name").value(expected.getName()),
                jsonPath("$.surname").value(expected.getSurname()),
                jsonPath("$.patronymic").value(expected.getPatronymic()),
                jsonPath("$.email").value(expected.getEmail()),
                jsonPath("$.group").value(expected.getGroup())
        );
    }

    public void assertThatReturnedForbiddenStatus(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
