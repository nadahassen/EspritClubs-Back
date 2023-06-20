package com.stage.spring.repository;

import com.stage.spring.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
@Repository

public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query("Select c from Club c where c.isDeleted= 0")
    public List<Club>  getUndeletedClubs();
    List<Club> findByUserId(Long idUser);



}
