package com.softserve.exception;

public class FieldNullException extends RuntimeException {
    public FieldNullException(Class<?> clazz, String searchParam) {
        super("Field " + searchParam + " in " + clazz.getSimpleName().toLowerCase() + " is null");
    }
}
