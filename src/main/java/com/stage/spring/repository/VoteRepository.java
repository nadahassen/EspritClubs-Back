package com.stage.spring.repository;

import com.stage.spring.entity.Vote;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository

public interface VoteRepository extends JpaRepository<Vote, Long> {
  //  List<Vote> findByElectionIdAndNomCandidatAndNomClub(Long electionId, String candidateName, String partyName);
   // List<Vote> findByElectionId(Long electionId);

}
