package com.example.Appointment.service;

import com.example.Appointment.domain.Schedule;
import com.example.Appointment.domain.User;

import java.util.List;

/**
 * @author ginger1998
 */
public interface ScheduleService {
    List<Schedule> findAll();
    List<Schedule> findByTeacher(User teacher);
    Schedule findById(Long id);
    Schedule add(Schedule schedule);
    Schedule update(Long id,Schedule schedule);
    void delete(Long id);
}
