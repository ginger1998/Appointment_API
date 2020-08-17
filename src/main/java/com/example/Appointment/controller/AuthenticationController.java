package com.example.Appointment.controller;

import com.example.Appointment.dto.AuthenticationRequestDto;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ginger1998
 */

@RestController
@RequestMapping("/login")
public class AuthenticationController {


    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "user authorization",description = "Performs user authorization via unique email and password")
    @ApiResponses(value={@
            ApiResponse(responseCode="200",description ="Authorization was successful",
            content = {@Content(schema = @Schema(implementation = Object.class,
                    description = "Map of parameters that contains username and token"))}),
            @ApiResponse(responseCode ="401",description = "Authorization failed",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PostMapping(produces ="application/json", consumes ="application/json")
    public ResponseEntity login(@Parameter(description = "Dto that contains email and password of user") @RequestBody AuthenticationRequestDto requestDto){
        try{
            return ResponseEntity.ok(userService.login(requestDto));
        }catch(AuthenticationException e){
            return new ResponseEntity(
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED);
        }
    }
}

