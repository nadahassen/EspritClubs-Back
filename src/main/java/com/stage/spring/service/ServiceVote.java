package com.stage.spring.service;

import com.stage.spring.entity.Vote;
import com.stage.spring.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j

public class ServiceVote implements IServiceVote {

    @Autowired
    private VoteRepository voteRepository;


    @Override
    public List<Vote> retrieveAllVotes() {
        return voteRepository.findAll();
    }

    @Override
    public Vote addVote(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public void deleteVote(Long id) {
        voteRepository.deleteById(id);

    }

    @Override
    public Vote updateVote(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public Vote retrieveVote(Long id) {
        return voteRepository.findById(id).orElse(null);    }

    @Override
    public Boolean checkIfExist(Long id) {
        return voteRepository.existsById(id);
    }

    //retrieves all the votes for a given election, filters the votes by candidate name and party name if specified,
    // and returns a Map with the candidate names and the number of votes for each candidate.
  /*  public Map<String, Long> getVoteResultsForElection(Long electionId, String candidateName, String clubame) {
        List<Vote> votes = voteRepository.findByElectionIdAndNomCandidatAndNomClub(electionId, candidateName, clubame);
        return votes.stream()
                .collect(Collectors.groupingBy(Vote::getNomCandidat, Collectors.counting()));
    }

    //retrieve the vote results for a specific election, filtered by candidate name and party name.
    @Override
    public List<Vote> getVotesForElection(Long electionId) {
        return voteRepository.findByElectionId(electionId);
    }*/
    @Override
    public Page<Vote> getVotesPagedAndSorted(int offset, int pageSize, String field) {
        return voteRepository.findAll(PageRequest.of(offset, pageSize, Sort.by(field)));
    }

    @Override
    public Page<Vote> getVotesPaged(Pageable pageable) {
        return voteRepository.findAll(pageable);
    }
}
// récupère tous les votes pour une élection donnée,
// filtre éventuellement les votes par nom de candidat ou nom de parti,
// et renvoie un Map avec les noms des candidats et le nombre de votes pour chaque candidat.
// Cela permet de récupérer les résultats des votes pour une élection spécifique et d'effectuer
// des calculs ou des analyses supplémentaires sur les données.
