package com.example.Appointment.exception;

/**
 * @author ginger1998
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg){super(msg);}
    public BadRequestException(String msg,Throwable t){super(msg,t);}

}
