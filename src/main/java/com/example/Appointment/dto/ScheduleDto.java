package com.example.Appointment.dto;

import com.example.Appointment.domain.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author ginger1998
 */
public class ScheduleDto {
    private Long id;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private UserDto teacher;

    public ScheduleDto(Long id, LocalDate date, LocalTime begin, LocalTime end, UserDto teacher) {
        this.id = id;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.teacher = teacher;
    }

    public ScheduleDto(){}

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

    public Schedule toSchedule(){
        Schedule schedule=new Schedule();
        schedule.setId(id);
        schedule.setDate(date);
        schedule.setBegin(begin);
        schedule.setEnd(end);
        schedule.setTeacher(teacher.toUser());
        return schedule;
    }

    public static ScheduleDto fromSchedule(Schedule schedule){
        ScheduleDto scheduleDto=new ScheduleDto();
        scheduleDto.setId(schedule.getId());
        scheduleDto.setDate(schedule.getDate());
        scheduleDto.setBegin(schedule.getBegin());
        scheduleDto.setEnd(schedule.getEnd());
        scheduleDto.setTeacher(UserDto.fromUser(schedule.getTeacher()));
        return scheduleDto;
    }
}
