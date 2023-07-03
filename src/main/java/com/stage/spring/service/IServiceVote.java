package com.stage.spring.service;
import com.stage.spring.entity.Vote;

import java.util.List;
import java.util.Map;
public interface IServiceVote {


    List<Vote> retrieveAllVotes();

    Vote addVote(Vote vote);

    void deleteVote(String id);

    Vote updateVote(Vote vote);

    Vote retrieveVote(String id);

    Boolean checkIfExist(String id);

    Map<String, Long> retrieveAllVotesByCandidat(String nomElection);
}
