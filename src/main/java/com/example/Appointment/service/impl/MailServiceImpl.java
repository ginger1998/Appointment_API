package com.example.Appointment.service.impl;

import com.example.Appointment.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author ginger1998
 */

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String emailSupport;

    @Override
    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(emailSupport);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }
}
