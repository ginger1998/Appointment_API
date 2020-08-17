package com.example.Appointment.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @author ginger1998
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
