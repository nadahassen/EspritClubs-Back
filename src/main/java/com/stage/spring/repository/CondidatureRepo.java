package com.stage.spring.repository;
import com.stage.spring.entity.Condidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CondidatureRepo extends JpaRepository<Condidature, Long> {
}
