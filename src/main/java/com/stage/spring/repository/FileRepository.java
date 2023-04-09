package com.stage.spring.repository;

import com.stage.spring.entity.File;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends JpaRepository<File,Long>{

}
