package com.stage.spring.service;
import com.stage.spring.entity.Election;
import com.stage.spring.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
public interface IServiceVote {
    List<Vote> retrieveAllVotes();

    Vote addVote(Vote vote);

    void deleteVote(Long id);

    Vote updateVote(Vote vote);

    Vote retrieveVote(Long id);

    Boolean checkIfExist(Long id);

    Page<Vote> getVotesPagedAndSorted(int offset, int pageSize, String field);

    Page<Vote> getVotesPaged(Pageable pageable);

   // Map<String, Long> getVoteResultsForElection(Long electionId, String candidateName, String partyName);
    //List<Vote> getVotesForElection(Long electionId);

}