package com.example.Appointment.service;

import com.example.Appointment.domain.Lesson;
import com.example.Appointment.domain.User;

import java.util.List;

/**
 * @author ginger1998
 */
public interface LessonService {
    List<Lesson> findByTeacher(User teacher);
    List<Lesson> findByStudent(User student);
    Lesson findById(Long id);
    Lesson add(Lesson lesson);
    void delete(Long id);
    Lesson approve(Long id);
    Lesson decline(Long id);
    Lesson cancel(Long id);
}
