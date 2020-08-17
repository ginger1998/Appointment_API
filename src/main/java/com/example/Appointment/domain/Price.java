package com.example.Appointment.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author ginger1998
 */

@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Integer duration;

    @NotNull
    private Double cost;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="teacher_id")
    private User teacher;

    public Price() {
    }

    public Price(Integer duration,Double cost,User teacher) {
        this.duration=duration;
        this.cost=cost;
        this.teacher=teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(id, price.id) &&
                Objects.equals(duration, price.duration) &&
                Objects.equals(cost, price.cost) &&
                Objects.equals(teacher, price.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, duration, cost, teacher);
    }
}
