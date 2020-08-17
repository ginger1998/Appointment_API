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
public interface LessonRepository extends JpaRepository<Lesson,Long> {

    List<Lesson> findByTeacher(User teacher);
    List<Lesson> findByStudent(User teacher);

    @Query(value="select * from lesson where (date=?) and ((? between begin_time and end_time) or (? between begin_time and end_time)) and (teacher_id=?) and (status='APPROVE')",nativeQuery = true)
    List<Lesson> findTeacherIntersection(LocalDate date, LocalTime begin, LocalTime end, Long teacher_id);

    @Query(value="select * from lesson where (date=?) and ((? between begin_time and end_time) or (? between begin_time and end_time)) and (student_id=?) and (status='APPROVE')",nativeQuery = true)
    List<Lesson> findStudentIntersection(LocalDate date, LocalTime begin, LocalTime end, Long student_id);

//    @Query(value="select * from schedule where (date=?) and (? between begin_time and end_time) and (? between begin_time and end_time) and (teacher_id=?)",nativeQuery = true)
//    Schedule findSchedule(LocalDate date,LocalTime begin,LocalTime end,Long teacher_id);
}
