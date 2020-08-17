package com.example.Appointment.repositories;

import com.example.Appointment.domain.Price;
import com.example.Appointment.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ginger1998
 */
public interface PriceRepository extends JpaRepository<Price,Long> {

    List<Price> findByTeacher(User teacher);
    Price findByDurationAndTeacher(Integer duration, User teacher);
}
