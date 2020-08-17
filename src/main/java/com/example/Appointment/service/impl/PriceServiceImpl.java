package com.example.Appointment.service.impl;

import com.example.Appointment.domain.Price;
import com.example.Appointment.domain.User;
import com.example.Appointment.exception.BadRequestException;
import com.example.Appointment.repositories.PriceRepository;
import com.example.Appointment.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ginger1998
 */
@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Autowired
    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public List<Price> findAll() {
        return priceRepository.findAll(Sort.by(Sort.Direction.ASC,"teacher"));
    }

    @Override
    public List<Price> findByTeacher(User teacher) {
        return priceRepository.findByTeacher(teacher);
    }

    @Override
    public Price findById(Long id){
        return priceRepository.findById(id).orElse(null);
    }

    @Override
    public Price add(@Valid Price price) {
        if(priceRepository.findByDurationAndTeacher(price.getDuration(),price.getTeacher())!=null){
            throw new BadRequestException("Price with this duration already exist!");
        }
        return priceRepository.save(price);
    }

    @Override
    public Price update(Long id, @Valid Price price) {
        Price priceByDuration=priceRepository.findByDurationAndTeacher(price.getDuration(),price.getTeacher());
        if(priceByDuration!=null && !priceByDuration.getId().equals(id)){
            throw new BadRequestException("Price with this duration already exist!");
        }
        Price priceFromDb=priceRepository.findById(id).get();
        priceFromDb.setDuration(price.getDuration());
        priceFromDb.setCost(price.getCost());
        return priceRepository.save(priceFromDb);
    }

    @Override
    public void delete(Long id) {
        priceRepository.deleteById(id);
    }
}
