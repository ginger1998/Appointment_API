package com.example.Appointment.dto;

import com.example.Appointment.domain.Lesson;
import com.example.Appointment.domain.LessonStatus;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author ginger1998
 */
public class LessonDto {
    private Long id;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private UserDto teacher;
    private UserDto student;
    private ScheduleDto schedule;
    private LessonStatus lessonStatus;

    public LessonDto(){}

    public LessonDto(Long id, LocalDate date, LocalTime begin, LocalTime end, UserDto teacher, UserDto student,ScheduleDto schedule, LessonStatus lessonStatus) {
        this.id = id;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.teacher = teacher;
        this.student = student;
        this.schedule=schedule;
        this.lessonStatus = lessonStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getBegin() {
        return begin;
    }

    public void setBegin(LocalTime begin) {
        this.begin = begin;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public UserDto getTeacher() {
        return teacher;
    }

    public void setTeacher(UserDto teacher) {
        this.teacher = teacher;
    }

    public UserDto getStudent() {
        return student;
    }

    public void setStudent(UserDto student) {
        this.student = student;
    }

    public ScheduleDto getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleDto schedule) {
        this.schedule = schedule;
    }

    public LessonStatus getLessonStatus() {
        return lessonStatus;
    }

    public void setLessonStatus(LessonStatus lessonStatus) {
        this.lessonStatus = lessonStatus;
    }

    public Lesson toLesson(){
        Lesson lesson= new Lesson();
        lesson.setId(id);
        lesson.setDate(date);
        lesson.setBegin(begin);
        lesson.setEnd(end);
        lesson.setTeacher(teacher.toUser());
        lesson.setStudent(student.toUser());
        lesson.setSchedule(schedule.toSchedule());
        lesson.setLessonStatus(lessonStatus);
        return lesson;
    }

    public static LessonDto fromLesson(Lesson lesson){
            LessonDto lessonDto=new LessonDto();
            lessonDto.setId(lesson.getId());
            lessonDto.setDate(lesson.getDate());
            lessonDto.setBegin(lesson.getBegin());
            lessonDto.setEnd(lesson.getEnd());
            lessonDto.setTeacher(UserDto.fromUser(lesson.getTeacher()));
            lessonDto.setStudent(UserDto.fromUser(lesson.getStudent()));
            lessonDto.setSchedule(ScheduleDto.fromSchedule(lesson.getSchedule()));
            lessonDto.setLessonStatus(lesson.getLessonStatus());
            return lessonDto;
    }
}
