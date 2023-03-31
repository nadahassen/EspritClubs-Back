package com.stage.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stage.spring.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

}
