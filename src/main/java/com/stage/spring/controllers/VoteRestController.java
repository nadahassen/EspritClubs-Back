package com.stage.spring.controllers;

import com.stage.spring.service.IServiceVote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stage.spring.entity.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/Vote")
@CrossOrigin(origins = "http://localhost:4200")
public class VoteRestController {

    @Autowired
    private IServiceVote serviceVote;

    @GetMapping("/get-all-votes")
    public ResponseEntity<List<Vote>> getAllVotes() {
        List<Vote> votes = serviceVote.retrieveAllVotes();
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }

    @GetMapping("getVoteById/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable("id") String id) {
        Vote vote = serviceVote.retrieveVote(id);
        if (vote != null) {
            return new ResponseEntity<>(vote, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addVote")
    public ResponseEntity<Vote> addVote(@RequestBody Vote vote) {
        Vote newVote = serviceVote.addVote(vote);
        return new ResponseEntity<>(newVote, HttpStatus.CREATED);
    }

    @PutMapping("/updateVote/{id}")
    public ResponseEntity<Vote> updateVote(@PathVariable("id") Long id, @RequestBody Vote vote) {
        String voteId = String.valueOf(id);
        if (serviceVote.checkIfExist(voteId)) {
            vote.setIdVote(voteId);
            Vote updatedVote = serviceVote.updateVote(vote);
            return new ResponseEntity<>(updatedVote, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/deleteVote/{id}")
    public ResponseEntity<Void> deleteVote(@PathVariable("id") String id) {
        if (serviceVote.checkIfExist(id)) {
            serviceVote.deleteVote(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/election/{nomElection}")
    public ResponseEntity<Map<String, Long>> getAllVotesByCandidat(@PathVariable("nomElection") String nomElection)
    {
        Map<String, Long> votesByCandidat = serviceVote.retrieveAllVotesByCandidat(nomElection);
        return new ResponseEntity<>(votesByCandidat, HttpStatus.OK);
    }

}
