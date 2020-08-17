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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author ginger1998
 */

@RestController
@RequestMapping("/profile")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user info",description = "Gets user personal information by it's ID",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="User info was successfully received",
            content = {@Content(schema = @Schema(implementation = UserDto.class,
                    description = "User information"))}),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="404",description = "Not found",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @GetMapping("{id}")
    public ResponseEntity get(@Parameter(description = "User ID") @PathVariable("id") Long id, @AuthenticationPrincipal User user){
        try {
            if(userService.findById(id).equals(user))
                return new ResponseEntity(UserDto.fromUser(user),HttpStatus.OK);
            else{
                return new ResponseEntity("The access is denied",HttpStatus.FORBIDDEN);
            }
        }
        catch (NullPointerException e){
            return new ResponseEntity("The user not found",HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "Update user info",description = "Updates user personal information by it's ID",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="User info was successfully updated",
            content = {@Content(schema = @Schema(implementation = UserDto.class,
                    description = "User information"))}),
            @ApiResponse(responseCode ="400",description = "Bad request",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PutMapping("{id}")
    public ResponseEntity update(
            @Parameter(description = "User ID")  @PathVariable("id") Long id,
            @Parameter(description = "User info") @RequestBody User updateUser,
            @AuthenticationPrincipal User user){
        if(user.equals(userService.findById(id))) {
            updateUser.setRole(user.getRole());
            if(updateUser.getPassword()==null)
                updateUser.setPassword(user.getPassword());
            User finalUser = userService.update(id, updateUser);
            return new ResponseEntity<>(UserDto.fromUser(finalUser), HttpStatus.OK);
        }
        else{
            return new ResponseEntity("The access is denied",HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Delete user",description = "Deletes user profile by it's ID",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="User info was successfully removed",content = @Content),
            @ApiResponse(responseCode ="400",description = "Bad request",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id")Long id, @AuthenticationPrincipal User user){
        if (userService.findById(id).equals(user)) {
            try {
                userService.delete(id);
                return ResponseEntity.ok("The user has successfully removed");
            }
            catch (BadRequestException e){
                return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
            }
        }
        else
           return new ResponseEntity("The access is denied",HttpStatus.FORBIDDEN);
    }
}
