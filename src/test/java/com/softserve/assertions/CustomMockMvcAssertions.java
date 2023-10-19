package com.softserve.assertions;

import com.softserve.exception.apierror.ApiError;
import com.softserve.exception.apierror.ApiValidationError;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomMockMvcAssertions {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final String defaultUrl;
    private final String defaultUrlWithId;

    public CustomMockMvcAssertions(MockMvc mockMvc, ObjectMapper objectMapper, String defaultUrl) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.defaultUrl = defaultUrl;
        this.defaultUrlWithId = defaultUrl + "/{id}";
    }

    public <T> void assertForGet(T expected, String url) throws Exception {
        MockHttpServletRequestBuilder getRequest = get(url).contentType(MediaType.APPLICATION_JSON);
        performRequestWithReturnedStatusOkAndExpectedObject(getRequest, expected);
    }

    public <T> void assertForGetWhenEntityNotFound(int id) throws Exception {
        assertForGetWhenEntityNotFound(id, defaultUrlWithId);
    }

    public <T> void assertForGetWhenEntityNotFound(int id, String url) throws Exception {
        mockMvc.perform(get(url, id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    public <T> void assertForUpdate(T expected) throws Exception {
        assertForUpdate(expected, defaultUrl);
    }

    public <T> void assertForUpdate(T expected, String url) throws Exception {
        MockHttpServletRequestBuilder putRequest = put(url).content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON);
        performRequestWithReturnedStatusOkAndExpectedObject(putRequest, expected);
    }

    public <T> void assertForSave(T expected, Function<T, ResultMatcher> matcherIgnoringId) throws Exception {
        assertForSave(expected, matcherIgnoringId, defaultUrl);
    }

    public <T> void assertForSave(T expected, Function<T, ResultMatcher> matcherIgnoringId,
                                  String url) throws Exception {
        mockMvc.perform(post(url).content(objectMapper.writeValueAsString(expected))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(matcherIgnoringId.apply(expected));
    }

    public <T> void assertForDelete(int id) throws Exception {
        assertForDelete(id, defaultUrlWithId);
    }

    public <T> void assertForDelete(int id, String url) throws Exception {
        mockMvc.perform(delete(url, id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public <T> void assertForGetListWithOneEntity(T expected) throws Exception {
        assertForGetListWithOneEntity(expected, defaultUrl);
    }

    public <T> void assertForGetListWithOneEntity(T expected, String url) throws Exception {
        assertForGetList(Collections.singletonList(expected), url);
    }

    public <T> void assertForGetList(List<T> expected) throws Exception {
        assertForGetList(expected, defaultUrl);
    }

    public <T> void assertForGetList(List<T> expected, String url) throws Exception {
        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(expected.size())))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    public <T> void assertForValidationErrorsOnSave(List<ApiValidationError> errorList,
                                                    T incorrectEntity) throws Exception {
        assertForValidationErrorsOnSave(errorList, incorrectEntity, defaultUrl);
    }

    public <T> void assertForValidationErrorsOnSave(List<ApiValidationError> errorList, T incorrectEntity,
                                                    String url) throws Exception {
        MvcResult result = getResultOfBadPostRequest(incorrectEntity, url);
        ApiError apiError = objectMapper.readValue(result.getResponse().getContentAsString(), ApiError.class);
        assertThat(apiError.getSubErrors()).hasSameSizeAs(errorList).hasSameElementsAs(errorList);
    }

    private <T> void performRequestWithReturnedStatusOkAndExpectedObject(MockHttpServletRequestBuilder requestBuilder,
                                                                         T expected) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    private <T> MvcResult getResultOfBadPostRequest(T incorrectEntity, String url) throws Exception {
        return mockMvc.perform(post(url).content(objectMapper.writeValueAsString(incorrectEntity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andReturn();
    }
}
