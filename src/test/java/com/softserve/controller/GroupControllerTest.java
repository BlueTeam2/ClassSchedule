package com.softserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.assertions.CustomMockMvcAssertions;
import com.softserve.config.*;
import com.softserve.dto.*;
import com.softserve.exception.apierror.ApiValidationError;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
@Sql(value = {"classpath:create-groups-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class GroupControllerTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule smr = new SpringMethodRule();

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    private CustomMockMvcAssertions assertions;

    private GroupDTO groupDTOWithID4L;

    private GroupDTO disabledGroupDTOWithID5L;

    private GroupDTO groupDTOWithID6L;

    private StudentWithoutGroupDTO studentDTOWithId4LForGroupWithId4L;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        assertions = new CustomMockMvcAssertions(mockMvc, objectMapper, "/groups");

        groupDTOWithID4L = GroupDTO.builder()
                .id(4L)
                .disable(false)
                .title("444")
                .build();

        groupDTOWithID6L = GroupDTO.builder()
                .id(6L)
                .title("666")
                .disable(false)
                .build();

        disabledGroupDTOWithID5L = GroupDTO.builder()
                .id(5L)
                .disable(true)
                .title("555")
                .build();

        studentDTOWithId4LForGroupWithId4L = StudentWithoutGroupDTO.builder()
                .id(4L)
                .name("Name One")
                .surname("Surname One")
                .patronymic("Patron One")
                .email("zzz1@gmail.com")
                .build();
    }

    @Test
    public void getAllGroups() throws Exception {
        List<GroupDTO> expected = List.of(groupDTOWithID6L, groupDTOWithID4L);
        assertions.assertForGetList(expected, "/groups");
    }

    @Test
    public void getGroupById() throws Exception {
        assertions.assertForGet(groupDTOWithID4L, "/groups/4");
    }

    @Test
    @WithMockUser(
            username = "second@mail.com",
            password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.",
            roles = "TEACHER"
    )
    @Sql(value = {"classpath:create-lessons-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getByTeacherId() throws Exception {
        GroupDTO expectedGroup = GroupDTO.builder().id(4L).disable(false).title("111").build();
        assertions.assertForGetListWithOneEntity(expectedGroup, "/groups/teacher/4");
    }

    @Test
    public void returnNotFoundIfGroupNotFoundedById() throws Exception {
        assertions.assertForGetWhenEntityNotFound(445, "/groups/{id}");
    }

    @Test
    public void getGroupWithStudentsById() throws Exception {
        mockMvc.perform(get("/groups/4/with-students").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(groupDTOWithID4L.getId()))
                .andExpect(jsonPath("$.title").value(groupDTOWithID4L.getTitle()))
                .andExpect(jsonPath("$.students", hasSize(1)))
                .andExpect(jsonPath("$.students[0]").value(studentDTOWithId4LForGroupWithId4L));
    }

    @Test
    public void returnNotFoundIfGroupWithStudentsNotFoundedById() throws Exception {
        assertions.assertForGetWhenEntityNotFound(44, "/groups/{id}/with-students");
    }

    @Test
    public void saveGroup() throws Exception {
        GroupDTO groupDTO = GroupDTO.builder()
                .title("sdsdsdsd")
                .build();
        assertions.assertForSave(groupDTO, GroupControllerTest::matchIgnoringId);
    }

    @Test
    public void saveAfterGroupWithId() throws Exception {
        GroupOrderDTO groupDTO = new GroupOrderDTO();
        groupDTO.setTitle("sdsdsdsd");
        groupDTO.setDisable(false);
        groupDTO.setAfterId(6L);
        String groupJSON = "{\n" +
                           "  \"afterId\": 6,\n" +
                           "  \"disable\": false,\n" +
                           "  \"id\": 0,\n" +
                           "  \"title\": \"sdsdsdsd\"\n" +
                           "}";
        mockMvc.perform(post("/groups/after").content(groupJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"));
        List<GroupDTO> expected = List.of(groupDTOWithID6L, groupDTO, groupDTOWithID4L);
        groupDTO.setId(1L);
        assertions.assertForGetList(expected, "/groups");
    }

    @Test
    public void saveAfterGroupWithoutId() throws Exception {
        GroupDTO groupDTO = GroupDTO.builder()
                .title("sdsdsdsd")
                .disable(false)
                .build();
        assertions.assertForSave(groupDTO, GroupControllerTest::matchIgnoringId, "/groups/after");
        List<GroupDTO> expected = List.of(groupDTO, groupDTOWithID6L, groupDTOWithID4L);
        groupDTO.setId(1L);
        assertions.assertForGetList(expected, "/groups");
    }

    @Test
    public void updateGroupOrderWithId() throws Exception {
        GroupOrderDTO groupDTO = new GroupOrderDTO();
        groupDTO.setTitle("sdsdsdsd");
        groupDTO.setDisable(false);
        groupDTO.setAfterId(4L);
        groupDTO.setId(1L);
        String groupJSON = "{\n" +
                           "  \"afterId\": 4,\n" +
                           "  \"disable\": false,\n" +
                           "  \"id\": 1,\n" +
                           "  \"title\": \"sdsdsdsd\"\n" +
                           "}";
        mockMvc.perform(post("/groups/after").content(groupJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"));
        groupDTO.setAfterId(6L);
        groupJSON = groupJSON.replace("4", "6");
        mockMvc.perform(put("/groups/after").content(groupJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        List<GroupDTO> expected = List.of(groupDTOWithID6L, groupDTO, groupDTOWithID4L);
        assertions.assertForGetList(expected, "/groups");
    }

    @Test
    public void updateGroupOrderWithoutId() throws Exception {
        GroupDTO groupDTO = GroupDTO.builder()
                .title("sdsdsdsd")
                .disable(false)
                .build();
        assertions.assertForSave(groupDTO, GroupControllerTest::matchIgnoringId, "/groups/after");
        groupDTO.setId(1L);
        assertions.assertForUpdate(groupDTO, "/groups/after");
        List<GroupDTO> expected = List.of(groupDTO, groupDTOWithID6L, groupDTOWithID4L);
        assertions.assertForGetList(expected, "/groups");
    }

    @Test
    public void throwFieldAlreadyExistsExceptionForTitleWhenSave() throws Exception {
        GroupDTO groupDTO = groupDTOWithID4L;
        groupDTO.setId(null);
        assertThatReturnedFieldAlreadyExistsException(post("/groups"), groupDTO);
    }

    @Test
    public void updateGroup() throws Exception {
        GroupForUpdateDTO groupForUpdateDTO = GroupForUpdateDTO.builder()
                .id(groupDTOWithID4L.getId())
                .title(groupDTOWithID4L.getTitle())
                .disable(true)
                .build();
        assertions.assertForUpdate(groupForUpdateDTO);
    }

    @Test
    public void throwFieldAlreadyExistsExceptionForTitleWhenUpdate() throws Exception {
        GroupDTO groupDTO = groupDTOWithID4L;
        groupDTO.setTitle(disabledGroupDTOWithID5L.getTitle());
        assertThatReturnedFieldAlreadyExistsException(put("/groups"), groupDTO);
    }

    @Test
    public void getAllDisabledGroups() throws Exception {
        assertions.assertForGetListWithOneEntity(disabledGroupDTOWithID5L, "/groups/disabled");
    }

    @Test
    @WithMockUser(username = "first@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "USER")
    public void returnForbiddenIfAuthenticatedUserRoleIsNotManager() throws Exception {
        mockMvc.perform(get("/groups/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "sixth@mail.com", password = "$2a$04$SpUhTZ/SjkDQop/Zvx1.seftJdqvOploGce/wau247zQhpEvKtz9.", roles = "USER")
    public void returnForbiddenIfAuthenticatedUserRoleIsNotTeacher() throws Exception {
        mockMvc.perform(get("/groups/teacher/{teacherId}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteGroup() throws Exception {
        assertions.assertForDelete(5);
    }

    public Object[] parametersForTestValidationException() {
        String lengthErrorMessage = "Title must be between 2 and 35 characters long";
        return new Object[]{
                new Object[]{null, "Title cannot be empty"},
                new Object[]{"T", lengthErrorMessage},
                new Object[]{RandomStringUtils.random(36, "abc"), lengthErrorMessage}
        };
    }

    @Parameters
    @Test
    public void testValidationException(String incorrectTitle, String errorMessage) throws Exception {
        GroupDTO groupDTO = GroupDTO.builder()
                .title(incorrectTitle)
                .build();
        ApiValidationError error = new ApiValidationError(
                "Group",
                "title",
                incorrectTitle,
                errorMessage
        );
        assertions.assertForValidationErrorsOnSave(Collections.singletonList(error), groupDTO);
    }

    private static ResultMatcher matchIgnoringId(GroupDTO groupDTO) {
        return ResultMatcher.matchAll(jsonPath("$.title").value(groupDTO.getTitle()));
    }

    private <T> void assertThatReturnedFieldAlreadyExistsException(MockHttpServletRequestBuilder requestBuilder,
                                                                   T groupDTO) throws Exception {
        mockMvc.perform(requestBuilder.content(objectMapper.writeValueAsString(groupDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message")
                        .value("Group with provided title already exists"));
    }
}
