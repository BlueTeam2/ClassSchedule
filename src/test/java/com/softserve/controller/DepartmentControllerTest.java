package com.softserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.assertions.CustomMockMvcAssertions;
import com.softserve.config.DBConfigTest;
import com.softserve.config.MyWebAppInitializer;
import com.softserve.config.SecurityConfig;
import com.softserve.config.SecurityWebApplicationInitializer;
import com.softserve.config.WebMvcConfig;
import com.softserve.dto.DepartmentDTO;
import com.softserve.dto.TeacherDTO;
import com.softserve.exception.apierror.ApiValidationError;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        username = "vbforwork702@mail.com",
        password = "$2a$10$42sZYaqffhxKah7sTFsm3OXF02qdUUykPfVWPO3GguHvoDui.WsIi",
        roles = "MANAGER"
)
@Sql(value = "classpath:create-departments-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DepartmentControllerTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule smr = new SpringMethodRule();

    private CustomMockMvcAssertions assertions;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DepartmentDTO departmentDTOWithId4L;

    private DepartmentDTO disableDepartmentDTOWithId5L;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        assertions = new CustomMockMvcAssertions(mockMvc, objectMapper, "/departments");

        departmentDTOWithId4L = DepartmentDTO.builder()
                .id(4L)
                .name("Department4")
                .disable(false)
                .build();

        disableDepartmentDTOWithId5L = DepartmentDTO.builder()
                .id(5L)
                .name("Department5")
                .disable(true)
                .build();
    }

    @Test
    public void getAll() throws Exception {
        assertions.assertForGetListWithOneEntity(departmentDTOWithId4L);
    }

    @Test
    public void getById() throws Exception {
        assertions.assertForGet(departmentDTOWithId4L, "/departments/4");
    }

    @Test
    @WithMockUser(
            username = "vbforwork702@mail.com",
            password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.",
            roles = "USER"
    )
    public void returnForbiddenIfAuthenticatedUserRoleIsNotManager() throws Exception {
        mockMvc.perform(get("/departments/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void saveDepartment() throws Exception {
        DepartmentDTO expected = new DepartmentDTO();
        expected.setName("save new departments");
        assertions.assertForSave(expected, e -> jsonPath("$.name").value(e.getName()));
    }

    @Test
    public void updateDepartment() throws Exception {
        assertions.assertForUpdate(departmentDTOWithId4L);
    }

    @Test
    public void deleteById() throws Exception {
        assertions.assertForDelete(5);
    }

    @Test
    public void returnBadRequestIfReferencesOnDepartmentExist() throws Exception {
        mockMvc.perform(delete("/departments/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnNotFoundIfEntityNotFoundedById() throws Exception {
        assertions.assertForGetWhenEntityNotFound(20);
    }

    @Test
    public void throwFieldAlreadyExistsExceptionOnSave() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(departmentDTOWithId4L.getName());
        assertThatReturnedFieldAlreadyExistsException(post("/departments"), departmentDTO);
    }

    @Test
    public void throwFieldAlreadyExistsExceptionOnUpdate() throws Exception {
        DepartmentDTO departmentDTO = departmentDTOWithId4L;
        departmentDTO.setName(disableDepartmentDTOWithId5L.getName());
        assertThatReturnedFieldAlreadyExistsException(put("/departments"), departmentDTO);
    }

    public Object[] parametersForTestValidationException() {
        String errorMessage = "Name cannot be blank";
        return new Object[]{
                new Object[]{null, errorMessage},
                new Object[]{"", errorMessage},
                new Object[]{"  ", errorMessage},
        };
    }

    @Parameters
    @Test
    public void testValidationException(String incorrectName, String errorMessage) throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(incorrectName);
        ApiValidationError error = new ApiValidationError(
                "Department",
                "name",
                incorrectName,
                errorMessage
        );
        assertions.assertForValidationErrorsOnSave(Collections.singletonList(error), departmentDTO);
    }

    @Test
    public void getAllDisable() throws Exception {
        assertions.assertForGetListWithOneEntity(disableDepartmentDTOWithId5L, "/departments/disabled");
    }

    @Test
    public void getAllTeachers() throws Exception {
        DepartmentDTO departmentDTO = departmentDTOWithId4L;

        TeacherDTO firstTeacher = new TeacherDTO();
        firstTeacher.setId(4L);
        firstTeacher.setDisable(false);
        firstTeacher.setName("Ivan");
        firstTeacher.setSurname("Ivanov");
        firstTeacher.setPatronymic("Ivanovych");
        firstTeacher.setPosition("docent");
        firstTeacher.setDepartmentDTO(departmentDTO);

        assertions.assertForGetListWithOneEntity(firstTeacher, "/departments/4/teachers");
    }

    private <T> void assertThatReturnedFieldAlreadyExistsException(MockHttpServletRequestBuilder requestBuilder,
                                                                   T groupDTO) throws Exception {
        mockMvc.perform(requestBuilder.content(objectMapper.writeValueAsString(groupDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message")
                        .value("Department with provided name already exists"));
    }
}
