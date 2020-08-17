package com.example.Appointment.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * @author ginger1998
 */
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

     @NotNull
     private LocalDate date;

     @NotNull
     @Column(name="begin_time")
     private LocalTime begin;

     @NotNull
     @Column(name="end_time")
     private LocalTime end;

     @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="teacher_id")
     private User teacher;

     @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="student_id")
     private User student;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="schedule_id")
    private Schedule schedule;

     @Enumerated(EnumType.STRING)
     @Column(name="status")
     private LessonStatus lessonStatus;

    public Lesson() {
    }

    public Lesson(LocalDate date,LocalTime begin,LocalTime end, User teacher, User student,Schedule schedule, LessonStatus lessonStatus) {
        this.date=date;
        this.begin=begin;
        this.end=end;
        this.teacher=teacher;
        this.student=student;
        this.schedule=schedule;
        this.lessonStatus=lessonStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public LessonStatus getLessonStatus() {
        return lessonStatus;
    }

    public void setLessonStatus(LessonStatus lessonStatus) {
        this.lessonStatus = lessonStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) &&
                Objects.equals(date, lesson.date) &&
                Objects.equals(begin, lesson.begin) &&
                Objects.equals(end, lesson.end) &&
                Objects.equals(teacher, lesson.teacher) &&
                Objects.equals(student, lesson.student) &&
                Objects.equals(schedule, lesson.schedule) &&
                lessonStatus == lesson.lessonStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, begin, end, teacher, student, schedule, lessonStatus);
    }
}
