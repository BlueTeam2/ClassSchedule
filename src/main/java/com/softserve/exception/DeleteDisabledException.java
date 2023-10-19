package com.softserve.exception;

public class DeleteDisabledException extends RuntimeException {
    public DeleteDisabledException(Class<?> clazz) {
        super("Unable to delete " + clazz.getSimpleName().toLowerCase());
    }
}
