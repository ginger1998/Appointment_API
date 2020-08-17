package com.example.Appointment.repositories;

import com.example.Appointment.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ginger1998
 */

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
}
