package com.example.Appointment.controller;

import com.example.Appointment.domain.Lesson;
import com.example.Appointment.domain.LessonStatus;
import com.example.Appointment.domain.Role;
import com.example.Appointment.domain.User;
import com.example.Appointment.dto.LessonDto;
import com.example.Appointment.exception.BadRequestException;
import com.example.Appointment.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ginger1998
 */

@RestController
@RequestMapping("/lesson")
public class LessonController {

    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Operation(summary = "Get all lessons",description = "Gets all lessons where user are participating",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="List of lessons was successfully received",
            content = {@Content(schema = @Schema(implementation = Object.class,
                    description = "List of lessons"))}),
            @ApiResponse(responseCode ="403",description = "Access denied for unauthorized user",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @GetMapping(produces ="application/json")
    public ResponseEntity getLessons(@AuthenticationPrincipal User user){
        List<LessonDto> lessons;
        if(user.getRole().equals(Role.STUDENT)){
            lessons=lessonService.findByStudent(user).stream()
                    .map(lesson -> LessonDto.fromLesson(lesson))
                    .collect(Collectors.toList());
        }
        else{
            lessons=lessonService.findByTeacher(user).stream()
                    .map(lesson -> LessonDto.fromLesson(lesson))
                    .collect(Collectors.toList());
        }
        return new ResponseEntity(lessons, HttpStatus.OK);
    }

    @Operation(summary = "Cancel lesson",description = "Сancellation of a lesson by a student",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="Lesson successfully canceled",
            content = {@Content(schema = @Schema(implementation = LessonDto.class,
                    description = "Information about lesson"))}),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('STUDENT')")
    @PutMapping(value="{id}/cancel",produces ="application/json")
    public ResponseEntity cancelLesson(@AuthenticationPrincipal User student,@Parameter(description = "Lesson ID") @PathVariable Long id){
        if(lessonService.findById(id).getStudent().equals(student)){
            return new ResponseEntity(LessonDto.fromLesson(lessonService.cancel(id)),HttpStatus.OK);
        }
        else
            return new ResponseEntity("The access is denied!",HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Approve lesson",description = "Approvement of a lesson by a teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="Lesson successfully approved",
            content = {@Content(schema = @Schema(implementation = LessonDto.class,
                    description = "Information about lesson"))}),
            @ApiResponse(responseCode ="400",description = "Bad request: lesson time slot intersects with another lesson",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping(value="{id}/approve",produces="application/json")
    public ResponseEntity approveLesson(@AuthenticationPrincipal User teacher,@Parameter(description = "Lesson ID") @PathVariable Long id){
        Lesson lesson=lessonService.findById(id);
        if(lesson.getTeacher().equals(teacher)){
            try {
                if(lesson.getLessonStatus().equals(LessonStatus.CANCELED)){
                    return new ResponseEntity("Unfortunately, the student has already canceled this reservation!",HttpStatus.OK);
                }
                return new ResponseEntity(LessonDto.fromLesson(lessonService.approve(id)), HttpStatus.OK);
            }
            catch (BadRequestException e){
                return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
            }
        }
        else
            return new ResponseEntity("The access is denied!",HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Decline lesson",description = "Declination of a lesson by a teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="Lesson successfully declined",
            content = {@Content(schema = @Schema(implementation = LessonDto.class,
                    description = "Information about lesson"))}),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping(value="{id}/decline",produces="application/json")
    public ResponseEntity declineLesson(@AuthenticationPrincipal User teacher,@Parameter(description = "Lesson ID") @PathVariable Long id){
        Lesson lesson=lessonService.findById(id);
        if(lesson.getTeacher().equals(teacher)){
            if(lesson.getLessonStatus().equals(LessonStatus.CANCELED)){
                return new ResponseEntity("Unfortunately, the student has already canceled this reservation!",HttpStatus.OK);
            }
            return new ResponseEntity(LessonDto.fromLesson(lessonService.decline(id)),HttpStatus.OK);
        }
        else
            return new ResponseEntity("The access is denied!",HttpStatus.FORBIDDEN);
    }

}
