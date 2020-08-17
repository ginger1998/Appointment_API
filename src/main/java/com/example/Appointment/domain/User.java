package com.example.Appointment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ginger1998
 */


@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(columnDefinition = "serial")
    private Long id;

    @NotBlank(message="Поле ім'я є обов'зковим для вводу")
    @Size(min=2, max=50)
    private String firstname;

    @NotBlank(message="Поле прізвище є обов'зковим для вводу")
    @Size(min=2, max=50)
    private String lastname;

    @Email
    @NotBlank(message="Поле пошта є обов'зковим для вводу")
    private String email;

    @NotBlank(message="Поле пароль є обов'зковим для вводу")
    private String password;

    @NotBlank(message="Поле номер телефону є обов'зковим для вводу")
    @Size(min=10, max=13)
    private String phone;

    @NotNull(message="Потрібно вказати свою позицію")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<Schedule> schedule;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
    private Set<Price> prices;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
    private Set<Lesson> teacherLessons;

    @JsonIgnore
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private Set<Lesson> studentLessons;

    public User() {
    }

    public User(String firstname, String lastname, String password, String phone, String email, String role) {
        this.firstname=firstname;
        this.lastname=lastname;
        this.password=password;
        this.phone=phone;
        this.email=email;
        this.role=Role.valueOf(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(Set<Schedule> schedule) {
        this.schedule = schedule;
    }

    public Set<Price> getPrices() {
        return prices;
    }

    public void setPrices(Set<Price> prices) {
        this.prices = prices;
    }

    public Set<Lesson> getTeacherLessons() {
        return teacherLessons;
    }

    public void setTeacherLessons(Set<Lesson> teacherLessons) {
        this.teacherLessons = teacherLessons;
    }

    public Set<Lesson> getStudentLessons() {
        return studentLessons;
    }

    public void setStudentLessons(Set<Lesson> studentLessons) {
        this.studentLessons = studentLessons;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return status.name().equals("ACTIVE");
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(role).stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.name())
                ).collect(Collectors.toList());
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                firstname.equals(user.firstname) &&
                lastname.equals(user.lastname) &&
                phone.equals(user.phone) &&
                email.equals(user.email) &&
                role == user.role;
    }



    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, phone, email, role);
    }

}
