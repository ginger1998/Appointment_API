package com.example.Appointment.controller;

import com.example.Appointment.domain.User;
import com.example.Appointment.dto.UserDto;
import com.example.Appointment.exception.BadRequestException;
import com.example.Appointment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author ginger1998
 */

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "user registration",description = "Performs user registration")
    @ApiResponses(value={
            @ApiResponse(responseCode="201",description ="Registration was successful",
            content = {@Content(schema = @Schema(implementation = UserDto.class,
                    description = "User information"))}),
            @ApiResponse(responseCode ="400",description = "Bad request",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PostMapping
    public ResponseEntity createUser(@Parameter(description="User personal information") @RequestBody User user) {
        try {
            return new ResponseEntity(UserDto.fromUser(userService.register(user)), HttpStatus.CREATED);
        }
        catch(BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
