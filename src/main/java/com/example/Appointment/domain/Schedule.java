package com.example.Appointment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

/**
 * @author ginger1998
 */

@Entity
public class Schedule {

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

    @JsonIgnore
    @OneToMany(mappedBy = "schedule",cascade = CascadeType.ALL)
    private Set<Lesson> lessons;

    public Schedule() {
    }

    public Schedule(LocalDate date,LocalTime begin, LocalTime end,User teacher) {
        this.date=date;
        this.begin=begin;
        this.end=end;
        this.teacher=teacher;
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

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) &&
                Objects.equals(date, schedule.date) &&
                Objects.equals(begin, schedule.begin) &&
                Objects.equals(end, schedule.end) &&
                Objects.equals(teacher, schedule.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, begin, end, teacher);
    }
}
