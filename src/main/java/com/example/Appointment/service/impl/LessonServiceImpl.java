package com.example.Appointment.service.impl;

import com.example.Appointment.domain.Lesson;
import com.example.Appointment.domain.LessonStatus;
import com.example.Appointment.domain.Schedule;
import com.example.Appointment.domain.User;
import com.example.Appointment.exception.BadRequestException;
import com.example.Appointment.repositories.LessonRepository;
import com.example.Appointment.service.LessonService;
import com.example.Appointment.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ginger1998
 */
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final MailService mailService;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, MailService mailService) {
        this.lessonRepository = lessonRepository;
        this.mailService = mailService;
    }

    @Override
    public List<Lesson> findByTeacher(User teacher) {
        return lessonRepository.findByTeacher(teacher);
    }

    @Override
    public List<Lesson> findByStudent(User student) {
        return lessonRepository.findByStudent(student);
    }

    @Override
    public Lesson findById(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    @Override
    public Lesson add(@Valid Lesson lesson) {
        if(lesson.getBegin().compareTo(lesson.getEnd())<0) {
            Schedule schedule=lesson.getSchedule();
            if (schedule.getDate().equals(lesson.getDate()) && schedule.getBegin().compareTo(lesson.getBegin())<=0 && schedule.getEnd().compareTo(lesson.getEnd())>=0) {
                if (lessonRepository.findTeacherIntersection(lesson.getDate(), lesson.getBegin(), lesson.getEnd(), lesson.getTeacher().getId()).isEmpty()) {
                    if (lessonRepository.findStudentIntersection(lesson.getDate(), lesson.getBegin(), lesson.getEnd(), lesson.getStudent().getId()).isEmpty()) {
                        lesson.setLessonStatus(LessonStatus.PENDING);
                        Lesson savedLesson=lessonRepository.save(lesson);
                        User student=lesson.getStudent();
                        User teacher=lesson.getTeacher();
                        String link="http://localhost:8080/lesson";
                        String linkCancel=String.format("%s/%s/cancel",link,lesson.getId());
                        String linkApprove=String.format("%s/%s/approve",link,lesson.getId());
                        String linkDecline=String.format("%s/%s/decline",link,lesson.getId());
                        String subject="New reservation";
                        String messageStudent=String.format("Dear %s %s! \n"+
                                "You successfully reserved lesson with %s %s. You can cancel your reservation by link below: \n"+
                                "%s"+
                                "",student.getFirstname(),student.getLastname(),teacher.getFirstname(),teacher.getLastname(),linkCancel);
                        String messageTeacher=String.format("Dear %s %s! \n"+
                                "Student %s %s has reserved a lesson with you. You can approve reservation by link below: \n"+
                                "%s\n"+
                                "or decline by link below:\n"+
                                "%s",teacher.getFirstname(),teacher.getLastname(),student.getFirstname(),student.getLastname(),linkApprove,linkDecline);
                        mailService.send(student.getEmail(),subject,messageStudent);
                        mailService.send(teacher.getEmail(),subject,messageTeacher);
                        return savedLesson;
                    } else
                        throw new BadRequestException("You already have lesson with another teacher at that time slot!");
                } else
                    throw new BadRequestException("The teacher already have lesson with another student at that time slot");
            } else
                throw new BadRequestException("Time slot does not fit in existing time frames of this schedule!");
        } else
            throw new BadRequestException("The begin time must not be greater then end time!");
    }

    @Override
    public void delete(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public Lesson approve(Long id) {
        Lesson lesson=lessonRepository.findById(id).get();
        if(lessonRepository.findTeacherIntersection(lesson.getDate(),lesson.getBegin(),lesson.getEnd(),lesson.getTeacher().getId()).isEmpty()) {
            if(lessonRepository.findStudentIntersection(lesson.getDate(),lesson.getBegin(),lesson.getEnd(),lesson.getStudent().getId()).isEmpty()){
                lesson.setLessonStatus(LessonStatus.APPROVE);
                return lessonRepository.save(lesson);
            }
            else {
                lesson.setLessonStatus(LessonStatus.CANCELED);
                lessonRepository.save(lesson);
                throw new BadRequestException("The student already have lesson with another teacher at that time slot");
            }
        }
        else
            throw new BadRequestException("You already have lesson with another student at that time slot!");
    }

    @Override
    public Lesson decline(Long id) {
        Lesson lesson=lessonRepository.findById(id).get();
        lesson.setLessonStatus(LessonStatus.DECLINE);
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson cancel(Long id) {
        Lesson lesson=lessonRepository.findById(id).get();
        lesson.setLessonStatus(LessonStatus.CANCELED);
        return lessonRepository.save(lesson);
    }
}
