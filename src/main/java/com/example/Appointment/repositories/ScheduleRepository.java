package com.example.Appointment.repositories;

import com.example.Appointment.domain.Lesson;
import com.example.Appointment.domain.Schedule;
import com.example.Appointment.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author ginger1998
 */
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findByTeacher(User teacher);

    @Query(value="select * from schedule where (date=?) and ((? between begin_time and end_time) or (? between begin_time and end_time)) and (teacher_id=?)",nativeQuery = true)
    List<Schedule> findIntersection(LocalDate date, LocalTime begin,LocalTime end,Long teacher_id);

    @Query(value="select * from lesson where (schedule_id=?) and ((status='APPROVE') or (status='PENDING'))",nativeQuery = true)
    List<Lesson> findLessons(Long schedule_id);
}
