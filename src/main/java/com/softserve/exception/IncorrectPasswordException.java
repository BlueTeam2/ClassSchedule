package com.softserve.exception;

public class IncorrectPasswordException extends RuntimeException {
    private static final String DEFAULT_MESSAGE
            = "Password must contains at least: 8 characters(at least: 1 upper case, 1 lower case, "
            + "1 number and 1 special character('!@#$%^&*')) and no more 30 characters.";

    public IncorrectPasswordException() {
        super(DEFAULT_MESSAGE);
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
