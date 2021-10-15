package ru.project.tasklist.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtCommonException extends AuthenticationException {

    public JwtCommonException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtCommonException(String msg) {
        super(msg);
    }
}
