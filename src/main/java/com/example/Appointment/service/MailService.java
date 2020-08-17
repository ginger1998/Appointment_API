package com.example.Appointment.service;

/**
 * @author ginger1998
 */
public interface MailService {
    void send(String emailTo,String subject,String message);
}
