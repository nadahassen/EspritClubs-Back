package com.stage.spring.service;

import com.stage.spring.entity.Condidature;
import com.stage.spring.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IServiceCondidature {
    List<Condidature> retrieveAllCondidature ();

    Condidature  addCondidature (Condidature a) ;

    void deleteCondidature (Long id);

    Condidature  updateCondidature(Condidature  a);

    Condidature  retrieveCondidatureById(Long id);
}
