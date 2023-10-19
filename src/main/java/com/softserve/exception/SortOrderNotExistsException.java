package com.softserve.exception;

import lombok.Getter;

@Getter
public class SortOrderNotExistsException extends RuntimeException {

    private final String shortMessage;

    public SortOrderNotExistsException(Class<?> clazz, Long id) {
        super(clazz.getSimpleName() + " with id " + id + " does not exists or have not set sort order");
        this.shortMessage = clazz.getSimpleName() + " was not found or have not set sort order";
    }
}
