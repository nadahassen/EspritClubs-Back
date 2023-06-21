package com.stage.spring.service;

import com.stage.spring.entity.Condidature;
import com.stage.spring.entity.Image;
import com.stage.spring.repository.CondidatureRepo;
import com.stage.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class ServiceCondidature implements IServiceCondidature {

    @Autowired
    CondidatureRepo condidatureRepo;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IServiceImage imageService;


    @Override
    public List<Condidature> retrieveAllCondidature() {
        return condidatureRepo.findAll();
    }

    @Override
    public Condidature addCondidature(Condidature a) {
        return condidatureRepo.save(a);
    }

    @Override
    public void deleteCondidature(Long id) {
        condidatureRepo.deleteById(id);

    }

    @Override
    public Condidature updateCondidature(Condidature a) {
        return condidatureRepo.save(a);
    }

    @Override
    public Condidature retrieveCondidatureById(Long id) {
        Condidature a =condidatureRepo.findById(id).get();
        return a;    }
}
