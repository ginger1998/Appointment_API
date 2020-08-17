package com.example.Appointment.service.impl;

import com.example.Appointment.domain.Lesson;
import com.example.Appointment.domain.Schedule;
import com.example.Appointment.domain.User;
import com.example.Appointment.exception.BadRequestException;
import com.example.Appointment.repositories.ScheduleRepository;
import com.example.Appointment.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ginger1998
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll(Sort.by(Sort.Direction.ASC,"teacher"));
    }

    @Override
    public List<Schedule> findByTeacher(User teacher) {
        return scheduleRepository.findByTeacher(teacher);
    }

    @Override
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    @Override
    public Schedule add(@Valid Schedule schedule) {
        List<Schedule> scheduleIntersections=scheduleRepository.findIntersection(schedule.getDate(),schedule.getBegin(),schedule.getEnd(),schedule.getTeacher().getId());
        if(scheduleIntersections.isEmpty()) {
            if (schedule.getBegin().compareTo(schedule.getEnd()) < 0)
                return scheduleRepository.save(schedule);
            else
                throw new BadRequestException("The begin time must not be greater then end time!");
        }
        else
            throw new BadRequestException("The time slot of schedule intersects with another one!");
    }

    @Override
    public Schedule update(Long id, @Valid Schedule schedule) {
        List<Schedule> scheduleIntersections=scheduleRepository.findIntersection(schedule.getDate(),schedule.getBegin(),schedule.getEnd(),schedule.getTeacher().getId());
        if(schedule.getBegin().compareTo(schedule.getEnd())<0) {
            if ((scheduleIntersections.isEmpty() || (scheduleIntersections.size() == 1 && scheduleIntersections.get(0).getId() == id))) {
                Schedule scheduleFromDb = scheduleRepository.findById(id).get();
                scheduleFromDb.setDate(schedule.getDate());
                scheduleFromDb.setBegin(schedule.getBegin());
                scheduleFromDb.setEnd(schedule.getEnd());
                return scheduleRepository.save(scheduleFromDb);
            }
            else
                throw new BadRequestException("The time slot of schedule intersects with another one!");
        }
        else
            throw new BadRequestException("The begin time must not be greater then end time!");
    }

    @Override
    public void delete(Long id) {
        List<Lesson> lessons=scheduleRepository.findLessons(id);
        if(lessons.isEmpty()) {
            scheduleRepository.deleteById(id);
        }
        else
            throw new BadRequestException("You have approved or pending lessons at this schedule!");
    }
}
