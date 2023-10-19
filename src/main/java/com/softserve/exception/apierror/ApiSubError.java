package com.softserve.exception.apierror;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
@JsonSubTypes.Type(value = ApiValidationError.class)
public interface ApiSubError {
}
