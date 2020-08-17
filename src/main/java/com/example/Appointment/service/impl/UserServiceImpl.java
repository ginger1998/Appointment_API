package com.example.Appointment.service.impl;

import com.example.Appointment.domain.Status;
import com.example.Appointment.domain.User;
import com.example.Appointment.dto.AuthenticationRequestDto;
import com.example.Appointment.exception.BadRequestException;
import com.example.Appointment.repositories.UserRepository;
import com.example.Appointment.security.jwt.JwtTokenProvider;
import com.example.Appointment.service.MailService;
import com.example.Appointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ginger1998
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MailService mailService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public User register(@Valid User user)  {
        if(userRepository.findByEmail(user.getEmail())!=null){
            throw new BadRequestException("User with this email already exist!");
        }
        else{
            if(user.getPassword().length()<8){
                throw new BadRequestException("Password must have no less then 8 character");
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setStatus(Status.ACTIVE);
            User savedUser=userRepository.save(user);
            String link="http://localhost:8080/";
            String message=String.format("Hello, %s %s! \n"+
                    "Thank you for joining Appointment. Start working with Appointment right now by link below: \n"+
                    "%s",savedUser.getFirstname(),savedUser.getLastname(),link);
            mailService.send(user.getEmail(),"Welcome to Appointment!",message);
            return savedUser;
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users=userRepository.findAll();
        return users;
    }

    @Override
    public User findById(Long id) {
        User user=userRepository.findById(id).orElse(null);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user=userRepository.findByEmail(email);
        return user;
    }

    @Override
    public void delete(Long id) {
        User user=userRepository.findById(id).orElse(null);
        if(user.getStudentLessons().isEmpty() && user.getTeacherLessons().isEmpty()) {
            userRepository.deleteById(id);
        }
        else
            throw new BadRequestException("You cannot delete your profile until there are lessons with your participation!");
    }

    @Override
    public User update(Long id, @Valid User user) {
        User userFromDb=userRepository.findById(id).get();
        String encodePassword=bCryptPasswordEncoder.encode(user.getPassword());
        if(!userFromDb.getPassword().equals(encodePassword)){
            userFromDb.setPassword(encodePassword);
        }
        userFromDb.setFirstname(user.getFirstname());
        userFromDb.setLastname(user.getLastname());
        userFromDb.setPhone(user.getPhone());
        userFromDb.setEmail(user.getEmail());
        return userRepository.save(userFromDb);
    }

    @Override
    public Map<Object,Object> login(AuthenticationRequestDto requestDto){
        String username=requestDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,requestDto.getPassword()));
        User user=findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("Bad credentials!");
        }
        String token=jwtTokenProvider.createToken(username, Arrays.asList(user.getRole()));
        Map<Object,Object> response=new HashMap<>();
        response.put("username",username);
        response.put("token",token);
        return response;
    }
}
