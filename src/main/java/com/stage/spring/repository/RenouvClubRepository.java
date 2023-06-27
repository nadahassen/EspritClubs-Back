package com.stage.spring.repository;
import com.stage.spring.entity.Club;
import com.stage.spring.entity.RenouvellementClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RenouvClubRepository  extends JpaRepository<RenouvellementClub, Long> {
    @Query("Select c from Club c where c.isDeleted= 0")
    public List<RenouvellementClub> getUndeletedClubs();
}
