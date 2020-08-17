package com.example.Appointment.controller;

import com.example.Appointment.domain.Price;
import com.example.Appointment.domain.Role;
import com.example.Appointment.domain.User;
import com.example.Appointment.dto.LessonDto;
import com.example.Appointment.dto.PriceDto;
import com.example.Appointment.dto.ScheduleDto;
import com.example.Appointment.exception.BadRequestException;
import com.example.Appointment.service.PriceService;
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
@RequestMapping("/price")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @Operation(summary = "Get all prices",description = "Gets all prices if user is student or gets prices of particular teacher if user is teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="List of prices was successfully received",
                    content = {@Content(schema = @Schema(implementation = Object.class,
                            description = "List of prices"))}),
            @ApiResponse(responseCode ="403",description = "Access denied for unauthorized user",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @GetMapping(produces = "application/json")
    public ResponseEntity getAll(@AuthenticationPrincipal User user){
        List<PriceDto> prices;
        if(user.getRole().equals(Role.STUDENT))
            prices=priceService.findAll().stream()
                .map(price -> PriceDto.fromPrice(price)).collect(Collectors.toList());
        else {
            prices= priceService.findByTeacher(user).stream()
                    .map(price -> PriceDto.fromPrice(price)).collect(Collectors.toList());
        }
        return new ResponseEntity(prices,HttpStatus.OK);
    }
    @Operation(summary = "Get price",description = "Gets a price by it's ID",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="Price was successfully received",
                    content = {@Content(schema = @Schema(implementation = PriceDto.class,
                            description = "Price Dto"))}),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="404",description = "Not found",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping(value="{id}",produces ="application/json")
    public ResponseEntity getPrice(@Parameter(description = "Price ID") @PathVariable("id") Long id, @AuthenticationPrincipal User teacher) {
        try {
            Price price = priceService.findById(id);
            if(price.getTeacher().equals(teacher))
                return new ResponseEntity(PriceDto.fromPrice(price),HttpStatus.OK);
            else
                return new ResponseEntity("The access is denied!",HttpStatus.FORBIDDEN);
        }
        catch(NullPointerException e){
            return new ResponseEntity("Price not found",HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add price",description = "Adding a price by a teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="201",description ="Price successfully added",
                    content = {@Content(schema = @Schema(implementation =PriceDto.class,
                            description = "Information about price"))}),
            @ApiResponse(responseCode ="400",description = "Bad request",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping(produces = "application/json",consumes = "application/json")
    public ResponseEntity addPrice(@Parameter(description = "Cost and duration of the price") @RequestBody Price price, @AuthenticationPrincipal User teacher) {
        try {
            price.setTeacher(teacher);
            return new ResponseEntity(PriceDto.fromPrice(priceService.add(price)),HttpStatus.CREATED);
        }
        catch(BadRequestException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update price",description = "Updates a price by a teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="Price successfully updated",
                    content = {@Content(schema = @Schema(implementation = PriceDto.class,
                            description = "Information about price"))}),
            @ApiResponse(responseCode ="400",description = "Bad request",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping("{id}")
    public ResponseEntity updatePrice(@Parameter(description = "Price ID") @PathVariable("id") Long id,
                                      @Parameter(description = "Cost and duration of the price") @RequestBody Price price,
                                      @AuthenticationPrincipal User teacher) {
        Price priceFromDb=priceService.findById(id);
        if(priceFromDb.getTeacher().equals(teacher)) {
            try {
                price.setTeacher(teacher);
                return new ResponseEntity(PriceDto.fromPrice(priceService.update(id, price)), HttpStatus.OK);
            }
            catch(BadRequestException e){
                return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity("The access is denied!",HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Delete price",description = "Deletes a price by a teacher",security = {@SecurityRequirement(name="JwtToken")})
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description ="Price successfully deleted",content = @Content),
            @ApiResponse(responseCode ="403",description = "Access denied",content = @Content),
            @ApiResponse(responseCode ="500",description = "Internal server error",content = @Content)})
    @PreAuthorize("hasAuthority('TEACHER')")
    @DeleteMapping("{id}")
    public ResponseEntity deletePrice(@Parameter(description = "Price ID") @PathVariable("id") Long id, @AuthenticationPrincipal User teacher){
        if (priceService.findById(id).getTeacher().equals(teacher)) {
            priceService.delete(id);
            return new ResponseEntity("The price has successfully removed",HttpStatus.OK);
        }
        else
            return new ResponseEntity("The access is denied!",HttpStatus.FORBIDDEN);
    }

}
