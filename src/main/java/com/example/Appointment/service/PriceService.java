package com.example.Appointment.service;

import com.example.Appointment.domain.Price;
import com.example.Appointment.domain.User;

import java.util.List;

/**
 * @author ginger1998
 */
public interface PriceService {
    List<Price> findAll();
    List<Price> findByTeacher(User teacher);
    Price findById(Long id);
    Price add(Price price);
    Price update(Long id,Price price);
    void delete(Long id);
}
