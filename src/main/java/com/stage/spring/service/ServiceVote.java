package com.stage.spring.service;

import com.stage.spring.entity.Vote;
import com.stage.spring.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j

public class ServiceVote implements IServiceVote{

    @Autowired
    private VoteRepository voteRepository;
    @Override
    public List<Vote> retrieveAllVotes() {
        return voteRepository.findAll();    }

    @Override
    public Vote addVote(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public void deleteVote(String id) {
        voteRepository.deleteById(id);

    }

    @Override
    public Vote updateVote(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public Vote retrieveVote(String id) {
        return voteRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean checkIfExist(String id) {
        return voteRepository.existsById(id);
    }
// récupère tous les votes pour une élection donnée,
// filtre éventuellement les votes par nom de candidat ou nom de parti,
// et renvoie un Map avec les noms des candidats et le nombre de votes pour chaque candidat.
// Cela permet de récupérer les résultats des votes pour une élection spécifique et d'effectuer
// des calculs ou des analyses supplémentaires sur les données.
    @Override
    public Map<String, Long> retrieveAllVotesByCandidat(String nomElection) {

        List<Vote> allVotesByElection = this.retrieveAllVotes();
        List<String> candidats = new ArrayList<>();

        allVotesByElection.removeIf(v -> !v.getNomElection().equals(nomElection));

//        else if (nomCandidat != null) {
//            allVotes.removeIf(v -> !v.getNomCandidat().equals(nomCandidat));
//        } else if (nomParti != null){
//            allVotes.removeIf(v -> !v.getNomParti().equals(nomParti));
//        }
        for (Vote vote: allVotesByElection) {
            candidats.add(vote.getNomCandidat());
        }
        return candidats.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }
}
