package com.stage.spring.controllers;

import com.stage.spring.entity.Condidature;
import com.stage.spring.service.ServiceCondidature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/Condidature")
@CrossOrigin(origins = "http://localhost:4200")
public class CondidatureRestController {


    @Autowired
    ServiceCondidature serviceCondidature;

    @GetMapping("/retrieveAllCondidatures")
    public List<Condidature> retrieveAllCondidatures() {
        List<Condidature> list = serviceCondidature.retrieveAllCondidature();
        return list;
    }

    @PostMapping("/addCondidature")
    public Condidature addCondidature(@RequestBody Condidature A) {
        return serviceCondidature.addCondidature(A);
    }

    @PostMapping("/updateCondidature")
    public Condidature updateCondidature(@RequestBody Condidature A) {
        return serviceCondidature.updateCondidature(A);
    }

    @DeleteMapping("/deleteCondidature/{id}")
    public void deleteCondidature(@PathVariable("id") Long id) {
        serviceCondidature.deleteCondidature(id);
    }

    @GetMapping("retrieveCondidatureById/{id}")
    public Condidature retrieveCondidatureById(@PathVariable Long id) {
        return serviceCondidature.retrieveCondidatureById(id);
    }

}
