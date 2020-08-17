package com.example.Appointment.dto;

import com.example.Appointment.domain.Price;

/**
 * @author ginger1998
 */
public class PriceDto {
    private Long id;
    private Double cost;
    private Integer duration;
    private UserDto teacher;

    public PriceDto(Long id, Double cost, Integer duration, UserDto teacher) {
        this.id = id;
        this.cost = cost;
        this.duration = duration;
        this.teacher = teacher;
    }

    public PriceDto(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public UserDto getTeacher() {
        return teacher;
    }

    public void setTeacher(UserDto teacher) {
        this.teacher = teacher;
    }

    public Price toPrice(){
        Price price=new Price();
        price.setId(id);
        price.setCost(cost);
        price.setDuration(duration);
        price.setTeacher(teacher.toUser());
        return price;
    }

    public static PriceDto fromPrice(Price price){
        PriceDto priceDto=new PriceDto();
        priceDto.setId(price.getId());
        priceDto.setCost(price.getCost());
        priceDto.setDuration(price.getDuration());
        priceDto.setTeacher(UserDto.fromUser(price.getTeacher()));
        return priceDto;
    }
}
