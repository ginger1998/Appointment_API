package com.example.Appointment.service;

import com.example.Appointment.domain.User;
import com.example.Appointment.dto.AuthenticationRequestDto;

import java.util.List;
import java.util.Map;

/**
 * @author ginger1998
 */
public interface UserService {
    User register(User user);
    List<User> getAll();
    User findById(Long id);
    User findByEmail(String email);
    void delete(Long id);
    User update(Long id, User user);
    Map<Object,Object> login(AuthenticationRequestDto requestDto);
}
