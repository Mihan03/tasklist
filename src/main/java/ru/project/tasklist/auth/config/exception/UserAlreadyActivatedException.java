package ru.project.tasklist.auth.config.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyActivatedException extends AuthenticationException {
    public UserAlreadyActivatedException(String msg) {
        super(msg);
    }
}
