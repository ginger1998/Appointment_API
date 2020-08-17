package com.example.Appointment.controller;

import com.example.Appointment.domain.Lesson;
import com.example.Appointment.domain.Role;
import com.example.Appointment.domain.Schedule;
import com.example.Appointment.domain.User;
import com.example.Appointment.dto.LessonDto;
import com.example.Appointment.dto.ScheduleDto;
import com.example.Appointment.exception.BadRequestException;
import com.example.Appointment.service.LessonService;
import com.example.Appointment.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ginger1998
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final LessonService lessonService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, LessonService lessonService) {
        this.scheduleService = scheduleService;
        this.lessonService = lessonService;
    }

    @Operation(summary = "Get all schedules",description = "Gets all schedules if user is student or gets schedules of particular teacher if user is teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="List of schedules was successfully received",
            content = {@Content(schema = @Schema(implementation = Object.class,
                    description = "List of schedules"))}),
            @ApiResponse(responseCode ="403",description = "Access denied for unauthorized user",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @GetMapping(produces = "application/json")
    public ResponseEntity getAll(@AuthenticationPrincipal User user){
        List<ScheduleDto> schedules;
        if(user.getRole().equals(Role.STUDENT))
            schedules=scheduleService.findAll().stream()
                    .map(schedule -> ScheduleDto.fromSchedule(schedule)).collect(Collectors.toList());
        else {
            schedules= scheduleService.findByTeacher(user).stream()
                    .map(schedule -> ScheduleDto.fromSchedule(schedule)).collect(Collectors.toList());
        }
        return new ResponseEntity(schedules, HttpStatus.OK);
    }

    @Operation(summary = "Get schedule",description = "Gets a schedule by it's ID",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="Schedule was successfully received",
            content = {@Content(schema = @Schema(implementation = ScheduleDto.class,
                    description = "Schedule Dto"))}),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping(value="{id}",produces = "application/json")
    public ResponseEntity getSchedule(@Parameter(description ="Schedule ID") @PathVariable("id") Long id, @AuthenticationPrincipal User teacher){
        Schedule schedule=scheduleService.findById(id);
        if(schedule.getTeacher().equals(teacher))
            return new ResponseEntity(ScheduleDto.fromSchedule(schedule),HttpStatus.OK);
        else
            return new ResponseEntity("The access is denied!",HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Add schedule",description = "Adding a schedule by a teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="201",description ="Schedule successfully added",
            content = {@Content(schema = @Schema(implementation = ScheduleDto.class,
                    description = "Information about schedule"))}),
            @ApiResponse(responseCode ="400",description = "Bad request: schedule time slot is invalid or intersects with another schedule",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping(produces = "application/json",consumes = "application/json")
    public ResponseEntity addSchedule(@Parameter(description = "Date and time of the schedule") @RequestBody Schedule schedule, @AuthenticationPrincipal User teacher){
        try{
            schedule.setTeacher(teacher);
            return new ResponseEntity(ScheduleDto.fromSchedule(scheduleService.add(schedule)),HttpStatus.CREATED);
        }
        catch (BadRequestException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update schedule",description = "Updates a schedule by a teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="Schedule successfully updated",
            content = {@Content(schema = @Schema(implementation = ScheduleDto.class,
                    description = "Information about schedule"))}),
            @ApiResponse(responseCode ="400",description = "Bad request: schedule time slot is invalid or intersects with another schedule",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping(value="{id}",produces = "application/json",consumes = "application/json")
    public ResponseEntity updateSchedule(@Parameter(description ="Schedule ID") @PathVariable("id") Long id,@Parameter(description = "Date and time of the schedule") @RequestBody Schedule schedule, @AuthenticationPrincipal User teacher) {
        try {
            Schedule scheduleFromDb = scheduleService.findById(id);
            if (scheduleFromDb.getTeacher().equals(teacher)) {
                schedule.setTeacher(teacher);
                return new ResponseEntity(ScheduleDto.fromSchedule(scheduleService.update(id, schedule)), HttpStatus.OK);
            } else {
                return new ResponseEntity("The access is denied!", HttpStatus.FORBIDDEN);
            }
        }
        catch (BadRequestException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete schedule",description = "Deletes a schedule by a teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="Schedule successfully deleted",content = @Content),
            @ApiResponse(responseCode ="400",description = "Bad request",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @DeleteMapping("{id}")
    public ResponseEntity deleteSchedule(@Parameter(description = "Schedule ID") @PathVariable("id") Long id, @AuthenticationPrincipal User teacher){
        if (scheduleService.findById(id).getTeacher().equals(teacher)) {
            try {
                scheduleService.delete(id);
                return ResponseEntity.ok("The schedule has successfully removed");
            }
            catch(BadRequestException e){
                return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
            }
        }
        else
            return new ResponseEntity("The access is denied!",HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Add lesson",description = "Adding a lesson by a student",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="201",description ="Lesson successfully added",
            content = {@Content(schema = @Schema(implementation = LessonDto.class,
                    description = "Information about lesson"))}),
            @ApiResponse(responseCode ="400",description = "Bad request: lesson time slot is invalid or intersects with another lesson",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping(value = "{id}/reserve",produces = "application/json",consumes = "application/json")
    public ResponseEntity addLesson(@AuthenticationPrincipal User student, @Parameter(description = "Schedule Id") @PathVariable("id") Long schedule_id,@Parameter(description = "Date and time of the lesson") @RequestBody Lesson lesson){
        try {
            Schedule schedule = scheduleService.findById(schedule_id);
            lesson.setStudent(student);
            lesson.setTeacher(schedule.getTeacher());
            lesson.setSchedule(schedule);
            return new ResponseEntity(LessonDto.fromLesson(lessonService.add(lesson)),HttpStatus.CREATED);
        }
        catch(BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
