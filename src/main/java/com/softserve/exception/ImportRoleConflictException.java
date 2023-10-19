package com.softserve.exception;

public class ImportRoleConflictException extends RuntimeException {
    public ImportRoleConflictException(String message) {
        super(message);
    }
}
