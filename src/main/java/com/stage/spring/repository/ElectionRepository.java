package com.stage.spring.repository;

import com.stage.spring.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface ElectionRepository extends JpaRepository<Election,Long>{
}
