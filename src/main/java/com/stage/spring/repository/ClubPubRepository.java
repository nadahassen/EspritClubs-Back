package com.stage.spring.repository;

import com.stage.spring.entity.Club;
import com.stage.spring.entity.ClubPublication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubPubRepository extends JpaRepository<ClubPublication, Long> {

    @Query("Select c from ClubPublication c where c.isDeleted= 0")
    public List<ClubPublication> getUndeletedClubs();

}
